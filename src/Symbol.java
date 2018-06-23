/**
 * used so rule can hold a lis tof objects that are either
 * nonTerminal or terminal, both of which extend from here
 */
public class Symbol implements SymbolInterface {
    String representation;
    Symbol left, right;

    @Override
    public void setSymbol(String representation) {
        this.representation = representation;
    }

    @Override
    public String getSymbol() {
        return this.representation; //TODO HMMMMMMMMM
    }

    @Override
    public void setLeftSymbol(Symbol leftSymbol) {
        this.left = leftSymbol;
    }

    @Override
    public void setRightSymbol(Symbol rightSymbol) {
        this.right = rightSymbol;
    }

    @Override
    public Symbol getLeftSymbol() {
        return this.left;
    }

    @Override
    public Symbol getRightSymbol() {
        return this.right;
    }

    @Override
    public void join(Symbol nextSymbol) {

    }


}
