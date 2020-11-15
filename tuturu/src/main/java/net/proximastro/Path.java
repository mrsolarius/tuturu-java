package net.proximastro;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Path {
    public static final String getPath(){
        try {
            String str = new File(Path.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            if (str.contains("/")){
                ArrayList<String> matchStr = new ArrayList<String>(Arrays.asList(str.split("/")));
                matchStr.remove(matchStr.size()-1);
                str = "";
                for (String string:matchStr) {
                    str+=string+"/";
                }
            }else {
                ArrayList<String> matchStr = new ArrayList<String>(Arrays.asList(str.split("\\\\")));
                matchStr.remove(matchStr.size()-1);
                str = "";
                for (String string:matchStr) {
                    str+=string+"\\";
                }
            }


            return str;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }
}
