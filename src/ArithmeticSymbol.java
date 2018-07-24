public class ArithmeticSymbol {
    Character representation;
    Integer probability;
    Double probabiltiyRatio;


    public ArithmeticSymbol(Character c) {
        setRepresentation(c);
    }


    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }

    public Double getProbabiltiyRatio() {
        return probabiltiyRatio;
    }

    public void setProbabiltiyRatio(Double probabiltiyRatio) {
        this.probabiltiyRatio = probabiltiyRatio;
    }


    public Character getRepresentation() {
        return representation;
    }

    public void setRepresentation(Character representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return getProbabiltiyRatio().toString();
    }
}
