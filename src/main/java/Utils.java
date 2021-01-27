import java.util.ArrayList;

public class Utils {

    /**
     * Initial depth  =  1
     */
    public static int depth = 1;
    /**
     * Default strategy of impact analysis is analyze the current opened java file
     */
    public static boolean isCurrent = true;

    /**
     * Returns String array
     * Take ArrayList of Strings and return array of strings
     *
     * @param arr array of strings
     * @return string array
     */
    public static String[] GetStringArray(ArrayList<String> arr) {
        String[] strArray = new String[arr.size()];
        strArray = arr.toArray(strArray);
        return strArray;
    }
}
