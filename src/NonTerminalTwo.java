public class NonTerminalTwo extends Symbol implements NonTerminalInterface {
    RuleImpl rule;

    public NonTerminalTwo(String nonTerminal) {
        setSymbol(nonTerminal);
    }

    @Override
    public RuleImpl getRule() {
        return rule;
    }

    @Override
    public void setRule(RuleImpl rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {return true;}
        else {return false;}
    }

    @Override
    public String toString() {
        return this.getSymbol();
    }
}
