package github.axolotl.coorder.service;

import github.axolotl.coorder.component.RoomComponent;
import github.axolotl.coorder.entity.Room;
import org.springframework.stereotype.Service;

@Service
public class UserPurchaseService {

    private final RoomComponent roomComponent;

    public UserPurchaseService(RoomComponent roomComponent) {
        this.roomComponent = roomComponent;
    }

    public void updateQuantity(String userId, String roomId, String productId, int count) {
        Room room = roomComponent.getRoom(roomId);
        if (room != null) {
            room.updateQuantity(userId, productId, count);
        }
    }
}
