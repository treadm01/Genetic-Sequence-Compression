package HuffmanCoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Encoder {
    List<Hnode> fullNodes;
    int fullAmount = 0;

    public List<Hnode> getOrderedNodes(Map<String, Integer> symbolFreqs) {
        List<Hnode> nodes = new ArrayList<>();

        for (String s : symbolFreqs.keySet()) {
            Hnode newNode = new Hnode(s, symbolFreqs.get(s));
            nodes.add(newNode);
            fullAmount += symbolFreqs.get(s);
        }

        fullNodes = orderNodes(nodes);
        return fullNodes;
    }

    public List<Hnode> orderNodes(List<Hnode> nodes) {
        nodes = nodes.stream()
                .sorted(Hnode::compareTo)
                .collect(Collectors.toList());
        return nodes;
    }

    public void buildTree() {
        Hnode lowestNode;
        Hnode secondLowestNode;
        int frequency = 0;
        while (frequency != fullAmount) {
            lowestNode = fullNodes.remove(0);
            secondLowestNode = fullNodes.remove(0);
            frequency = lowestNode.frequency + secondLowestNode.frequency;
            Hnode newNode = new Hnode("", frequency);
            newNode.addNodes(secondLowestNode, lowestNode); //todo think this ordering is correct, lower on the right
            fullNodes.add(newNode);
            orderNodes(fullNodes);
        }

        System.out.println(fullNodes.get(fullNodes.size()-1));
    }

}
