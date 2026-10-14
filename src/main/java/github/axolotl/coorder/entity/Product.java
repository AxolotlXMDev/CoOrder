package github.axolotl.coorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && minPurchaseQuantity == product.minPurchaseQuantity && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, minPurchaseQuantity);
    }

    public String getId() {
        return String.valueOf(hashCode());//TODO: 改为json存储id或者读入时自增生成id（现在数据量小可用，但是仍有碰撞风险）
    }
}
