import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Line {
    private static Map<String, Object> map = new TreeMap<>();
    private static Map<String, List<String>> listMap = new TreeMap<>();

    public void setListMap(String line, List<String> stations) {
        listMap.put(line, stations);
    }

   public void setMap() {
        map.put("stations", listMap);
    }

    public static Map<String, Object> getMap() {
        return map;
    }
}










