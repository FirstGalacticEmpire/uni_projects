package pandemic.shops;

import pandemic.main.Util;

import java.util.Date;

/**
 * Class simulating product.
 */
public class Product {
    private static int numOfProducts = 0;
    private final int id;
    private final String name;
    private final Long expirationDate;

    /**
     * Product constructor.
     */
    public Product() {
        this.id = numOfProducts;
        numOfProducts += 1;
        this.name = Util.generateRandomString(5);
        this.expirationDate = new Date().getTime() +
                Util.generateRandomNum(5, 250) * 1000L;
    }

    /**
     * @return Description of an product.
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                '}';
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

}
