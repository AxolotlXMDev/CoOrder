package github.axolotl.coorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSection {
    private String name;
    private List<Product> products;

    public List<Product> getProducts() {
        return products == null ? new ArrayList<>() : products;
    }
}
