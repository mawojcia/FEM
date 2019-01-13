import org.json.simple.*;
import org.json.simple.parser.JSONParser;


import java.io.FileReader;
import java.util.concurrent.ExecutionException;

public class ReadJson {

    public static double H;
    public static double L;
    public static double NH;
    public static double NL;
    public static double INITIAL_TEMPERATURE;
    public static double AMBIENT_TEMPERATURE;
    public static double ALFA;
    public static double CONDUCTIVITY;
    public static double DENSITY;
    public static double SPECIFIC_HEAT;
    public static double SIMULATION_TIME;
    public static double SIMULATION_STEP_TIME;

    public ReadJson() {

    }

    public static void jsonData() {

        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader("data.json"));
            JSONObject jsonObject = (JSONObject) obj;

            H = Double.parseDouble((String) jsonObject.get("H"));
            L = Double.parseDouble((String) jsonObject.get("L"));
            NH = Double.parseDouble((String) jsonObject.get("nH"));
            NL = Double.parseDouble((String) jsonObject.get("nL"));
            INITIAL_TEMPERATURE = Double.parseDouble((String) jsonObject.get("initial_temperature"));
            AMBIENT_TEMPERATURE = Double.parseDouble((String) jsonObject.get("ambient_temperature"));
            ALFA = Double.parseDouble((String) jsonObject.get("alfa"));
            CONDUCTIVITY = Double.parseDouble((String) jsonObject.get("conductivity"));
            DENSITY = Double.parseDouble((String) jsonObject.get("density"));
            SPECIFIC_HEAT = Double.parseDouble((String) jsonObject.get("specific_heat"));
            SIMULATION_TIME = Double.parseDouble((String) jsonObject.get("simulation_time"));
            SIMULATION_STEP_TIME = Double.parseDouble((String) jsonObject.get("simulation_step_time"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
