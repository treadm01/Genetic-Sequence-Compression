import java.util.List;

public class uncompress {

    //TODO save to file and retrieve in a way that objects are known to be terminal, non terminal, rule
    // from there can start to compare

    public String getOutput(rule r, List<rule> compressedRule, String soFar) {
        String uncompressed = soFar;
        for (symbol s : r.values) {
            if (s instanceof terminal) {
                uncompressed += s.getRepresentation();
            }
            else {
                for (rule rx : compressedRule) {
                    if (rx.getRuleNumber() == Integer.parseInt(String.valueOf(s.getRepresentation()))) {
                        uncompressed = getOutput(rx, compressedRule, uncompressed);
                    }
                }
            }
        }
        return uncompressed;
    }

    public String processInput(List<rule> compressedRule) {

        rule firstRule = compressedRule.get(0);
        String uncompressed = "";

        uncompressed += getOutput(firstRule, compressedRule, uncompressed);

        return uncompressed;
    }
}
