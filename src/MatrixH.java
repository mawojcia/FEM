import java.io.FileNotFoundException;

public class MatrixH {

    Jacobian jacobian;

    double[][][] matrixX = new double[4][4][4];
    double[][][] matrixY = new double[4][4][4];

    double[][] matrixH = new double[4][4];

    double[][][] sum = new double[4][4][4];

//    K*({dN/dX}*{dN/dX}^T + {dN/dY}*{dN/dY}^T)*DETJ

    public MatrixH(Jacobian jacobian) throws FileNotFoundException {
        this.jacobian = jacobian;
        GetFromFile data = new GetFromFile();
        double conductivity = data.conductivity;

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    matrixX[i][j][k] = jacobian.dNdX[i][j] * jacobian.dNdX[i][k];
                    matrixY[i][j][k] = jacobian.dNdY[i][j] * jacobian.dNdY[i][k];

                    sum[i][j][k] = (matrixX[i][j][k] + matrixY[i][j][k])*conductivity*jacobian.detJ[i];
                }
            }
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k=0; k < 4; k++)
                {
                    matrixH[i][j] += sum[k][i][j];
                }
            }
        }
    }
}
