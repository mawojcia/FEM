public class MatrixH {

    Jacobian jacobian;

    double[][][] matrixX = new double[4][4][4];
    double[][][] matrixY = new double[4][4][4];

    double[][] matrixH = new double[4][4];

    double[][][] sum = new double[4][4][4];

    double K = 30;

    public MatrixH(Jacobian jacobian) {
        this.jacobian = jacobian;

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    matrixX[i][j][k] = jacobian.dNdX[i][j] * jacobian.dNdX[i][k];
                    matrixY[i][j][k] = jacobian.dNdY[i][j] * jacobian.dNdY[i][k];

                    System.out.print(matrixX[i][j][k]+" ");

                    sum[i][j][k] = (matrixX[i][j][k] + matrixY[i][j][k])*K*jacobian.detJ[i];
                }
                System.out.println();
            }
            System.out.println("\n");
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

        for(int i = 0; i < 4; i++) {

            for(int j = 0; j < 4; j++) {

                System.out.print(matrixH[i][j]+" ");
            }
            System.out.println();
        }


    }
}
