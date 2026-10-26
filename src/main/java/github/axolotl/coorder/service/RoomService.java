package github.axolotl.coorder.service;

import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.Shop;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final int roomBound = 1_000_000;
    private final ShopService shopService;

    public RoomService(ShopService shopService) {
        this.shopService = shopService;
    }

    public String createRoom(String userId, String shopId) {
        String roomId = generateRoomId();
        Shop shop = shopService.getShopById(shopId);
        Room room = new Room(roomId, userId, shop);
        rooms.put(roomId, room);
        return roomId;
    }

    public String findRoomWithUser(String userId) {
        return rooms.values().stream()
                .filter(r -> r.hasUser(userId))
                .map(Room::getRoomId)
                .findFirst().orElse(null);
    }

    public boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public boolean joinRoom(String userId, String roomId) {
        if (findRoomWithUser(userId) != null) {
            destroyUser(userId);
        }
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

    public void leaveRoom(String userId, String roomId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            if (room.getOwnerUserId().equals(userId)) {
                //房主离开->销毁房间
                room.destroy();
                rooms.remove(roomId);
            }else{
                room.removeUser(userId);
            }
        }
    }

    public void destroyUser(String userId) {
        for (Room room : new ArrayList<>(rooms.values())) {//避免ConcurrentModificationException
            if (room.hasUser(userId)) {
                leaveRoom(userId, room.getRoomId());
            }
        }
    }
}
