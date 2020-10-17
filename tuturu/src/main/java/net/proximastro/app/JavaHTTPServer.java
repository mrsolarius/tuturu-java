package net.proximastro.app;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

// chaque client et récupérer dans un seul thread
public class JavaHTTPServer implements Runnable{

    static final File WEB_ROOT = new File("./tuturu/src/main/resources/public");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    // port to listen connection
    static final int PORT = 8080;

    // verbose mode
    static final boolean verbose = true;

    // Client Connection via Socket Class
    private Socket connect;

    public JavaHTTPServer(Socket c) {
        connect = c;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Le Server démmare...\nIl écoute sur le port : " + PORT + " ...\n");

            // écoute des requete des utilisateur utilisateur dans une boucle infini
            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if (verbose) {
                    System.out.println("Ouverture de la connection. ( le " + new Date() + ")");
                }

                // Création d'un thread dedier au serveur
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Erreur du serveur lors de la connexion : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // gestion des cas particulier des connexion client
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String URI = null;

        try {

            // récupération de la requete avec un buffer reader
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // récupération du flux pour l'imprimer
            out = new PrintWriter(connect.getOutputStream());
            // récupération des données entrante pour les POST
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // lecture de la premiere ligne de la connexion client
            String input = in.readLine();
            // récpration du token de connexion
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // récupération de la methode du client
            // récupération du fichier de la requete
            URI = parse.nextToken().toLowerCase();
            ControllerManager ctrlm = new ControllerManager();

            // Verification de la methode
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {

                if (verbose) {
                    System.out.println("501 La methode : " + method + " n'est pas implementer.");
                }

                // renvoie du fichier au client
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                //Lecture du fichier à envoyer au client
                byte[] fileData = readFileData(file, fileLength);
                out.println(headerBuilder(501,"C'est pas encore implementer",null,fileLength));
                out.println();
                out.flush();
                //ecriture du contenu
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                // methode head ou get
                String routeURI;
                if (!(routeURI = ctrlm.getRegisterRoutesURI(URI)).equals("")){
                    RouteController RC = ctrlm.getRouteController(routeURI);
                } else {

                    File file = new File(WEB_ROOT, URI);
                    int fileLength = (int) file.length();
                    String content = getContentType(URI);

                    if (method.equals("GET")) { // GET method donc on renvoie le contenu du fichier
                        byte[] fileData = readFileData(file, fileLength);

                        // Envoie du header HTTP
                        out.println(headerBuilder(200, "OK LOL", content, fileLength));
                        out.println(); // Line vide entre le head et le contenu TR2S IMPORTANT
                        out.flush();

                        dataOut.write(fileData, 0, fileLength);
                        dataOut.flush();
                    }

                    if (verbose) {
                        System.out.println("Le fichier " + URI + " du type " + content + " et envoyer");
                    }
                }

            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, URI);
            } catch (IOException ioe) {
                System.err.println("Erreur le fichier na pas était trouver : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Erreur serveur : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // fermeture du soket
            } catch (Exception e) {
                System.err.println("Fermeture du flux : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Fermeture de la conexion.\n");
            }
        }


    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        byte[] fileData = readFileData(file, fileLength);
        //Création du header de retour
        out.println(headerBuilder(404,"File Not Found",null,fileLength));
        out.println();
        out.flush();
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("Le Fichier " + fileRequested + " est introuvable");
        }
    }

    public static String headerBuilder(int statusCode, String statusMessage,String contentMimeType,int fileLength){
        contentMimeType = contentMimeType == null ? "text/plain" : contentMimeType;
        return "HTTP/1.1 "+statusCode+" "+statusMessage+"\\r\\n"+
                "Server: Java Tuturu HTTP Server : 1.0\\r\\n"+
                "Date: " + new Date()+"\\r\\n"+
                "Content-type: " + contentMimeType+"\\r\\n"+
                "Content-length: " + fileLength+"\\r\\n"+
                "\\n";
    }

    public static HashMap<String,String> buildGETMap(String URI){
        //inisialisation de la hash map des paramettres
        HashMap<String, String> getParams = new HashMap<>();
        if (URI.split("\\?+").length==2) {
            //Sépartion de tous les parametre de la requette get
            String[] params = URI.split("\\?+")[1].split("&");
            for (int i = 0; i < params.length; i++) {
                //definition du tupe de valeur cles et valeur
                String[] keyValue = params[i].split("=");
                //verification qu'il y a bien une valeur apres le =
                if (keyValue.length == 2) {
                    //ajouts des paramettre à la hashmap
                    getParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return getParams;
    }

    public static HashMap<String,String> buildURIMap(String URI, String routeURI){
        String[] URIArray = URI.split("/");
        String[] routeURIArray = routeURI.split("/");
        HashMap<String,String> URIMap = new HashMap<>();
        if (URIArray.length == routeURIArray.length){
            for (int i = 0; i <URIArray.length ; i++) {
                if (routeURIArray[i].startsWith("{") && routeURIArray[i].endsWith("}")){
                    URIMap.put(
                            routeURIArray[i]
                                    .replace("{","")
                                    .replace("}",""),
                            URIArray[i]
                    );
                }
            }
        }
        return  URIMap;
    }

}