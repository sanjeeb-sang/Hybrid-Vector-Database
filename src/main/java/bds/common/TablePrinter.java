package bds.common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that prints ResultSet values in a formatted way.
 */
public class TablePrinter {

    private ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
    private ArrayList<String> currentRow = new ArrayList<>();
    private static Logger logger = Logger.getInstance();

    /**
     * Add a new column item.
     * @param item column item.
     */
    public void addColumnItem(String item) {
        item = item.trim();
        currentRow.add(item);
    }

    /**
     * Specify that next row will now start.
     */
    public void nextRow() {
        table.add(currentRow);
        currentRow = new ArrayList<>();
    }

    /**
     * Method to print all the stored values as a table.
     */
    public void printTable() {
        if (table == null && (currentRow == null || currentRow.isEmpty())) return;

        if (currentRow != null && !currentRow.isEmpty()) {
            table.add(currentRow);
        }

        HashMap<Integer, Integer> columnLengthMap = getColumnLengthMap();
        int maxRowLength = getMaxRowLength(columnLengthMap);
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append("=".repeat(maxRowLength)).append("\n");
        for (ArrayList<String> row : table) {
            sb.append(" | ");
            for (int i = 0; i < row.size(); i++) {
                String columnItem = row.get(i);
                sb.append(columnItem);
                int columnLength = columnLengthMap.get(i);
                int lengthDiff = columnLength - (columnItem.length());
                if (lengthDiff > 0) {
                    sb.append(" ".repeat(lengthDiff));
                }

                sb.append(" | ");
            }

            sb.append("\n");
            sb.append(" ").append("=".repeat(maxRowLength)).append("\n");
        }

        logger.output(sb.toString());
    }

    /**
     * Method to create and return the ColumnLengthMap.
     * @return the columnLength map.
     */
    private HashMap<Integer, Integer> getColumnLengthMap() {
        HashMap<Integer, Integer> lengthMap = new HashMap<>();
        for (ArrayList<String> row : table) {
            for (int i = 0; i < row.size(); i++) {
               String columnItem = row.get(i);
               int currentLength = columnItem.length();
               if (lengthMap.containsKey(Integer.valueOf(i))) {
                   if (currentLength > lengthMap.get(Integer.valueOf(i))) {
                       lengthMap.put(Integer.valueOf(i), currentLength);
                   }
               } else {
                   lengthMap.put(Integer.valueOf(i), currentLength);
               }
            }
        }
        return lengthMap;
    }

    private int getMaxRowLength(HashMap<Integer, Integer> maxColumnLengths) {
        int numColumns = maxColumnLengths.size();
        int fixedWidth = numColumns * 3 + 1;
        int totalWidth = fixedWidth;
        for (int i = 0; i < numColumns; i++) {
            if (maxColumnLengths.containsKey(Integer.valueOf(i))) {
                totalWidth += maxColumnLengths.get(Integer.valueOf(i));
            }
        }
        return totalWidth;
    }
}
