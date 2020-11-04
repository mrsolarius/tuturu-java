package net.proximastro.webserveur.sax;

import java.util.ArrayList;
import java.util.HashMap;

public class IfClass {
    private HashMap<String, Boolean> hashMapIf;
    private String keyLastHashMap;
    private ArrayList<String> keys;

    public IfClass(){
        this.hashMapIf = new HashMap<>();
        keys = new ArrayList<>();
        keyLastHashMap = null;
    }

    public void addIfOnHashMap(String path, boolean isRespected){
        keys.add(path);
        this.hashMapIf.put(path, isRespected);
    }

    public boolean getValueOfLastHashMap(){
        if(this.hashMapIf.size() > 0){
            return this.hashMapIf.get(keys.get(keys.size()-1));
        }else{
            return true;
        }
    }

    public void inverseValueOfLastHashMap(){
        String key = keys.get(keys.size()-1);
        this.hashMapIf.replace(key, !hashMapIf.get(key));
    }

    public void removeLastHashMap(){
        String key = keys.get(keys.size()-1);
        this.hashMapIf.remove(key);
        keys.remove(key);
        if(keys.size() > 0){
            keyLastHashMap = keys.get(keys.size()-1);
        }else{
            keyLastHashMap = null;
        }
    }
}
