package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    Symbol left, right;
    long representation;
    private static final long PRIME = 2265539; // from sequitur
    public Boolean isComplement = false;
    Boolean isEdited = false;
    public Symbol complement;
    int symbolIndex = 0; // to keep location of actual symbol for edit
    List<Edit> editList = new ArrayList<>(); // todo this shouldn't be needed for each, at symbol level

    public void setIsEdit(List<Edit> edits) {
        this.editList.addAll(edits);
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
        );
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

    //todo creating a new each time, must be an alternative without links, just get current complement
    public Symbol getReverseComplement() {
        Symbol left = createReverseComplement(this);
        Symbol right = createReverseComplement(this.getLeft()); // left and right symbols of reverse digram as it will be entered into the map

        right.assignLeft(left);
        left.assignRight(right);
        left.assignLeft(new Terminal('!')); //todo for comparisons in hashmap, complement requires a left.left

        return right;
    }

    public Boolean isARule() {
        return this.getLeft().getLeft().isGuard()
                && this.getRight().isGuard();
    }

    public Boolean isNotOverlapping(Symbol symbol) {
        return this.getRight() != symbol
                && this.getLeft() != symbol;
    }

    private Symbol createReverseComplement(Symbol currentSymbol) {
        Symbol reverse = new Symbol(); // could it ever be guard? todo yes seems to be, setting guards needlessly
        if (currentSymbol instanceof Terminal) { // right hand side of digram
            reverse = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0))); //todo a better way to get char
        }
        else if (currentSymbol instanceof NonTerminal) {
            reverse = new NonTerminal(((NonTerminal) currentSymbol).getRule());
            ((NonTerminal) currentSymbol).getRule().decrementCount();
        }

        reverse.isComplement = !currentSymbol.isComplement;
        reverse.complement = currentSymbol;
        currentSymbol.complement = reverse;

        return reverse;
    }
}
