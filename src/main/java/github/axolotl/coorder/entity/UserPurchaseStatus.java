package github.axolotl.coorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPurchaseStatus {
    private String userId;
    private String roomId;
    private final HashMap<String, Integer> purchaseQuantities = new HashMap<>();

    public void updateQuantity(String productId, int count) {
        purchaseQuantities.put(productId, count);
    }
}
