import java.io.FileNotFoundException;

public class MatrixC {

    Jacobian jacobian;

    double[][][] NNT = new double[4][4][4];
    double[][] matrixC = new double[4][4];

    public MatrixC(Jacobian jacobian) throws FileNotFoundException {
        this.jacobian = jacobian;
        GetFromFile data = new GetFromFile();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++){
//                    c*ro*{N}{N}T
                    NNT[i][k][j] = jacobian.N[j][i] * jacobian.N[i][k] * jacobian.detJ[i] * data.specific_heat * data.density;
                }
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++) {
                    matrixC[i][j] += NNT[k][i][j];
                }
            }
        }
    }
}
