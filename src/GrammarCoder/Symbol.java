package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    Symbol left, right;
    long representation;
    private static final long PRIME = 2265539; // from sequitur
    public Boolean isComplement = false;
    Boolean isEdited = false;
    String edits = "";
    public Symbol complement;
    int index;
    int symbolIndex; // to keep location of actual symbol for edit
    List<Edit> editList = new ArrayList<>(); // todo this shouldn't be needed for each, at symbol level

    public void setIsEdit(String edits) {
        this.edits += edits;
        isEdited = true;
    }

    public Boolean isGuard() {
        return representation == 0;
    }

    @Override
    public String toString() {
        return String.valueOf(representation);
    } //TODO convert to necessary symbol

    @Override
    public int hashCode() {
        long code;
        //from sequitur
        long a = this.representation;
        long b = left.representation; // switched check to look at left symbol rather than right
        code = ((21599 * a) + (20507 * b));
        code = code % PRIME;
        return (int)code;
    }


    @Override
    public boolean equals(Object obj) {
        //TODO add all checks
        Symbol symbol = (Symbol) obj;

        return ((representation == symbol.getRepresentation())
                && (left.representation == (symbol.left.getRepresentation()))
        ); // switched check to look at left symbol rather than right
    }

    public void assignLeft(Symbol left) {
        this.left = left;
    }

    public void assignRight(Symbol right) {
        this.right = right;
    }

    public Symbol getLeft() {
        return left;
    }

    public Symbol getRight() {
        return right;
    }

    public long getRepresentation() {
        return representation;
    }

    // todo this is only used once for finding approx matches, must be a better way
    // the way in which it is called as object method, but still requires the next right symbol is not good
    public Symbol getNextTerminal(Symbol currentSymbol, Boolean isComplement) {
        //todo is guard an ok check, does this really always get the next terminal
        while (!(currentSymbol instanceof Terminal) && !currentSymbol.isGuard()) {
            if (currentSymbol instanceof Terminal) {
                if (isComplement) {
                    currentSymbol = currentSymbol.getLeft();
                }
                else {
                    currentSymbol = currentSymbol.getRight();
                }
            }
            else {
                if (isComplement) {
                    currentSymbol = getNextTerminal(((NonTerminal) currentSymbol).getRule().getLast(),
                            currentSymbol.isComplement);
                } else {
                    currentSymbol = getNextTerminal(((NonTerminal) currentSymbol).getRule().getGuard().getRight(),
                            currentSymbol.isComplement);
                }
            }
        }
        if (currentSymbol instanceof Terminal && isComplement) {
            Symbol complementSymbol = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0)));
            //todo ordering of links ok here for complement?
            complementSymbol.assignRight(currentSymbol.getRight());
            complementSymbol.assignLeft(currentSymbol.getLeft());
            currentSymbol = complementSymbol;
        }
        return currentSymbol;
    }
}
