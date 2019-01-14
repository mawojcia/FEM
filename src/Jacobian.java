import java.lang.*;

public class Jacobian {

    Node[] nodes = new Node[4];

    double[] ksi = new double[4];
    double[] eta = new double[4];

    double[] x = new double[4];
    double[] y = new double[4];

    double[][] N = new double[4][4];
    double[][] dNdKsi = new double[4][4];
    double[][] dNdEta = new double[4][4];

    double[] dXdKsi = new double[4];
    double[] dXdEta = new double[4];
    double[] dYdKsi = new double[4];
    double[] dYdEta = new double[4];

    double[] detJ = new double[4];

    double[][] dNdX = new double[4][4];
    double[][] dNdY = new double[4][4];

//    FUNKCJE KSZTAŁTU ZDEFINIOWANE SĄ W LOAKLNYM UKŁADZIE WSPÓŁRZĘDNYCH, DLATEGO POTRZEBUJEMY PRZEJŚĆ Z PUNKTÓW
//    X I Y NA PUNKTY KSI I ETA.

    public Jacobian(Node[] nodes) {
        this.nodes = nodes;

        for(int i = 0; i < 4; i++) {
            x[i] = nodes[i].x;
            y[i] = nodes[i].y;
            System.out.println("("+x[i]+", "+y[i]+")");
        }

//        PUNKTY CAŁKOWANIA
        ksi[0] = -1/(Math.sqrt(3));
        ksi[1] = 1/(Math.sqrt(3));
        ksi[2] = 1/(Math.sqrt(3));
        ksi[3] = -1/(Math.sqrt(3));

        eta[0] = -1/(Math.sqrt(3));
        eta[1] = -1/(Math.sqrt(3));
        eta[2] = 1/(Math.sqrt(3));
        eta[3] = 1/(Math.sqrt(3));


        //N=1/4*(1-ksi)(1-eta)
        //FUNKCJE KSZTAŁTU
        for(int i = 0; i < 4; i++) {
            N[i][0] = 0.25*(1-ksi[i])*(1-eta[i]);
            N[i][1] = 0.25*(1+ksi[i])*(1-eta[i]);
            N[i][2] = 0.25*(1+ksi[i])*(1+eta[i]);
            N[i][3] = 0.25*(1-ksi[i])*(1+eta[i]);
        }


        //POCHODNE FUNKCJI KSZTAŁTU PO KSI I ETA  #DLA KAŻDEGO PUNKTU MAMY 4 POCHODNE FUNKCJI KSZTAŁTU
        for(int i = 0; i < 4; i++) {

            dNdKsi[i][0] = -0.25*(1-eta[i]);
            dNdKsi[i][1] = 0.25*(1-eta[i]);
            dNdKsi[i][2] = 0.25*(1+eta[i]);
            dNdKsi[i][3] = -0.25*(1+eta[i]);

            dNdEta[i][0] = -0.25*(1-ksi[i]);
            dNdEta[i][1] = -0.25*(1+ksi[i]);
            dNdEta[i][2] = 0.25*(1+ksi[i]);
            dNdEta[i][3] = 0.25*(1-ksi[i]);
        }


        //JACOBIAN PRZEKSZTALCENIA
        for(int i = 0; i < 4; i++) {
            dXdKsi[i] = dNdKsi[i][0]*x[0] + dNdKsi[i][1]*x[1] + dNdKsi[i][2]*x[2] + dNdKsi[i][3]*x[3];
            dXdEta[i] = dNdEta[i][0]*x[0] + dNdEta[i][1]*x[1] + dNdEta[i][2]*x[2] + dNdEta[i][3]*x[3];
            dYdKsi[i] = dNdKsi[i][0]*y[0] + dNdKsi[i][1]*y[1] + dNdKsi[i][2]*y[2] + dNdKsi[i][3]*y[3];
            dYdEta[i] = dNdEta[i][0]*y[0] + dNdEta[i][1]*y[1] + dNdEta[i][2]*y[2] + dNdEta[i][3]*y[3];
        }

        //detJ, WYZNACZNIK MACIERZY 2X2
        for(int i = 0; i < 4; i++) {
            detJ[i] = (dYdEta[i]*dXdKsi[i])-(dYdKsi[i]*dXdEta[i]);
        }

        //obliczamy dN/dx, dN/dy (odwrocona macierz A), WYNIKA Z MNOŻENIA MACIERZY
        for(int i = 0; i < 4; i++) {
            dNdX[i][0] = (1/detJ[i]) * (dYdEta[i]*dNdKsi[i][0] - dYdKsi[i]*dNdEta[i][0]);
            dNdX[i][1] = (1/detJ[i]) * (dYdEta[i]*dNdKsi[i][1] - dYdKsi[i]*dNdEta[i][1]);
            dNdX[i][2] = (1/detJ[i]) * (dYdEta[i]*dNdKsi[i][2] - dYdKsi[i]*dNdEta[i][2]);
            dNdX[i][3] = (1/detJ[i]) * (dYdEta[i]*dNdKsi[i][3] - dYdKsi[i]*dNdEta[i][3]);

            dNdY[i][0] = (1/detJ[i]) * (-dXdEta[i]*dNdKsi[i][0] + dXdKsi[i]*dNdEta[i][0]);
            dNdY[i][1] = (1/detJ[i]) * (-dXdEta[i]*dNdKsi[i][1] + dXdKsi[i]*dNdEta[i][1]);
            dNdY[i][2] = (1/detJ[i]) * (-dXdEta[i]*dNdKsi[i][2] + dXdKsi[i]*dNdEta[i][2]);
            dNdY[i][3] = (1/detJ[i]) * (-dXdEta[i]*dNdKsi[i][3] + dXdKsi[i]*dNdEta[i][3]);
        }
    }
}
