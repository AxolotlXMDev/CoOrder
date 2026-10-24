package github.axolotl.coorder.entity;

import com.vaadin.flow.signals.shared.SharedNumberSignal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPurchaseStatus {
    private String userId;
    private String roomId;
    private final Map<String, Integer> purchaseQuantities = new ConcurrentHashMap<>();
    private final SharedNumberSignal totalPriceSignal = new SharedNumberSignal(0d);

    public void updateQuantity(String productId, int quantityDifference, double priceDifference) {
        totalPriceSignal.update(current -> current + priceDifference);
        purchaseQuantities.put(productId, purchaseQuantities.getOrDefault(productId, 0) + quantityDifference);
    }

    public Integer getOldQuantity(String productId) {
        return purchaseQuantities.getOrDefault(productId, 0);
    }
}
