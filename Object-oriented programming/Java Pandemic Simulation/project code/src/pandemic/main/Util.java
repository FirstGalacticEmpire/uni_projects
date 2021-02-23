package pandemic.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Static class containing useful methods.
 */
public class Util {

    /**
     * Generates string of specified length.
     * @param stringLength - length of generated String
     * @return generatedString
     */
    public static String generateRandomString(int stringLength){
        //based on https://www.baeldung.com/java-random-string

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    /**
     * Converts date from numerical form to SimpleDateFormat.
     * @param dateInLong milliseconds from 1970
     * @return Formatted date as a String
     */
    public static String dateFromLong(long dateInLong){
        String pattern = "MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(dateInLong));
    }

    /**
     * Generates random number in specified range
     * @param lowerBound lower bound
     * @param higherBound higher bound
     * @return random number as a integer
     */
    public static int generateRandomNum(int lowerBound, int higherBound){
        return ThreadLocalRandom.current().nextInt(lowerBound, higherBound);
    }

}
