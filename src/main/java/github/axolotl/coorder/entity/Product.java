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
}
