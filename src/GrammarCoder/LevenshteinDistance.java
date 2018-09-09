package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class LevenshteinDistance {
    List<List<Integer>> editTable;
    List<Character> FIRST;
    List<Character> SECOND;
    int FIRST_SIZE;
    int SECOND_SIZE;

    public LevenshteinDistance(List<Character> first, List<Character> second) {
        List<List<Integer>> array = new ArrayList<>();
        FIRST_SIZE = first.size();
        SECOND_SIZE = second.size();

        while (first.size() < second.size()) {
            first.add('x');
        }
        while (second.size() < first.size()) {
            second.add('x');
        }

        for (int i = 0; i < 10; i++) {
            array.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                array.get(i).add(0);
            }
        }

        for (int i = 1; i < first.size() + 1; i++) {
            array.get(i).set(0, i);
        }

        for (int i = 1; i < second.size() + 1; i++) {
            array.get(0).set(i, i);
        }

        int editCost = 0;
        for (int i = 1; i < first.size(); i++) {
            for (int j = 1; j < second.size(); j++) {
                if (first.get(j) == second.get(i)) {
                    editCost = 0;
                    array.get(j).set(i,
                            getMinimum(array.get(j - 1).get(i),
                                    array.get(j).get(i - 1),
                                    array.get(j - 1).get(i - 1)));
                }
                else {
                    editCost = 1;
                    array.get(j).set(i,
                            getMinimum(array.get(j - 1).get(i) + 1,
                                    array.get(j).get(i - 1) + 1,
                            array.get(j - 1).get(i - 1) + editCost));
                }
            }
        }

        for (List l : array) {
            System.out.println(l);
        }
        editTable = array;
        FIRST = first;
        SECOND = second;
    }

    public int getMinimum(int delete, int insert, int sub) {
        List<Integer> values = new ArrayList<>();
        values.add(delete);
        values.add(insert);
        values.add(sub);
        Integer min = Integer.MAX_VALUE;
        for (Integer i : values) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    public void findPath() {
        int currentX = FIRST_SIZE - 1;
        int currentY = SECOND_SIZE - 1;
        int currentCell = editTable.get(currentX).get(currentY);
        System.out.println(currentCell);

        while (currentX != 0 && currentY != 0) {
            int topCell = editTable.get(currentX - 1).get(currentY);
            int leftCell = editTable.get(currentX).get(currentY - 1);
            int diagonalCell = editTable.get(currentX - 1).get(currentY - 1);

            if (diagonalCell <= topCell && diagonalCell <= leftCell
                    && (diagonalCell == currentCell || diagonalCell == currentCell - 1)) {
                currentX = currentX - 1;
                currentY = currentY - 1;
                if (diagonalCell == currentCell - 1) {
                    System.out.println("sub");
                } else {
                    System.out.println("the same");
                }
                currentCell = editTable.get(currentX).get(currentY);
                System.out.println(currentCell);
            } else if (leftCell <= topCell
                    && (leftCell == currentCell || leftCell == currentCell - 1)) {
                currentY = currentY - 1;
                System.out.println("insert");
                currentCell = editTable.get(currentX).get(currentY);
                System.out.println(currentCell);
            } else {
                currentX = currentX - 1;
                System.out.println("delete");
                currentCell = editTable.get(currentX).get(currentY);
                System.out.println(currentCell);
            }
        }
    }
}
