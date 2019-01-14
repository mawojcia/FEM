import java.io.FileNotFoundException;

import static java.lang.StrictMath.sqrt;

public class MatrixH_BC {

    Jacobian jacobian;

    double[] borderLength = new double[4];
    double[] detJ = new double[4];
    double[] ksi = new double[2];
    double[] eta = new double[2];
    double[][] N = new double[2][4];

    double[][] pow1 = new double[4][4];
    double[][] pow2 = new double[4][4];
    double[][] pow3 = new double[4][4];
    double[][] pow4 = new double[4][4];

    double[][] matrixHBC = new double[4][4];

//    alfa*{N}{N}T

    public MatrixH_BC(Jacobian jacobian, int[] edge) throws FileNotFoundException {
        this.jacobian = jacobian;
        GetFromFile data = new GetFromFile();
        double alfa = data.alfa;

        //DLUGOSCI BOKOW
        for(int i = 0; i < 4; i++) {
            borderLength[i] = sqrt((jacobian.x[(i+1)%4] - jacobian.x[i])*(jacobian.x[(i+1)%4] - jacobian.x[i]) +
                    (jacobian.y[(i+1)%4] - jacobian.y[i])*(jacobian.y[(i+1)%4] - jacobian.y[i]));
        }

        for(int i = 0; i < 4; i++){
            detJ[i] = borderLength[i]/2;
        }


        //##############POW 1####################
        ksi[0] = -1/sqrt(3);
        ksi[1] = 1/sqrt(3);
        eta[0] = eta[1] = -1;

        for(int i = 0; i < 2; i++) {
            N[i][0] = 0.25 * (1-ksi[i])*(1-eta[i]);
            N[i][1] = 0.25 * (1+ksi[i])*(1-eta[i]);
            N[i][2] = 0.25 * (1+ksi[i])*(1+eta[i]);
            N[i][3] = 0.25 * (1-ksi[i])*(1+eta[i]);
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                pow1[i][j] = ((N[0][i]*N[0][j])+(N[1][i]*N[1][j]))*detJ[0]*alfa;
            }
        }


        //##############POW 2####################
        ksi[0] = ksi[1] = 1;
        eta[0] = -1/sqrt(3);
        eta[1] = 1/sqrt(3);

        for(int i = 0; i < 2; i++) {
            N[i][0] = 0.25 * (1-ksi[i])*(1-eta[i]);
            N[i][1] = 0.25 * (1+ksi[i])*(1-eta[i]);
            N[i][2] = 0.25 * (1+ksi[i])*(1+eta[i]);
            N[i][3] = 0.25 * (1-ksi[i])*(1+eta[i]);
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                pow2[i][j] = ((N[0][i]*N[0][j])+(N[1][i]*N[1][j]))*detJ[1]*alfa;
            }
        }


        //##############POW 3####################
        ksi[0] = 1/sqrt(3);
        ksi[1] = -1/sqrt(3);
        eta[0] = eta[1] = 1;

        for(int i = 0; i < 2; i++) {
            N[i][0] = 0.25 * (1-ksi[i])*(1-eta[i]);
            N[i][1] = 0.25 * (1+ksi[i])*(1-eta[i]);
            N[i][2] = 0.25 * (1+ksi[i])*(1+eta[i]);
            N[i][3] = 0.25 * (1-ksi[i])*(1+eta[i]);
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                pow3[i][j] = ((N[0][i]*N[0][j])+(N[1][i]*N[1][j]))*detJ[2]*alfa;
            }
        }


        //##############POW 4####################
        ksi[0] = ksi[1] = -1;
        eta[0] = 1/sqrt(3);
        eta[1] = -1/sqrt(3);

        for(int i = 0; i < 2; i++) {
            N[i][0] = 0.25 * (1-ksi[i])*(1-eta[i]);
            N[i][1] = 0.25 * (1+ksi[i])*(1-eta[i]);
            N[i][2] = 0.25 * (1+ksi[i])*(1+eta[i]);
            N[i][3] = 0.25 * (1-ksi[i])*(1+eta[i]);
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                pow4[i][j] = ((N[0][i]*N[0][j])+(N[1][i]*N[1][j]))*detJ[3]*alfa;
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                matrixHBC[i][j] = pow1[i][j] * edge[0] + pow2[i][j] * edge[1] + pow3[i][j] * edge[2] + pow4[i][j] * edge[3];
                System.out.print(matrixHBC[i][j]+ " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }
}
