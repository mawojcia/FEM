import java.io.FileNotFoundException;

public class Grid {

    GetFromFile data = new GetFromFile();
    int nH = data.nH;
    int nL = data.nL;
    double h = data.H;
    double l = data.L;
    int nodesAmount;
    int elementsAmount;
    Node[] nodes;
    Element[] elements;

    double[][] globalMatrixH = new double[nH*nL][nH*nL];
    double[][] globalMatrixC = new double[nH*nL][nH*nL];
    double[][] globalMatrixH_bc = new double[nH*nL][nH*nL];
    double[][] globalVectorPtmp = new double[nH*nL][nH*nL];
    double[] vectorP = new double[nH*nL];

    public Grid() throws FileNotFoundException{
        nodesAmount = nH*nL;
        elementsAmount = (nH-1)*(nL-1);

        nodes = new Node[nodesAmount];
        elements = new Element[elementsAmount];

        double iInc = (l / (nL - 1));
        double jInc = (h / (nH - 1));
        int index = 0;

//Stworzenie tablicy nodow, ustalenie wspolrzednych na podstawie wysokosci, dlugosci oraz "skoku"
        for(double i = 0; i < nL*iInc; i += iInc) {
            for(double j = 0; j <nH*jInc; j += jInc) {
                nodes[index] = new Node(i, j, index);
                index++;
            }
        }

//Stworzenie tablicy elementow
        index = 0;

        for(int i = 0; i < nL - 1; i++) {
            for(int j = 0; j < nH -1; j++) {

                elements[index] = new Element(nodes[i*nH+j], nodes[(i+1)*nH+j], nodes[(i+1)*nH+j+1], nodes[i*nH+j+1], index, data.conductivity);
                index++;
            }
        }

//Sprawdzamy krawedzie elementu

        for(int i = 0; i < elementsAmount; i++) {
            for(int j = 0; j < 4; j++) {

                //Dół
                if(elements[i].nodes[j].y == 0) elements[i].edge[0] = 1;
                //System.out.print(" Edge: "+ elements[i].edge[0]);

                //Prawo
                if(elements[i].nodes[j].x == l || (elements[i].nodes[j].y) > l - 0.000000000001) elements[i].edge[1] = 1;

                //Góra
                if(elements[i].nodes[j].y == h || (elements[i].nodes[j].y) > h - 0.000000000001) elements[i].edge[2] = 1;

                //Lewo
                if(elements[i].nodes[j].x == 0) elements[i].edge[3] = 1;
            }
        }

//jacobian

        for(int i = 0; i < elementsAmount; i++) {

            elements[i].jacobian = new Jacobian(elements[i].getNodes());
            elements[i].matrixH = new MatrixH(elements[i].jacobian);
            elements[i].matrixC = new MatrixC(elements[i].jacobian);
            elements[i].matrixH_bc = new MatrixH_BC(elements[i].jacobian, elements[i].edge);
            elements[i].vectorP = new VectorP(elements[i].jacobian, elements[i].edge);
        }

//        for(int i = 0; i < elementsAmount; i++) {
//            for(int j =0;j<4; j++){
//                for(int k = 0; k < 4; k++) System.out.print(elements[i].matrixC.C[j][k] + " ");
//                System.out.print("\n");
//            }
//            System.out.print("\n\n");
//        }

//globalne macierze

        for(int i = 0; i < elementsAmount; i++) {
            for(int j = 0; j < 4; j++) {
                for(int k = 0; k < 4; k++) {
                    globalMatrixH[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].matrixH.matrixH[j][k];
                    globalMatrixH_bc[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].matrixH_bc.matrixHBC[j][k];
                    globalMatrixC[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].matrixC.C[j][k];
                    globalVectorPtmp[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].vectorP.vectorP[j][k];
                }
            }
        }

        for(int i = 0; i < nH*nL; i++) {
            for(int j = 0; j < nH*nL; j++) {
                System.out.print(globalMatrixC[i][j]+" ");
            }
            System.out.println();
        }

        double[] temperatures = new double[nodesAmount];

        for(int i = 0; i < nodesAmount; i++) {
            temperatures[i] = data.initial_temperature;
        }

        double[][] HC = new double[nodesAmount][nodesAmount];
        double step_time = data.simulation_step_time;

        for(int z = 0; z < data.simulation_time/data.simulation_step_time; z++) {

            for (int i = 0; i < nodesAmount; i++) {
                vectorP[i] = 0;
                for (int j = 0; j < nodesAmount; j++) {
                    HC[i][j] = globalMatrixH[i][j]+(globalMatrixC[i][j]/ data.simulation_step_time) +globalMatrixH_bc[i][j];
                    globalVectorPtmp[i][j] = globalVectorPtmp[i][j] + (globalMatrixC[i][j] / data.simulation_step_time) * temperatures[j];
                    vectorP[i] += globalVectorPtmp[i][j];
                }
                //System.out.print(vectorP[i]+" * ");
            }

            double m, s;
            double[][] globalMatrixHtmp = new double[nodesAmount][nodesAmount+1];

            //Eliminacja gaussa
            for (int i = 0; i < nodesAmount; i++) {
                for (int j = 0; j < nodesAmount+1; j++) {
                    if (j == nodesAmount) {
                        globalMatrixHtmp[i][j] = vectorP[i];//dopisujemy wektor P
                    }
                    else globalMatrixHtmp[i][j] = HC[i][j];//macierz wspolczynnikow
                }
            }

            //Eliminacja wspó³czynników
            for (int i = 0; i < nodesAmount - 1; i++)
            {
                for (int j = i + 1; j < nodesAmount; j++)
                {
                    m = -globalMatrixHtmp[j][i] / globalMatrixHtmp[i][i];	//m mnoznik przez który mnozone sa elementy macierzy
                    for (int k = i + 1; k <= nodesAmount; k++)
                    {
                        globalMatrixHtmp[j][k] += m * globalMatrixHtmp[i][k];
                    }
                }
            }

            //Wyliczanie niewiadomych
            for (int i = nodesAmount - 1; i >= 0; i--)
            {
                s = globalMatrixHtmp[i][nodesAmount];								//s zlicza sume iloczynow
                for (int j = nodesAmount - 1; j >= i + 1; j--)
                {
                    s -= globalMatrixHtmp[i][j] * vectorP[j];
                }
                vectorP[i] = s / globalMatrixHtmp[i][i];
                temperatures[i]	= vectorP[i];
            }

            double minTemp = temperatures[0];
            double maxTemp = temperatures[0];
            for (int i = 1; i < nodesAmount; i++)
            {
                if (temperatures[i] > maxTemp)
                {
                    maxTemp = temperatures[i];
                }
                if (temperatures[i] < minTemp)
                {
                    minTemp = temperatures[i];
                }
            }

            System.out.println("time: "+ step_time + " seconds Min: "+minTemp+", Max: "+maxTemp);
            step_time += data.simulation_step_time;
        }
    }

    public void printGrid() {

        for (Element ele:elements) {
            ele.printElement();
        }
    }
}
