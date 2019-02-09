package shortestPathProblem;

public class Arc {

    int cost;           //costo associato all'arco
    Node tail;          //nodo coda
    Node head;          //nodo testa

    public Arc(int cost, Node tail, Node head) {
        this.cost = cost;                           
        this.tail = tail;                           
        this.head = head;       
    }

    @Override
    public String toString() {
        return "Arc{" + "cost=" + cost + ", tail=" + tail + ", head=" + head + '}';
    }

}
