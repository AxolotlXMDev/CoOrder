package github.axolotl.coorder.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import github.axolotl.coorder.entity.Product;
import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.Shop;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.RoomService;
import github.axolotl.coorder.service.UserSessionService;
import github.axolotl.coorder.view.vaadincomponent.ProductPurchaseLayout;

@Route("purchase-room")
public class PurchaseRoomView extends VerticalLayout implements CheckBeforeEnterObserver {
    private final RoomService roomService;
    private final UserSessionService userSessionService;
    private Room room;
    private String roomId;


    public PurchaseRoomView(RoomService roomService, UserSessionService userSessionService) {
        this.roomService = roomService;
        this.userSessionService = userSessionService;

        if (!canAccess()) {
            return;
        }

        roomId = roomService.findRoomWithUser(UserSession.get().getId());
        room = roomService.getRoom(roomId);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout titleLayout = buildTitleLayout();
        titleLayout.setHeight("10%");
        titleLayout.setWidthFull();

        VerticalLayout contentLayout = buildContentLayout();
        contentLayout.setHeightFull();
        contentLayout.setWidthFull();

        add(titleLayout);
        add(contentLayout);
    }

    public HorizontalLayout buildTitleLayout() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setAlignItems(Alignment.CENTER);
        //TODO: 添加标题UI(店铺名称，房间id，个人总价，集体总价)
        UserSession owner = userSessionService.getUserSession(room.getOwnerUserId());
        titleLayout.add(new Text("房间: %s(所有者: %s)".formatted(room.getRoomId(), owner.getName())));
        return titleLayout;
    }

    public VerticalLayout buildContentLayout() {
        VerticalLayout content = new VerticalLayout();
        //TODO: 商品加购UI
        Shop shop = room.getShop();
        shop.getSections().forEach(section -> {
            content.add(new H2(section.getName()));
            section.getProducts().forEach(product -> {
                content.add(buildProductPurchaseLayout(product));
            });
        });
        return content;
    }

    private Component buildProductPurchaseLayout(Product product) {
        String userId = UserSession.get().getId();
        String productId = product.getId();
        return new ProductPurchaseLayout(
                product,
                room.getProductQuantityByUser(userId, productId),
                room.getTotalQuantitySignal(productId),
                quantity -> room.updateQuantity(userId, productId, quantity)
        );
    }


    @Override
    public boolean canAccess() {
        if (!UserSession.isLoggedIn()) return false;
        return roomService.findRoomWithUser(UserSession.get().getId()) != null;
    }

    @Override
    public void doWithAccessDenied(BeforeEnterEvent event) {
        if (!UserSession.isLoggedIn()) {
            Notification.show("请先登录。", 3000, Notification.Position.TOP_END);
            event.rerouteTo(LoginView.class);
        } else {
            Notification.show("你还没加入房间。", 3000, Notification.Position.TOP_END);
            event.rerouteTo(LobbyView.class);
        }
    }
}
