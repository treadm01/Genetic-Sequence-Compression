package HuffmanCoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class Encoder {
    List<Hnode> fullNodes;
    int fullAmount = 0;
    Hnode headNode;

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
        System.out.println(fullNodes);
        while (frequency != fullAmount) {
            lowestNode = fullNodes.remove(fullNodes.size() - 1);
            secondLowestNode = fullNodes.remove(fullNodes.size() - 1);
    //        System.out.println("after removal " + fullNodes);
            frequency = lowestNode.frequency + secondLowestNode.frequency;
            Hnode newNode = new Hnode("", frequency);
            newNode.addNodes(secondLowestNode, lowestNode); //todo think this ordering is correct, lower on the right
            lowestNode.head = newNode;
            secondLowestNode.head = newNode;
            fullNodes.add(newNode);
  //          System.out.println("adding new node " + fullNodes);
            fullNodes = orderNodes(fullNodes);
//            System.out.println("ordered " + fullNodes);
        }

        headNode = fullNodes.get(fullNodes.size()-1);
        System.out.println(headNode);
        System.out.println(headNode.right.left);
    }

    // next one to build a record of binary for each symbol, right?
    // can do at same time as tree, more of a pain with gamma prefix

    public void traverseTree() {
        Stack<Hnode> stack = new Stack<>();
        stack.push(headNode);
        String binaryCode = "";
        Hnode currentNode = headNode;
        currentNode.binary = binaryCode;
        while (!stack.isEmpty()) {
            if (currentNode.right != null && currentNode.left != null) {
                currentNode.right.binary = currentNode.binary + "1";
                currentNode.left.binary = currentNode.binary + "0";
                stack.push(currentNode.right);
                currentNode = currentNode.left;
            }
            else if (currentNode.left == null && currentNode.right == null) {
                System.out.println(currentNode.symbol + " = " + currentNode.binary);
                currentNode = stack.pop();
            }
        }
    }


}
