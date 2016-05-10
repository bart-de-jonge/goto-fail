package control;

/**
 * Created by Bart on 10/05/2016.
 */
public class CountUtilities {

    public static final int NUMBER_OF_CELLS_PER_COUNT = 4;

    /**
     * Format double into a nice displayable string.
     * @param d - the double to format
     * @return - a formatted string containng the double
     */
    public static String formatDouble(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

    /**
     * Parse a string containing a count number to a correctly parsed and formatted string.
     * @param countNumber - the string containing the countNumber to parse
     * @return - the parsed and formatted countnumber
     */
    public static String parseCountNumber(String countNumber) {
        double res = countNumber.isEmpty() ? 0 : Double.parseDouble(countNumber);
        res = Math.round(res * NUMBER_OF_CELLS_PER_COUNT)
                / (double) NUMBER_OF_CELLS_PER_COUNT;
        return formatDouble(res);
    }


}

