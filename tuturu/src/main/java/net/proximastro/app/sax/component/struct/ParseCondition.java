package net.proximastro.app.sax.component.struct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ParseCondition {
    private static final List<String> arrayCondition = Arrays.asList(">", ">=", "==", "!=", "<", "<=");

    public static boolean checkCondition(String str, HashMap<String, Object> parms){
        final List<String> arrayCondition = Arrays.asList(">", ">=", "==", "!=", "<", "<=");
        String[] strSplit = str.split(" ");
        int nbSpace = strSplit.length;

        int indexStrSplit = 0;
        if(nbSpace == 3 || (nbSpace-3)%4 == 0){

            boolean conditionEstValid = false;
            boolean actualIsValid = true;


            while(indexStrSplit < strSplit.length && !conditionEstValid) {
                int compteur = 0;
                Object[] condition = new Object[3];
                while (compteur < 3) {
                    String element = strSplit[indexStrSplit];
                    Object elementToInsert = null;

                    if (compteur != 1 && element.startsWith("${") && element.endsWith("}")) {
                        element = element.substring(2, element.length() - 1);
                        if(parms.containsKey(element)) {
                            elementToInsert = parms.get(element);
                        }else{
                            return false;
                        }
                    } else if (compteur != 1 && (element.startsWith("'") && element.endsWith("'") || element.startsWith("\"") && element.endsWith("\""))) {
                        elementToInsert = element.substring(1, element.length() - 1);
                    } else if (compteur != 1 && ("true".equals(element) || "false".equals(element))) {
                        elementToInsert = Boolean.valueOf(element);
                    } else if (compteur != 1) {
                        element = element.replace(",", ".");
                        elementToInsert = Float.valueOf(element);
                    } else if (arrayCondition.contains(element)) {
                        elementToInsert = element;
                    }

                    condition[compteur] = elementToInsert;
                    indexStrSplit++;
                    compteur++;
                }
                Object valeurA = condition[0];
                Object valeurB = condition[2];

                if (valeurA!=null && valeurB!=null) {
                    //vérification des 2 types
                    if (valeurA.getClass().getName().equals(valeurB.getClass().getName()) || valeurA instanceof Float && valeurB instanceof Integer || valeurA instanceof Integer && valeurB instanceof Float) {
                        if (valeurA instanceof String) {
                            if ("==".equals(condition[1])) {
                                if (!valeurA.equals(valeurB)) {
                                    actualIsValid = false;
                                }
                            } else if ("!=".equals(condition[1])) {
                                if (valeurA.equals(valeurB)) {
                                    actualIsValid = false;
                                }
                            }
                        } else if (valeurA instanceof Float || valeurA instanceof Integer) {
                            Float v1 = Float.valueOf(valeurA.toString());
                            Float v2 = Float.valueOf(valeurB.toString());


                            switch ((String) condition[1]) {
                                case ">=":
                                    if (v1 < v2) {
                                        actualIsValid = false;
                                    }
                                    break;
                                case ">":
                                    if (v1 <= v2) {
                                        actualIsValid = false;
                                    }
                                    break;
                                case "==":
                                    if (!v1.equals(v2)) {
                                        actualIsValid = false;
                                    }
                                    break;
                                case "!=":
                                    if (v1.equals(v2)) {
                                        actualIsValid = false;
                                    }
                                    break;
                                case "<":
                                    if (v1 >= v2) {
                                        actualIsValid = false;
                                    }
                                    break;
                                case "<=":
                                    if (v1 > v2) {
                                        actualIsValid = false;
                                    }
                                    break;
                            }
                        } else if (valeurA instanceof Boolean) {
                            if ("==".equals(condition[1])) {
                                if (!valeurA.equals(valeurB)) {
                                    actualIsValid = false;
                                }
                            } else if ("!=".equals(condition[1])) {
                                if (valeurA.equals(valeurB)) {
                                    actualIsValid = false;
                                }
                            }
                        } else {
                            if ("==".equals(condition[1])) {
                                if (!valeurA.equals(valeurB)) {
                                    actualIsValid = false;
                                }
                            } else if ("!=".equals(condition[1])) {
                                if (valeurA.equals(valeurB)) {
                                    actualIsValid = false;
                                }
                            }
                        }

                    }
                    if (valeurA instanceof String && valeurB instanceof String) {
                        if ("==".equals(condition[1])) {
                        } else if ("!=".equals(condition[1])) {
                            if (actualIsValid) {
                                actualIsValid = !((String) valeurA).equalsIgnoreCase(((String) valeurB));
                            }
                        }
                    }

                    if (indexStrSplit < strSplit.length) {
                        switch (strSplit[indexStrSplit]) {
                            case "or":
                                conditionEstValid = actualIsValid;
                                actualIsValid = true;
                                break;
                            case "and":

                                break;
                        }
                    } else {
                        conditionEstValid = actualIsValid;
                    }
                    indexStrSplit++;
                }
            }
            return conditionEstValid;
        }else if(nbSpace == 1){
            if(str.startsWith("${") && str.endsWith("}")){
                str = str.substring(2, str.length()-1);
                if(parms.containsKey(str)){
                    if(parms.get(str) instanceof Boolean){
                        return (Boolean) parms.get(str);
                    }
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}
