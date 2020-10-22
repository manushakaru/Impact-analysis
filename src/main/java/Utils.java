import java.util.ArrayList;

public class Utils {
    public static int depth=1;

    public static String[] GetStringArray(ArrayList<String> arr)
    {//check to get from directly Referance entity
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }
}
