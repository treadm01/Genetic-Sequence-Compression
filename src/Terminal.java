public class Terminal extends Symbol {
    long originalValue;

    public Terminal(long representation) { //TODO make long??
        originalValue = representation;
        this.representation = (representation * 2) + 1;
        left = new Symbol(); //todo having to add for equality check, not needed otherwise
    }

    @Override
    public String toString() {
        return String.valueOf((char) originalValue);
    }

    public long getOriginalValue() {
        return originalValue;
    }

//    public long reverseSymbol() {
//        char complement = 0;
//        if (originalValue == 'a') {
//            complement = 't';
//        }
//        else if (originalValue == 'c') {
//            complement = 'g';
//        }
//        else if (originalValue == 'g') {
//            complement = 'c';
//        }
//        else if (originalValue == 't') {
//            complement = 'a';
//        }
//        return (complement * 2) + 1;
//    }


}
