package main.parser;

import java.io.*;
import java.util.*;

/**
 * author: Ruslan Sokolov
 * date: 4/1/14
 */
public class FirstFollow {
    private static final Character EPSILON = 'ε';
    private static final Character START = 'E';
    private static final char END = '$';

    private InputStream in;
    private Map<Character, List<String>> rulesMap = new HashMap<>();

    public FirstFollow(InputStream in) {
        this.in = in;
        fillMap();
    }

    private void fillMap() {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String currentLine;
            while((currentLine = br.readLine()) != null) {
                String[] array = currentLine.split("->");
                if (!rulesMap.containsKey(array[0].charAt(0))) {
                    rulesMap.put(array[0].charAt(0), new ArrayList<String>());
                }
                String tmp = array[1];
                if (!tmp.contains("ε")) {
                    tmp += EPSILON;
                }
                rulesMap.get(array[0].charAt(0)).add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Character, Set<Character>> getFIRST() {
        Map<Character, Set<Character>> result = new HashMap<>();
        for (Character ch : rulesMap.keySet()) {
            result.put(ch, new HashSet<Character>());
        }
        boolean changed = true;
        while(changed) {
            changed = false;
            for (Character ch : rulesMap.keySet()) {
                int prevSize = result.get(ch).size();
                for (String st : rulesMap.get(ch)) {
                    char first = st.charAt(0);
                    if (!rulesMap.containsKey(first)) {
                        result.get(ch).add(first);
                        //result.put(first, new HashSet<>(Arrays.asList(first)));
                    } else {
                        result.get(ch).addAll(result.get(first));
                    }
                }
                if (prevSize != result.get(ch).size()) {
                    changed = true;
                }
            }
        }
        return result;
    }

    public Map<Character, Set<Character>> getFOLLOW() {
        Map<Character, Set<Character>> first = getFIRST();
        Map<Character, Set<Character>> result = new HashMap<>();
        for (Character ch : rulesMap.keySet()) {
            result.put(ch, new HashSet<Character>());
            if (ch == START) {
                result.get(ch).add(END);
            }
        }
        boolean changed = true;
        while(changed) {
            changed = false;
            for (Character ch : rulesMap.keySet()) {
                int prevSize = getMapSize(result);
                for (String st : rulesMap.get(ch)) {
                    for (int i = 0; i < st.length() - 1; i++) {
                        char cur = st.charAt(i);
                        char next = st.charAt(i + 1);
                        if (result.containsKey(st.charAt(i))) {
                            if (!result.containsKey(next)) {
                                if (next != EPSILON) {
                                    result.get(cur).add(next);
                                } else {
                                    result.get(cur).addAll(result.get(ch));
                                }
                            } else {
                                result.get(cur).addAll(first.get(next));
                                result.get(cur).remove(EPSILON);
                                if (first.get(next).contains(EPSILON)) {
                                    result.get(cur).addAll(result.get(ch));
                                }
                            }
                        }
                    }
                }
                if (prevSize != getMapSize(result)) {
                    changed = true;
                }
            }
        }
        return result;
    }

    public String toString(Map<Character, Set<Character>> map) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : map.keySet()) {
            sb.append(ch).append(" : [");
            for (Character c : map.get(ch)) {
                sb.append(c).append(" ,");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    private<T, K> int getMapSize(Map<T, Set<K>> map) {
        int sz = 0;
        for (Set<K> k : map.values()) {
            sz += k.size();
        }
        return sz;
    }
}

