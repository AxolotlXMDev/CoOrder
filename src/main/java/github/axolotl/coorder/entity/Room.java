package github.axolotl.coorder.entity;

import com.vaadin.flow.signals.shared.SharedValueSignal;
import lombok.Getter;

import java.util.HashMap;


public class Room {
    @Getter
    private final String roomId;
    @Getter
    private final String ownerUserId;
    @Getter
    private final Shop shop;
    private final HashMap<String, SharedValueSignal<Integer>> purchaseQuantitiesInRoom = new HashMap<>();
    private final HashMap<String, UserPurchaseStatus> userStatuses = new HashMap<>();

    public Room(String roomId, String ownerUserId, Shop shop) {
        this.roomId = roomId;
        this.ownerUserId = ownerUserId;
        this.shop = shop;
        shop.getAllProducts().forEach(product -> purchaseQuantitiesInRoom.put(product.getId(), new SharedValueSignal<>(0)));
    }

    public SharedValueSignal<Integer> getTotalQuantitySignal(String productId) {
        return purchaseQuantitiesInRoom.get(productId);
    }

    public int getUserQuantity(String userId, String productId) {
        UserPurchaseStatus status = userStatuses.get(userId);
        if (status != null) {
            return status.getPurchaseQuantities().getOrDefault(productId, 0);
        }
        return 0;
    }

    public void joinRoom(String userId) {
        userStatuses.put(userId, new UserPurchaseStatus(userId, roomId));
    }

    public UserPurchaseStatus getUserStatusById(String userId) {
        return userStatuses.get(userId);
    }

    public void updateQuantity(String userId, String productId, int count) {
        userStatuses.get(userId).updateQuantity(productId, count);
        int totalCount = userStatuses.values().stream()
                .mapToInt(status -> status.getPurchaseQuantities().getOrDefault(productId, 0))
                .sum();
        purchaseQuantitiesInRoom.get(productId).set(totalCount);
    }

    public int getProductQuantityByUser(String userId, String productId) {
        UserPurchaseStatus status = userStatuses.get(userId);
        if (status != null) {
            return status.getPurchaseQuantities().getOrDefault(productId, 0);
        }
        return 0;
    }

    public boolean hasUser(String userId) {
        return userStatuses.containsKey(userId);
    }
}
