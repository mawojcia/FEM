import java.io.FileNotFoundException;

public class Main {


    public static void main(String[] args) throws FileNotFoundException{

        //Grid grid = new Grid();
//        grid.createGrid();
//
//        grid.printGrid();

        Node n1 = new Node(0, 0, 20, 0);
        Node n2 = new Node(10, 0, 20, 1);
        Node n3 = new Node(10, 8, 20, 2);
        Node n4 = new Node(0, 8, 20, 3);

        Element element = new Element(n1,n2,n3,n4, 1);
        element.edge[0] = 1;
        element.edge[1] = 0;
        element.edge[2] = 0;
        element.edge[3] = 0;

        System.out.println("DET J");

        Jacobian jacob = new Jacobian(element.nodes);
        //MatrixH mat = new MatrixH(jacob);
        //MatrixC matC = new MatrixC(jacob);
        MatrixH_BC matrixH_bc = new MatrixH_BC(jacob, element.edge);


        //System.out.println("\n\n" + -1/(Math.sqrt(3)));
    }
}