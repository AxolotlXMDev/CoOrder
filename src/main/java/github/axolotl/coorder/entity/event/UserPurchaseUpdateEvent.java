package github.axolotl.coorder.entity.event;

public record UserPurchaseUpdateEvent(String userId,
                                      String productId,
                                      int orderQuantity,
                                      int newQuantity) {
    public static UserPurchaseUpdateEvent EMPTY = new UserPurchaseUpdateEvent(null, null, 0, 0);
}
