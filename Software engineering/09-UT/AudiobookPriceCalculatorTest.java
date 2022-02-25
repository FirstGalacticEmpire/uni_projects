package put.io.testing.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import put.io.testing.audiobooks.Audiobook;
import put.io.testing.audiobooks.AudiobookPriceCalculator;
import put.io.testing.audiobooks.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AudiobookPriceCalculatorTest {
    private AudiobookPriceCalculator audiobookPriceCalculator;
    private Audiobook audiobook;

    @BeforeEach
    public void setUP() throws Exception{
        audiobookPriceCalculator = new AudiobookPriceCalculator();
        audiobook = new Audiobook("Audiobook", 100);
    }

    @Test
    void testCalculate1(){
        Customer customer = new Customer("Random_name", Customer.LoyaltyLevel.STANDARD, false);
        assertEquals(100, audiobookPriceCalculator.calculate(customer, audiobook));
    }

    @Test
    void testCalculate2(){
        Customer customer = new Customer("Random_name", Customer.LoyaltyLevel.SILVER, false);
        assertEquals(90, audiobookPriceCalculator.calculate(customer, audiobook));
    }

    @Test
    void testCalculate3(){
        Customer customer = new Customer("Random_name", Customer.LoyaltyLevel.GOLD, false);
        assertEquals(80, audiobookPriceCalculator.calculate(customer, audiobook));
    }

    @Test
    void testCalculate4(){
        Customer customer = new Customer("Random_name", Customer.LoyaltyLevel.STANDARD, true);
        assertEquals(0, audiobookPriceCalculator.calculate(customer, audiobook));
    }

    @Test
    void testCalculate5(){
        Customer customer = new Customer("Random_name", Customer.LoyaltyLevel.GOLD, true);
        assertEquals(0, audiobookPriceCalculator.calculate(customer, audiobook));
    }

    @Test
    void testCalculate6(){
        Customer customer = new Customer("Random_name", Customer.LoyaltyLevel.SILVER, true);
        assertEquals(0, audiobookPriceCalculator.calculate(customer, audiobook));
    }

}
