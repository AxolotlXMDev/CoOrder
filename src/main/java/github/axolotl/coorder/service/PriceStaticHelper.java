
package github.axolotl.coorder.service;

import github.axolotl.coorder.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PriceStaticHelper {
    private final Map<UniqueProduct, Double> priceMap;

    public PriceStaticHelper(Shop shop) {
        Map<UniqueProduct, Double> priceMap = new HashMap<>();
        shop.getAllProducts().forEach(product -> {
            UniqueProduct uniqueProduct = new UniqueProduct(product.getId(), shop.getId());
            priceMap.put(uniqueProduct, product.getPrice());
        });
        this.priceMap = Collections.unmodifiableMap(priceMap);
    }


    public double getPrice(String shopId, String productId) {
        UniqueProduct uniqueProduct = new UniqueProduct(productId, shopId);
        Double v = priceMap.get(uniqueProduct);
        if (v == null) {
            throw new IllegalArgumentException("Product not found: " + productId + " in shop: " + shopId);
        }
        return v;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class UniqueProduct {
        private final String productId;
        private final String shopId;
    }
}
