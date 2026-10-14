package github.axolotl.coorder.service;

import com.vaadin.flow.signals.shared.SharedValueSignal;
import github.axolotl.coorder.entity.Room;
import org.springframework.stereotype.Service;

@Service
public class UserPurchaseService {

    private final RoomService roomService;

    public UserPurchaseService(RoomService roomService) {
        this.roomService = roomService;
    }

    public void updateQuantity(String userId, String roomId, String productId, int count) {
        Room room = roomService.getRoom(roomId);
        if (room != null) {
            room.updateQuantity(userId, productId, count);
        }
    }

    public SharedValueSignal<Integer> getTotalQuantitySignal(String roomId, String productId) {
        Room room = roomService.getRoom(roomId);
        if (room != null) {
            return room.getTotalQuantitySignal(productId);
        }
        return null;
    }

    public int getUserQuantity(String userId, String roomId, String productId) {
        Room room = roomService.getRoom(roomId);
        if (room != null) {
            return room.getUserQuantity(userId, productId);
        }
        return 0;
    }
}
