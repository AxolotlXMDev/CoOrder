package github.axolotl.coorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private String id;
    private String name;
    private String description;
    private List<ProductSection> sections;


    public List<ProductSection> getSections() {
        return sections == null ? new ArrayList<>() : sections;
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        if (sections != null) {
            for (ProductSection section : sections) {
                if (section.getProducts() != null) {
                    allProducts.addAll(section.getProducts());
                }
            }
        }
        return allProducts;
    }
}
