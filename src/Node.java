public class Node {

    double x, y;
    int id;

    public Node(double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void printNode() {
        System.out.println("ID: " + id + " wspolrzedne ("+x+","+y+")");
    }
}
