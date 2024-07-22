package bds.vectors;

import bds.common.Logger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * Class containing Vector Functions that can be used inside SQL. These functions include SUM_ELEMENTS, SIMILARITY,
 * and COSINE_SIMILARITY.
 */
public class VectorFunctions {

    private static Logger logger = Logger.getInstance();
    private static MathContext _mc = new MathContext(10);

    /**
     * Implementation for the SIMILARITY function.
     * @param columnVector the value stored in the vector column.
     * @param otherVector List of BigDecimal elements passed to SIMILARITY function.
     * @return The Similarity function result in Double.
     */
    public static double similarity(String columnVector, List<BigDecimal> otherVector) {
        logger.debug("SimilaritySearch: " + columnVector + ", " + otherVector.toString(), "VectorFunctions");
        BigDecimal total = BigDecimal.ZERO;
        try {
            DecimalVector vector = DecimalVector.fromString(columnVector.trim());
            List<BigDecimal> vectorCols = vector.getElements();
            for (int i = 0; i < vectorCols.size(); i++) {
                BigDecimal otherVal = getOrDefault(otherVector, i, BigDecimal.ZERO);
                BigDecimal colVal = getOrDefault(vectorCols, i, BigDecimal.ZERO);
                total = total.add(otherVal.multiply(colVal));
                logger.debug("otherval:" + otherVal + ", colval: " + colVal, "VectorFunctions");

            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return total.doubleValue();
    }

    /**
     * Implementation for the COSINE_SIMILARITY function.
     * @param columnVector the value stored in the vector column.
     * @param otherVector List of BigDecimal elements passed to COSINE_SIMILARITY function.
     * @return The CosineSimilarity function result in Double.
     */
    public static double cosineSimilarity(String columnVector, List<BigDecimal> otherVector) {

        try {
            BigDecimal dotProduct = BigDecimal.ZERO;
            BigDecimal normA = BigDecimal.ZERO;
            BigDecimal normB = BigDecimal.ZERO;

            DecimalVector vector1 = DecimalVector.fromString(columnVector.trim());
            int maxDimension = Math.max(vector1.getElements().size(), otherVector.size());
            //BigDecimal[] powDimensionA = new BigDecimal[maxDimension];
            //BigDecimal[] powDimensionB = new BigDecimal[maxDimension];

            for (int i = 0; i < maxDimension; i++) {
                BigDecimal Element1 = getOrDefault(vector1.getElements(), i, BigDecimal.ZERO);
                BigDecimal Element2 = getOrDefault(otherVector, i, BigDecimal.ZERO);
                BigDecimal dot = Element1.multiply(Element2);
                logger.debug("1: " + Element1.toString() + ", " + Element2.toString(), "VectorFunctions");
                dotProduct = dotProduct.add(dot);
                normA = normA.add(Element1.pow(2));
                normB = normB.add(Element2.pow(2));
                logger.debug("NormA: " + normA.toString() + " , NormB: " + normB.toString(), "VectorFunctions");

                //powDimensionA[i] = Element1.pow(2);
                //powDimensionB[i] = Element2.pow(2);
            }

            BigDecimal div = normA.sqrt(new MathContext(20)).multiply(normB.sqrt(new MathContext(20)));
            logger.debug("div : " + div, "VectorFunctions");
            return (dotProduct.doubleValue() / div.doubleValue());
        }
        catch (Exception ex) {
            System.out.println("Exception");
            System.out.println(ex.getMessage());
            return BigDecimal.ONE.doubleValue();
        }
    }

    /**
     * Implementation for the SUM_ELEMENTS function. This function finds the sum of all the elements in the vector and
     * returns that sum.
     * @param columnVector the value stored in the vector column.
     * @return the sum of all the elements.
     */
    public static BigDecimal sumElements(String columnVector) {
        logger.debug(columnVector, "VectorFunctions");

        DecimalVector vector = DecimalVector.fromString(columnVector.trim());
        List<BigDecimal> vectorCols = vector.getElements();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < vectorCols.size(); i++) {
            BigDecimal colVal = getOrDefault(vectorCols, i, BigDecimal.ZERO);
            total = total.add(colVal);
        }
        return total;
    }

    private static BigDecimal getMagnitude(List<BigDecimal> elements) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal el : elements) {
            total = total.add(el.pow(2));
        }

        return total.sqrt(_mc);
    }

    private static BigDecimal getOrDefault(List<BigDecimal> decimalsList, int index, BigDecimal defaultVal) {
        if (decimalsList == null || index >= decimalsList.size()) return defaultVal;

        return decimalsList.get(index);
    }
}