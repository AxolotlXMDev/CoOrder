package github.axolotl.coorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private String description;
    private double price;
    private int minPurchaseQuantity;

    public String getNameAndDescription() {
        if (description != null && !description.isEmpty()) {
            return "%s(%s)".formatted(name, description);
        } else {
            return name;
        }
    }

    public String getPriceWithMinPurchaseQuantity() {
        if (minPurchaseQuantity > 1) {
            return "%.2f (%d份起购)".formatted(price, minPurchaseQuantity);
        } else {
            return "%.2f".formatted(price);
        }
    }
}
