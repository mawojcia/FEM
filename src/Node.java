public class Node {

    double x, y;
    double t; //temperatura
    int id;

    public Node(double x, double y, double t, int id) {
        this.x = x;
        this.y = y;
        this.t = t;
        this.id = id;
    }

    public void printNode() {
        System.out.println("ID: " + id + " wspolrzedne ("+x+","+y+"), temperatura: "+t);
    }
}
