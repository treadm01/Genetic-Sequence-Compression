package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    Symbol left, right;
    long representation;
    private static final long PRIME = 2265539; // from sequitur
    public Boolean isComplement = false;
    Boolean isEdited = false;
    //String edits = "";
    public Symbol complement;
    int index;
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


    //todo for terminal and nonterminal
    //get new symbols and assign them together... could be in the class? its creating a digram but could return, just the right
    public Symbol getReverseComplement() {
        Symbol left = createReverseComplement(this);
        Symbol right = createReverseComplement(this.getLeft()); // left and right symbols of reverse digram as it will be entered into the map

        right.assignLeft(left);
        left.assignRight(right);
        left.assignLeft(new Terminal('!')); //todo for comparisons in hashmap, complement requires a left.left

        return right;
    }

    //todo should be in symbol? - used by getreverse, creates the actual instance
    public Symbol createReverseComplement(Symbol currentSymbol) {
        Symbol reverse = new Symbol(); // could it ever be guard? todo yes seems to be, setting guards needlessly
        if (currentSymbol instanceof Terminal) { // right hand side of digram
            reverse = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0))); //todo a better way to get char
        }
        else if (currentSymbol instanceof NonTerminal) {
            reverse = new NonTerminal(((NonTerminal) currentSymbol).getRule());
            ((NonTerminal) currentSymbol).getRule().decrementCount();
        }

        // if nonterminal is complement, it's complement wont be, same for terminals,
        // shouldn't be an issue as all are unique and values aren't altered elsewhere
        //currentSymbol.complement = left;
        reverse.isComplement = !currentSymbol.isComplement;
        reverse.complement = currentSymbol;

        return reverse;
    }

//    // todo this is only used once for finding approx matches, must be a better way
//    // the way in which it is called as object method, but still requires the next right symbol is not good
//    public Symbol getNextTerminal(Symbol currentSymbol, Boolean isComplement) {
//        //todo is guard an ok check, does this really always get the next terminal
//        while (!(currentSymbol instanceof Terminal) && !currentSymbol.isGuard()) {
//            if (currentSymbol instanceof Terminal) {
//                if (isComplement) {
//                    currentSymbol = currentSymbol.getLeft();
//                }
//                else {
//                    currentSymbol = currentSymbol.getRight();
//                }
//            }
//            else {
//                if (isComplement) {
//                    currentSymbol = getNextTerminal(((NonTerminal) currentSymbol).getRule().getLast(),
//                            currentSymbol.isComplement);
//                } else {
//                    currentSymbol = getNextTerminal(((NonTerminal) currentSymbol).getRule().getGuard().getRight(),
//                            currentSymbol.isComplement);
//                }
//            }
//        }
//        if (currentSymbol instanceof Terminal && isComplement) {
//            Symbol complementSymbol = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0)));
//            //todo ordering of links ok here for complement?
//            complementSymbol.assignRight(currentSymbol.getRight());
//            complementSymbol.assignLeft(currentSymbol.getLeft());
//            currentSymbol = complementSymbol;
//        }
//        return currentSymbol;
//    }
}
