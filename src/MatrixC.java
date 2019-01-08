public class MatrixC {

    //c*ro* {N} * {N}^T

    Jacobian jacobian;
    double c = 700;
    double ro = 7800;

    double[][][] NNT = new double[4][4][4];
    double[][] matrixC = new double[4][4];

    public MatrixC(Jacobian jacobian) {
        this.jacobian = jacobian;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++){

                    NNT[i][j][k] = jacobian.N[i][j] * jacobian.N[k][i] * jacobian.detJ[i] * c * ro;
                    //System.out.print(NNT[i][j][k]+" ");
                }
                //System.out.println();
            }
            //System.out.println("\n");
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++) {
                    matrixC[i][j] += NNT[k][i][j];
                }
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                System.out.print(matrixC[i][j]+"  ");
            }
            System.out.println();
        }
    }
}
