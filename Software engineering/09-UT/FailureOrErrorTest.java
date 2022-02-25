package put.io.testing.junit;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FailureOrErrorTest {
    private int generateRandomPositiveInt() {
        return ThreadLocalRandom.current().nextInt(1, 50 + 1);
    }


    @Test
    void test1() {
        //Failure
        assertTrue(false);
    }

    @Test
    void test2() {
        int num1 = generateRandomPositiveInt();
        int num2 = generateRandomPositiveInt();
        Calculator calculator = new Calculator();
        //Error
        calculator.addPositiveNumbers(num1, -num2);
    }

    @Test
    void test3(){
        try{
            assertTrue(false);
            //assertEquals(2+5, 2+6);
        }
        catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}
