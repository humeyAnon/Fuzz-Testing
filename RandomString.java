
import java.util.Arrays;
import java.util.Random;

public class RandomString {

    private String value = "ABCDEFGHIJKLMNOPQRSTUVWRXYZ" +
                           "abcdefghijklmnopqrstuvwrxyz" +
                           "1234567890-=[];'./<>!@#$%^&" +
                           "{}_+?,:\\|*()";
    private Random rnd = new Random();

    public String generateRandomString() {

        // Creating random string length, making char arrays
        int stringLength = rnd.nextInt(100);
        char[] valueCharArray = value.toCharArray();
        char[] stringOutput = new char[stringLength];

        // Looping through until the desired string length, selecting random chars from
        // the value char array
        for(int i = 0; i < stringLength; i++){
            stringOutput[i] = valueCharArray[rnd.nextInt(valueCharArray.length)];
        }
        return String.valueOf(stringOutput);
    }
}
