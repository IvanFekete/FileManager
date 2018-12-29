import java.util.Set;
import java.util.TreeMap;

public class ResultsSaver {
    private TreeMap<String, String> results;

    public ResultsSaver() {
        results = new TreeMap<>();
    }

    public String getResult(String key) {
        String result = results.get(key);
        return result == null ? "0" : result;
    }

    public String setResult(String key, String val)  {
        return results.putIfAbsent(key, val);
    }

    public boolean containsKey(String key) {
        return results.containsKey(key);
    }

}
