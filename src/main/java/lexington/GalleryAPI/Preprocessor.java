package lexington.GalleryAPI;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Preprocessor {




    public static HashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        Collections.reverse(mapValues);
        Collections.reverse(mapKeys);
        HashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        for (Integer val : mapValues) {
            Iterator<String> keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                int comp1 = passedMap.get(key);
                int comp2 = val;

                if (comp1 == comp2) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    static Map<String, Integer> sortHashMapByKeys(HashMap<String, Integer> passedMap) {
        Map<String, Integer> map = new TreeMap<>(passedMap);
        for(Map.Entry<String,Integer> entry : passedMap.entrySet()){
            map.put(entry.getKey(),entry.getValue());
        }
/*        HashMap<String, Integer> sortedMap = new HashMap<>();
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            sortedMap.put(entry.getKey(),entry.getValue());
            System.out.println(entry.getKey()+": "+entry.getValue());
        }*/
        return map;
    }

    static BufferedReader getReader(String fileName) {
        BufferedReader sc = null;
        try {
            sc = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(fileName), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sc;
    }
    public static PrintWriter getPrinter(String fileName) {
        PrintWriter pw= null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName),
                    StandardCharsets.UTF_8), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pw;
    }
}
