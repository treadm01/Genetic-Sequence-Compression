/**
 * used so rule can hold a lis tof objects that are either
 * nonTerminal or terminal, both of which extend from here
 */
public class Symbol implements SymbolInterface {
    String representation;
    Symbol left;
    Symbol right;

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


    public boolean digramEquals(Symbol o) {
        Symbol t = null;
        if (!(o instanceof Terminal) && !(o instanceof NonTerminal)) {
            throw new ClassCastException("Must be terminal or nonterminal. Received " + o.getClass());
        }

        if (o.getLeftSymbol() == null || this.getLeftSymbol() == null) {
            return false;
        }

        return  (o.toString().equals(this.toString()) && o.getLeftSymbol().equals(this.getLeftSymbol()));
    }
}
