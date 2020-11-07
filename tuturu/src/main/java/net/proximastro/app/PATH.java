package net.proximastro.app;

import java.io.File;

public class PATH
{
    public static final String routePATH = getLocalPath()+"/routes/routes.xml";
    public static final String publicPATH =  getLocalPath()+"/public";
    public static final String dataPATH = getLocalPath()+"/data/student_data.xml";
    public static final String viewPATH = getLocalPath()+"/views/";

    public static String getLocalPath(){
        try {
            System.out.println(new File(PATH.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath());
            return new File(PATH.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
        }catch (Exception e){
            return "";
        }
    }
}
