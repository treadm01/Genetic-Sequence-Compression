public class ArithmeticSymbol {
    Character representation;
    Integer probability;
    Double probabiltiyRatio;
    Integer segmentStart = 0;
    Integer segmentEnd = 0;


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

    public Integer getSegmentStart() {
        return segmentStart;
    }

    public void setSegmentStart(Integer segmentStart) {
        this.segmentStart = segmentStart;
    }

    @Override
    public String toString() {
        return getProbabiltiyRatio().toString();
    }

    public Integer getSegmentEnd() {
        return segmentEnd;
    }

    public void setSegmentEnd(Integer segmentEnd) {
        this.segmentEnd = segmentEnd;
    }
}
