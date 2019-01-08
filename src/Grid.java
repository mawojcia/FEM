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
    double temp = 20; //***************************PLIK**************************

    //double temp = data.temp;
    //double ro = data.ro;
    //double alfa = data.alfa;

    public Grid() throws FileNotFoundException{

        nodesAmount = nH*nL;
        elementsAmount = (nH-1)*(nL-1);

        nodes = new Node[nodesAmount];
        elements = new Element[elementsAmount];

//Stworzenie tablicy nodow, ustalenie wspolrzednych na podstawie wysokosci, dlugosci oraz "skoku"
        for(int i = 0; i < nL; i++) {
            for(int j = 0; j < nH; j++) {

                int index = i*nH+j;
                nodes[index] = new Node(i*l, j*h, temp, index);
            }
        }

//Stworzenie tablicy elementow
        int index = 0;

        for(int i = 0; i < nL - 1; i++) {
            for(int j = 0; j < nH -1; j++) {

                elements[index] = new Element(nodes[i*nH+j], nodes[(i+1)*nH+j], nodes[(i+1)*nH+j+1], nodes[i*nH+j+1], index);
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
    }

    public void printGrid() {

        for (Element ele:elements) {
            ele.printElement();
        }
    }
}
