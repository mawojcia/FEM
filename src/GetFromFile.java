import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GetFromFile {

    int nH, nL;
    double H, L, initial_temperature, ambient_temperature;
    double alfa, conductivity, density, simulation_time, simulation_step_time;

    public GetFromFile() throws FileNotFoundException {
        File file = new File("dane.txt");
        Scanner in = new Scanner(file);
        String[] line = new String[11];

        for(int i = 0; i < 11; i++) {

            line[i] = in.nextLine();
        }
        H = Double.parseDouble(line[0]);
        L = Double.parseDouble(line[1]);
        nH = Integer.parseInt(line[2]);
        nL = Integer.parseInt(line[3]);
        initial_temperature = Double.parseDouble(line[4]);
        ambient_temperature = Double.parseDouble(line[5]);
        alfa = Double.parseDouble(line[6]);
        conductivity = Double.parseDouble(line[7]);
        density = Double.parseDouble(line[8]);
        simulation_time = Double.parseDouble(line[9]);
        simulation_step_time = Double.parseDouble(line[10]);


    }

}
