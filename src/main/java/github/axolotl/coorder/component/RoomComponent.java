package github.axolotl.coorder.component;

import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.Shop;
import github.axolotl.coorder.service.ShopService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;

@Component
public class RoomComponent {
    private final HashMap<String, Room> rooms = new HashMap<>();
    private final Random random = new Random();
    private final int roomBound = 1_000_000;
    private final ShopService shopService;

    public RoomComponent(ShopService shopService) {
        this.shopService = shopService;
    }

    public void createRoom(String userId, String shopId) {
        String roomId = generateRoomId();
        Shop shop = shopService.getShopById(shopId);
        Room room = new Room(roomId, userId, shop);
        rooms.put(roomId, room);
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public boolean joinRoom(String userId, String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            return false;
        }
        room.joinRoom(userId);
        return true;
    }

    private String generateRoomId() {
        if (rooms.size() >= roomBound) {
            throw new IllegalStateException("房间号已用完");
        }

        String roomId;
        do {
            roomId = String.format("%06d", random.nextInt(roomBound));
        } while (rooms.containsKey(roomId));
        return roomId;
    }
}
