package github.axolotl.coorder.entity;

import com.vaadin.flow.signals.shared.SharedNumberSignal;
import com.vaadin.flow.signals.shared.SharedValueSignal;
import github.axolotl.coorder.entity.event.RoomMemberChangeEvent;
import github.axolotl.coorder.entity.event.RoomNumberChangeEventType;
import github.axolotl.coorder.entity.event.UserPurchaseUpdateEvent;
import github.axolotl.coorder.service.PriceHelper;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Room {
    @Getter
    private final String roomId;
    @Getter
    private final String ownerUserId;
    @Getter
    private final Shop shop;
    private final Map<String, SharedValueSignal<Integer>> purchaseQuantitiesInRoom = new ConcurrentHashMap<>();
    private final Map<String, UserPurchaseStatus> userStatuses = new ConcurrentHashMap<>();
    private final SharedNumberSignal totalPriceSignalInRoom = new SharedNumberSignal(0d);
    private final PriceHelper priceHelper;
    @Getter
    private final SharedValueSignal<RoomMemberChangeEvent> roomMemberEventSignal = new SharedValueSignal<>(RoomMemberChangeEvent.EMPTY);
    @Getter
    private final SharedValueSignal<UserPurchaseUpdateEvent> purchaseUpdateEventSignal = new SharedValueSignal<>(UserPurchaseUpdateEvent.EMPTY);


    public Room(String roomId, String ownerUserId, Shop shop) {
        this.roomId = roomId;
        this.ownerUserId = ownerUserId;
        this.shop = shop;
        this.priceHelper = new PriceHelper(shop);
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
        if (hasUser(userId)) {
            return;
        }
        roomMemberEventSignal.set(new RoomMemberChangeEvent(RoomNumberChangeEventType.USER_JOIN, userId));
        userStatuses.put(userId, new UserPurchaseStatus(userId, roomId));
    }

    public UserPurchaseStatus getUserStatusById(String userId) {
        return userStatuses.get(userId);
    }

    public void updateQuantity(String userId, String productId, int newQuantity) {
        int oldQuantity = getUserOldQuantity(userId, productId);
        int quantityDifference = newQuantity - oldQuantity;//变化的数量
        double priceDifference = quantityDifference * priceHelper.getPrice(shop.getId(), productId);//变化的价格

        userStatuses.get(userId).updateQuantity(productId, quantityDifference, priceDifference);
        purchaseQuantitiesInRoom.get(productId).update(current -> current + quantityDifference);//更新房间商品总数量
        purchaseUpdateEventSignal.set(new UserPurchaseUpdateEvent(userId, productId, oldQuantity, newQuantity));
        totalPriceSignalInRoom.update(current -> current + priceDifference);//更新房间总价格
    }

    private int getUserOldQuantity(String userId, String productId) {
        UserPurchaseStatus status = userStatuses.get(userId);
        if (status != null) {
            return status.getOldQuantity(productId);
        }
        return 0;
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

    public void removeUser(String userId) {
        userStatuses.get(userId).getPurchaseQuantities()
                .forEach((productId, quantity) -> updateQuantity(userId, productId, 0));
        roomMemberEventSignal.set(new RoomMemberChangeEvent(RoomNumberChangeEventType.USER_LEAVE, userId));
        userStatuses.remove(userId);
    }

    public SharedNumberSignal getRoomPriceSignal() {
        return totalPriceSignalInRoom;
    }

    public SharedNumberSignal getUserPriceSignal(String userId) {
        UserPurchaseStatus status = userStatuses.get(userId);
        if (status != null) {
            return status.getTotalPriceSignal();
        }
        return new SharedNumberSignal(0d);
    }

    public Collection<UserPurchaseStatus> getUserStatus() {
        return userStatuses.values();
    }

    public void destroy() {
        userStatuses.keySet().forEach(this::removeUser);
        purchaseQuantitiesInRoom.clear();
        userStatuses.clear();
    }
}
