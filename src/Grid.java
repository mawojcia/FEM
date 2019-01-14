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
    double[][] globalVectorP = new double[nH*nL][nH*nL];
    double[] vectorP = new double[nH*nL];

    public Grid() throws FileNotFoundException{
        nodesAmount = nH*nL;
        elementsAmount = (nH-1)*(nL-1);

        nodes = new Node[nodesAmount];
        elements = new Element[elementsAmount];


//STWORZENIE TABLICY NODÓW, NA PODSTAWIE KROKÓW WZDŁUŻ I WZWYŻ
        double lStep = (l / (nL - 1));
        double hStep = (h / (nH - 1));
        int index = 0;

        for(double i = 0; i < nL; i++) {
            for(double j = 0; j <nH; j++) {
                nodes[index] = new Node(i*lStep, j*hStep, index);
                index++;
            }
        }

//STWORZENIE TABLICY ELEMENTÓW
        index = 0;

        for(int i = 0; i < nL - 1; i++) {
            for(int j = 0; j < nH -1; j++) {

                elements[index] = new Element(nodes[i*nH+j], nodes[(i+1)*nH+j], nodes[(i+1)*nH+j+1], nodes[i*nH+j+1], index, data.conductivity);
                index++;
            }
        }

//SPRAWDZAMY CZY ŚCIANY ELEMENTÓW SĄ KRAWĘDZIAMI CAŁEJ SIATKI

        for(int i = 0; i < elementsAmount; i++) {
            for(int j = 0; j < 4; j++) {

                //Dół
                if(elements[i].nodes[j].y == 0) elements[i].edge[0] = 1;

                //Prawo
                if(elements[i].nodes[j].x == l) elements[i].edge[1] = 1;

                //Góra
                if(elements[i].nodes[j].y == h) elements[i].edge[2] = 1;

                //Lewo
                if(elements[i].nodes[j].x == 0) elements[i].edge[3] = 1;
            }
        }

//jacobian, macierze

        for(int i = 0; i < elementsAmount; i++) {

            elements[i].jacobian = new Jacobian(elements[i].nodes);
            elements[i].matrixH = new MatrixH(elements[i].jacobian);
            elements[i].matrixC = new MatrixC(elements[i].jacobian);
            elements[i].matrixH_bc = new MatrixH_BC(elements[i].jacobian, elements[i].edge);
            elements[i].vectorP = new VectorP(elements[i].jacobian, elements[i].edge);
        }

//globalne macierze

        for(int i = 0; i < elementsAmount; i++) {
            for(int j = 0; j < 4; j++) {
                for(int k = 0; k < 4; k++) {
                    globalMatrixH[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].matrixH.matrixH[j][k];
                    globalMatrixH_bc[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].matrixH_bc.matrixHBC[j][k];
                    globalMatrixC[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].matrixC.C[j][k];
                    globalVectorP[elements[i].nodes[j].id][elements[i].nodes[k].id] += elements[i].vectorP.vectorP[j][k];
                }
            }
        }

        double[] temperatures = new double[nodesAmount];

        for(int i = 0; i < nodesAmount; i++) {
            temperatures[i] = data.initial_temperature;
        }

        double[][] HC = new double[nodesAmount][nodesAmount];
        double step_time = data.simulation_step_time;

//        int flaga = 0;
        double[][] globalVectorPtmp = new double[nodesAmount][nodesAmount];

        for(int z = 0; z < data.simulation_time/data.simulation_step_time; z++) {

            System.out.print("\n");

            for (int i = 0; i < nodesAmount; i++) {
                vectorP[i] = 0;
                for (int j = 0; j < nodesAmount; j++) {
                    HC[i][j] = globalMatrixH[i][j]+(globalMatrixC[i][j]/ data.simulation_step_time) + globalMatrixH_bc[i][j];
                    globalVectorPtmp[i][j] = globalVectorP[i][j] + (globalMatrixC[i][j] / data.simulation_step_time) * temperatures[j];
                    vectorP[i] += globalVectorPtmp[i][j];
                }
                //System.out.print(vectorP[i]+" * ");
            }
            //System.out.print("\n");

//            if(flaga < 2) {
//                for (int i = 0; i < nodesAmount; i++) {
//                    for (int j = 0; j < nodesAmount; j++) {
//                        System.out.print(HC[i][j] + " ");
//                    }
//                    System.out.println();
//                }
//                flaga ++;
//            }
//            System.out.print("\n\n");

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

            //zerowanie wspolczynnikow
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

//                System.out.print(vectorP[i] + " # " );
            }
//            System.out.print("\n");


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
