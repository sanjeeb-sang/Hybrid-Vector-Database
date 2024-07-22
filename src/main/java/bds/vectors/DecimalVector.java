package bds.vectors;

import bds.common.Logger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DecimalVector class that stores vector values. The stored vector values are stored as a single Strings where
 * each vector is separated by a delimited like '::'.
 */
public class DecimalVector {
    private List<BigDecimal> elements;
    private static final String DELIMITER = "::";
    private static Logger logger = Logger.getInstance();

    /**
     * Argument constructor for the DecimalVector class.
     * @param vectorElements Initial vector elements.
     */
    public DecimalVector(List<BigDecimal> vectorElements) {
        elements = vectorElements;
    }

    /**
     * No argument constructor for DecimalVector class.
     */
    public DecimalVector() {
        elements = new ArrayList<>();
    }

    /**
     * Method to add a new element to this vector instance.
     * @param element The BigDecimal element to add.
     */
    public void addElement(BigDecimal element) {
        elements.add(element);
    }

    /**
     * Method to return all the BigDecimal elements stored in this DecimalVector instance.
     * @return List of all the BigDecimal elements stored in this DecimalVector instance.
     */
    public List<BigDecimal> getElements() {
        return elements;
    }

    /**
     * Method to convert this DecimalVector object to a String.
     * @return string form of this DecimalVector object.
     */
    @Override
    public String toString() {
        if (elements == null) return "";
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (BigDecimal bd : elements) {
            if (index > 0) {
                sb.append(DELIMITER);
            }

            sb.append(bd.toString());
            index++;
        }

        return sb.toString();
    }

    /**
     * Method to create a DecimalVector object from a String. The string strList should contain all the vector
     * elements separated by the delimiter '::'.
     * @param strList The String from which a new DecimalVector is to be created.
     * @return the newly created DecimalVector instance.
     */
    public static DecimalVector fromString(String strList) {
        logger.debug("from string", "DecimalVector");
        if (strList == null || strList.trim().isEmpty()) return new DecimalVector(Arrays.asList(new BigDecimal("0")));

        String[] splittedList = strList.split(DELIMITER);
        List<BigDecimal> decimalsList = new ArrayList<>();
        for (String str : splittedList) {
            if (!str.trim().isEmpty()) {
                logger.debug("before parsing to num: " + str, "DecimalVector");
                decimalsList.add(new BigDecimal(str));
            }
        }

        return new DecimalVector(decimalsList);
    }
}