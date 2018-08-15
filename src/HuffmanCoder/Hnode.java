package HuffmanCoder;

public class Hnode implements Comparable{
    String symbol;
    Integer frequency;

    public Hnode(String symbol, Integer frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return this.symbol + " = " + this.frequency;
    }

    @Override
    public int compareTo(Object o) {
        Hnode node = (Hnode) o;
        if (node.frequency < this.frequency) {
            return 1;
        }
        else if (node.frequency > this.frequency) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
