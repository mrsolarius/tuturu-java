package net.proximastro.app;

import javax.sound.midi.Soundbank;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

// chaque client et récupérer dans un seul thread
public class JavaHTTPServer implements Runnable{

    static final File WEB_ROOT = new File("src/main/resources/public");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "500.html";
    // port to listen connection
    static final int PORT = 8080;

    static boolean awaitPayload = false;

    // verbose mode
    static final boolean verbose = true;

    // Client Connection via Socket Class
    private Socket connect;

    public JavaHTTPServer(Socket c) {
        connect = c;
    }

    public static void main(String[] args) {
        try {
            System.out.println(WEB_ROOT.getPath());
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
        System.out.println("nouvelle requette entrente");
        try {

            // récupération du header avec le buffer reader
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));

            // récupération du flux pour l'imprimer
            out = new PrintWriter(connect.getOutputStream());
            // récupération des données entrante pour les POST
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // lecture de la premiere ligne de la connexion client
            String input = in.readLine();
            if (input==null)return;
            // récpration du token de connexion
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // récupération de la methode du client
            // récupération du fichier de la requete
            URI = parse.nextToken().toLowerCase();
            ControllerManager ctrlm = new ControllerManager();
            System.out.println("methode : "+method);
            // Verification de la methode
            String routeURI;
            switch (method){
                case "GET":
                case "HEAD":
                    if (!(routeURI = ctrlm.getRegisterRoutesURI(URI,method)).equals("")){
                        RouteController RC = ctrlm.getRouteController(routeURI);
                        String render = RC.render(URI,routeURI,"");

                        out.println(headerBuilder(200, "OK Je te jure sa passe xD", null, render.length()));
                        out.println(); // Line vide entre le head et le contenu TR2S IMPORTANT
                        out.flush();

                        dataOut.write(render.getBytes(), 0, render.length());
                        dataOut.flush();

                        if (verbose) {
                            System.out.println("La route " + routeURI + " à bien était rendu");
                        }

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
                    break;
                case "POST":
                    if (!(routeURI = ctrlm.getRegisterRoutesURI(URI,method)).equals("")){
                        StringBuilder payload = new StringBuilder();
                        while(in.ready()){
                            payload.append((char) in.read());
                        }
                        RouteController RC = ctrlm.getRouteController(routeURI);
                        String render = RC.render(
                                URI,
                                routeURI,
                                payload.toString()
                                        .split("\r\n")[
                                            payload.toString()
                                            .split("\r\n")
                                            .length-1]);

                        if (render.startsWith("301")){
                            out.println("HTTP/1.1 301 Moved Permanently\n" +
                                    "Location: "+render.replace("301 url:","").replace("\n",""));
                            out.println();
                            out.flush();
                            byte[] t = new byte[0];
                            dataOut.write(t, 0, render.length());
                            dataOut.flush();
                        }else {
                            out.println(headerBuilder(200, "OK Je te jure sa passe xD", null, render.length()));
                            out.println(); // Line vide entre le head et le contenu TR2S IMPORTANT
                            out.flush();
                            dataOut.write(render.getBytes(), 0, render.length());
                            dataOut.flush();
                        }

                        if (verbose) {
                            System.out.println("La route " + routeURI + " à bien était rendu");
                        }
                    }else{
                        fileNotFound(out, dataOut, URI);
                    }
                    break;
                default:
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
        /**
         * Application
         */
        if (fileRequested.endsWith(".pdf")){
            return "application/pdf";
        }
        else if (fileRequested.endsWith(".json")){
            return "application/json";
        }
        else if (fileRequested.endsWith(".zip")){
            return "application/zip";
        }
        /**
         * Text
         */
        else if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html")){
            return "text/html";
        }
        else if (fileRequested.endsWith(".xml")){
            return "text/xml";
        }
        else if (fileRequested.endsWith(".css")){
            return "text/css";
        }else if (fileRequested.endsWith(".csv")){
            return "text/csv";
        }
        /**
         * Audio
         */
        else if (fileRequested.endsWith(".mp3")){
            return "audio/mpeg";
        }else if (fileRequested.endsWith(".wma")){
            return "audio/x-ms-wma";
        }else if (fileRequested.endsWith(".wav")){
            return "audio/x-wav";
        }
        /**
         * Video
         */
        else if (fileRequested.endsWith(".mpeg")){
            return "video/mpeg";
        }else if (fileRequested.endsWith(".mp4")) {
            return "video/mp4";
        }
        else if (fileRequested.endsWith(".fly")) {
            return "video/fly";
        }
        else if (fileRequested.endsWith(".webm")) {
            return "video/webm";
        }
        /**
         * Image
         */
        else if (fileRequested.endsWith(".gif")) {
            return "image/gif";
        }else if (fileRequested.endsWith(".jpeg")||fileRequested.endsWith(".jpg")) {
            return "image/jpeg";
        }else if (fileRequested.endsWith(".png")) {
            return "image/png";
        }else if (fileRequested.endsWith(".tiff")||fileRequested.endsWith(".tif")) {
            return "image/tiff";
        }else if (fileRequested.endsWith(".ico")) {
            return "image/vnd.microsoft.icon";
        }else if (fileRequested.endsWith(".icns")) {
            return "image/x-icon";
        }else if (fileRequested.endsWith(".svg")||fileRequested.endsWith(".svgz")) {
            return "image/svg+xml";
        }
        /**
         * default
         */
        else {
            return "text/plain";
        }
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
        contentMimeType = contentMimeType == null ? "text/html" : contentMimeType;
        return "HTTP/1.1 "+statusCode+" "+statusMessage;
    }

}