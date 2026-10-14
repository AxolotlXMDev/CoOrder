package github.axolotl.coorder.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.RoomService;

@Route("purchase-room")
public class PurchaseRoomView extends VerticalLayout implements CheckBeforeEnterObserver {
    private final RoomService roomService;
    private Room room;
    private String getRoomId;


    public PurchaseRoomView(RoomService roomService) {
        this.roomService = roomService;

        if (!canAccess()) {
            return;
        }

        room = roomService.getRoom(getRoomId);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(buildTitleLayout());
        add(buildContent());
    }

    public HorizontalLayout buildTitleLayout() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setAlignItems(Alignment.CENTER);
        //TODO: 添加标题UI(店铺名称，房间id，个人总价，集体总价)
        return titleLayout;
    }

    public VerticalLayout buildContent() {
        VerticalLayout content = new VerticalLayout();
        //TODO: 商品加购UI
        return content;
    }

    @Override
    public boolean canAccess() {
        getRoomId = roomService.findRoomWithUser(UserSession.get().getId());
        return UserSession.isLoggedIn() &&
                getRoomId != null;
    }

    @Override
    public void doWithAccessDenied(BeforeEnterEvent event) {
        Notification.show("你还没加入房间。", 3000, Notification.Position.TOP_END);
        event.rerouteTo(LoginView.class);
    }
}
