package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    Rule nodeRule;
    List<TreeNode> children = new ArrayList<>();

    public TreeNode(Rule rule) {
        nodeRule = rule;
    }

    public String getAllNodes() {
        String s = "";
        for (TreeNode tn : children) {
            s += tn.toString() + " ";
        }
        return s;
    }

    @Override
    public String toString() {
        String s =  nodeRule.getSymbolString(nodeRule, nodeRule.isComplement);
        return s;
    }
}
