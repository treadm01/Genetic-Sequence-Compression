package HuffmanCoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Encoder {

    public List<Hnode> getOrderedNodes(Map<String, Integer> symbolFreqs) {
        List<Hnode> nodes = new ArrayList<>();

        for (String s : symbolFreqs.keySet()) {
            Hnode newNode = new Hnode(s, symbolFreqs.get(s));
            nodes.add(newNode);
        }

        nodes = nodes.stream()
                .sorted(Hnode::compareTo)
                .collect(Collectors.toList());

        System.out.println(nodes);

        return nodes;
    }

}
