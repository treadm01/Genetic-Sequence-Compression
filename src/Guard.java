public class Guard extends Symbol {
    @Override
    public Boolean isGuard() {
        return true;
    }

    public Guard(String representation) {
        this.representation = representation;
    }
}
