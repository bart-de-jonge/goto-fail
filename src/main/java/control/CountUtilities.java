package control;

/**
 * Created by Bart on 10/05/2016.
 */
public class CountUtilities {

    public static int numberOfCellsPerCount = 4;

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

    public static String parseCountNumber(String countNumber) {
        double res = countNumber.isEmpty() ? 0 : Double.parseDouble(countNumber);
        res = Math.round(res * numberOfCellsPerCount)
                / (double) numberOfCellsPerCount;
        return formatDouble(res);
    }


}

