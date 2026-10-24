package github.axolotl.coorder.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.shared.SharedNumberSignal;
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

        VerticalLayout titleLayout = buildTitleLayout();
        titleLayout.setWidthFull();

        TabSheet contentLayout = buildSectionTabSheet();
        contentLayout.setHeightFull();
        contentLayout.setWidthFull();

        add(titleLayout);
        add(contentLayout);
        //TODO 购物车UI+总价显示
    }

    public VerticalLayout buildTitleLayout() {
        VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.setSpacing(false);
        titleLayout.setPadding(false);
        UserSession owner = userSessionService.getUserSession(room.getOwnerUserId());

        titleLayout.add(new Span("%s[%s](所有者: %s)".formatted(
                room.getShop().getName(),
                room.getRoomId(),
                owner.getName()
        )));

        SharedNumberSignal totalPriceSignal = room.getRoomPriceSignal();
        SharedNumberSignal userPriceSignal = room.getUserPriceSignal(UserSession.get().getId());

        Span priceSpan = new Span("");
        Signal.effect(this, () ->
                priceSpan.setText(getPriceText(totalPriceSignal.get(), userPriceSignal.get())));
        titleLayout.add(priceSpan);


        return titleLayout;
    }

    private String getPriceText(double totalPrice, double userPrice) {
        return "个人总价: %.2f元, 房间总价: %.2f元".formatted(userPrice, totalPrice);
    }

    public TabSheet buildSectionTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_NO_PADDING);
        tabSheet.setHeightFull();
        tabSheet.setWidthFull();
        Shop shop = room.getShop();
        shop.getSections().forEach(section -> {
            VerticalLayout productLayouts = new VerticalLayout();
            productLayouts.setPadding(false);
            productLayouts.setSpacing(false);
            Scroller scroller = new Scroller(productLayouts);
            scroller.setHeightFull();

            section.getProducts().stream()
                    .map(this::buildProductPurchaseLayout)
                    .forEach(productLayouts::add);
            tabSheet.add(section.getName(), scroller);
        });
        return tabSheet;
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
