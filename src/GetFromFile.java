import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GetFromFile {

    int nH, nL;
    double H, L;

    public GetFromFile() throws FileNotFoundException {
        File file = new File("dane.txt");
        Scanner in = new Scanner(file);
        String[] line = new String[4];

        for(int i = 0; i < 4; i++) {

            line[i] = in.nextLine();
        }
        H = Double.parseDouble(line[0]);
        L = Double.parseDouble(line[1]);
        nH = Integer.parseInt(line[2]);
        nL = Integer.parseInt(line[3]);

    }

}
