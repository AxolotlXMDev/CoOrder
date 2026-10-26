package github.axolotl.coorder.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.shared.SharedNumberSignal;
import com.vaadin.flow.signals.shared.SharedValueSignal;
import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.Shop;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.entity.event.RoomMemberChangeEvent;
import github.axolotl.coorder.service.RoomService;
import github.axolotl.coorder.service.UserSessionService;
import github.axolotl.coorder.view.vaadincomponent.PriceInfoPopover;
import github.axolotl.coorder.view.vaadincomponent.ProductPurchaseLayout;
import github.axolotl.coorder.view.vaadincomponent.RoomMemberPopover;

@Route("purchase-room")
public class PurchaseRoomView extends VerticalLayout implements CheckBeforeEnterObserver {
    private final RoomService roomService;
    private final UserSessionService userSessionService;
    private String userId;
    private Room room;
    private String roomId;

    public PurchaseRoomView(RoomService roomService, UserSessionService userSessionService) {
        this.roomService = roomService;
        this.userSessionService = userSessionService;

        if (!canAccess()) {
            return;
        }

        room = roomService.getRoom(roomId);

        SharedValueSignal<RoomMemberChangeEvent> roomMemberEventSignal = room.getRoomMemberEventSignal();
        Signal.effect(this, () -> {
            roomMemberEventSignal.get();
            if (!room.hasUser(userId)) {
                UI.getCurrent().navigate(LobbyView.class);
            }
        });

        setPadding(false);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TabSheet contentLayout = buildSectionTabSheet();
        HorizontalLayout buttonLayout = buildBottomLayout();

        add(contentLayout);
        add(buttonLayout);
        //TODO 购物车UI+总价显示
    }

    public HorizontalLayout buildBottomLayout() {
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        bottomLayout.setAlignItems(Alignment.CENTER);
        bottomLayout.setSpacing(false);
        bottomLayout.setPadding(false);

        VerticalLayout homeLayout = new VerticalLayout();
        {
            homeLayout.setSpacing(false);

            Span homeNumberSpan = new Span("房间号: %s".formatted(room.getRoomId()));
            homeNumberSpan.getStyle().setFontSize("16px");
            Span shopNameSpan = new Span("%s[%s]".formatted(room.getShop().getName(), room.getShop().getId()));
            homeLayout.add(homeNumberSpan);
            homeLayout.add(shopNameSpan);

            RoomMemberPopover popover = new RoomMemberPopover(roomService, userSessionService, room, UserSession.get().getId());
            popover.setPosition(PopoverPosition.TOP);
            popover.setTarget(homeLayout);

        }
        VerticalLayout priceLayout = new VerticalLayout();
        {
            priceLayout.setSpacing(false);
            priceLayout.setPadding(false);

            SharedNumberSignal totalPriceSignal = room.getRoomPriceSignal();
            SharedNumberSignal userPriceSignal = room.getUserPriceSignal(UserSession.get().getId());

            Span userPriceSpan = new Span("￥0");
            userPriceSpan.getStyle().setColor("#FF4500");
            userPriceSpan.getStyle().setFontSize("24px");


            Span totalPriceSpan = new Span("￥0");
            totalPriceSpan.getStyle().setColor("#FF6A33");
            totalPriceSpan.getStyle().setFontSize("16px");


            Signal.effect(this, () -> {
                userPriceSpan.setText("￥%.2f".formatted(userPriceSignal.get()));
                totalPriceSpan.setText("房间总价: ￥%.2f".formatted(totalPriceSignal.get()));
            });
            priceLayout.add(userPriceSpan);
            priceLayout.add(totalPriceSpan);

            PriceInfoPopover popover = new PriceInfoPopover(room);
            popover.setPosition(PopoverPosition.TOP);
            popover.setTarget(priceLayout);
        }
        bottomLayout.add(homeLayout, priceLayout);


        return bottomLayout;
    }


    public TabSheet buildSectionTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_NO_PADDING);
        tabSheet.setHeightFull();
        tabSheet.setWidthFull();
        Shop shop = room.getShop();
        shop.getSections().forEach(section -> {
            VerticalLayout productLayouts = new VerticalLayout();
            Scroller scroller = new Scroller(productLayouts);
            scroller.setHeightFull();

            section.getProducts().stream()
                    .map(product -> ProductPurchaseLayout.forRoom(room, product))
                    .forEach(productLayouts::add);
            tabSheet.add(section.getName(), scroller);
        });
        return tabSheet;
    }

    @Override
    public boolean canAccess() {
        if (!UserSession.isLoggedIn()) return false;
        userId = UserSession.get().getId();
        return (roomId = roomService.findRoomWithUser(userId)) != null;
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
