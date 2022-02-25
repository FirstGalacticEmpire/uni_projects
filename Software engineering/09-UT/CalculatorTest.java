package put.io.testing.junit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    public void setUp() throws Exception {
        calculator = new Calculator();
    }

    private int generateRandomPositiveInt() {
        return ThreadLocalRandom.current().nextInt(1, 50 + 1);
    }

    @Test
    void testMultiplication() {
        for (int x = 5; x > 0; x--) {
            int num1 = generateRandomPositiveInt();
            int num2 = generateRandomPositiveInt();
            assertEquals(num1 * num2, calculator.multiply(num1, num2));
            assertEquals(num1 * num2, calculator.multiply(num2, num1));
        }
    }

    @Test
    void testAddition() {
        for (int x = 5; x > 0; x--) {
            int num1 = generateRandomPositiveInt();
            int num2 = generateRandomPositiveInt();
            assertEquals(num1 + num2, calculator.add(num1, num2));
            assertEquals(num2 + num1, calculator.add(num2, num1));
        }
    }

    @Test
    void testAddPositiveNumbers() {
        for (int x = 1; x > 0; x--) {
            int num1 = generateRandomPositiveInt();
            int num2 = generateRandomPositiveInt();
            assertEquals(num1 + num2, calculator.addPositiveNumbers(num1, num2));
            assertThrows(IllegalArgumentException.class, () -> calculator.addPositiveNumbers(num1, -num2));

        }
    }

}