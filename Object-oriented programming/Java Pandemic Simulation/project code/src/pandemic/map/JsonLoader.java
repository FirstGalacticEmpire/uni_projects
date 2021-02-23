package pandemic.map;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Loads json file.
 */
public class JsonLoader {
    private ArrayList<Long> mapSchema;
    private final JSONParser parser = new JSONParser();

    /**
     * JsonLoader constructor.
     */
    public JsonLoader() {
    }


    /**
     * Loads map structure from json file.
     *
     * @param pathToMapFile path to file that contains map
     * @return mapList
     */
    public ArrayList<Long> loadMap(String pathToMapFile) {
        try {
            Object obj = parser.parse(new FileReader(pathToMapFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray myArray = (JSONArray) jsonObject.get("layers");
            JSONObject jsonObject1 = (JSONObject) parser.parse(myArray.get(0).toString());
            JSONArray myArray2 = (JSONArray) jsonObject1.get("data");

            this.mapSchema = new ArrayList<>();
            if (myArray2 != null) {
                for (Object o : myArray2) {
                    this.mapSchema.add((Long) o);
                }
            }
        } catch (Exception e) {
            System.out.println("Map file not found.");
            e.printStackTrace();
        }
        return this.mapSchema;

    }

    public ArrayList<Long> getMapSchema() {
        return mapSchema;
    }
}
