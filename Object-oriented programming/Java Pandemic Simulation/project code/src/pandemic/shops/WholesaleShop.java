package pandemic.shops;

import pandemic.map.Position;

final public class WholesaleShop extends Shop {

    /**
     * Wholesaleshop constructor.
     * @param posOfShop
     */
    public WholesaleShop(Position posOfShop) {
        super(posOfShop);
    }

    /**
     * Creates a product.
     */
    public synchronized void CreateProduct() {
        Product product = new Product();
        if(this.addProductToListOfProducts(product)){

        }
        else {
//            System.out.println("Wasnt able to add product");
        }
    }

}
