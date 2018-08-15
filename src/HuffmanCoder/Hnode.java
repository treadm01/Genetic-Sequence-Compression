package HuffmanCoder;

public class Hnode implements Comparable{
    String symbol;
    Integer frequency;
    Hnode left;
    Hnode right;
    Hnode head;
    String binary;

    public Hnode(String symbol, Integer frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return this.symbol + " = " + this.frequency + " B: " + binary;
    }

    public void addNodes(Hnode left, Hnode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Object o) {
        Hnode node = (Hnode) o;
        if (node.frequency < this.frequency) {
            return -1;
        }
        else if (node.frequency > this.frequency) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
