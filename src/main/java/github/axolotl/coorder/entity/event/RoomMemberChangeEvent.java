package github.axolotl.coorder.entity.event;

public record RoomMemberChangeEvent(RoomNumberChangeEventType eventType,
                                    String userId) {
    public static RoomMemberChangeEvent EMPTY = new RoomMemberChangeEvent(RoomNumberChangeEventType.EMPTY, null);
}
