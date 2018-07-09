public class Guard extends Symbol {
    @Override
    public Boolean isGuard() {
        return true;
    }

    @Override
    public void assignLeft(Symbol symbol)  {
        if (!(symbol instanceof Rule)) {
            try {
                throw new Exception("NO!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            this.left = symbol;
        }
    }


    public Guard(String representation) {
        this.representation = representation;
    }
}
