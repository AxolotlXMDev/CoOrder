package github.axolotl.coorder.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import github.axolotl.coorder.entity.Shop;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.RoomService;
import github.axolotl.coorder.service.ShopService;
import github.axolotl.coorder.service.UserSessionService;
import github.axolotl.coorder.view.vaadincomponent.JoinRoomDialog;
import github.axolotl.coorder.view.vaadincomponent.ShopInfoDialog;

import java.util.List;

@Route("/")
public class LobbyView extends VerticalLayout implements CheckBeforeEnterObserver {
    private final ShopService shopService;
    private final UserSessionService userSessionService;
    private final RoomService roomService;

    public LobbyView(ShopService shopService, UserSessionService userSessionService, RoomService roomService) {
        this.shopService = shopService;
        this.userSessionService = userSessionService;
        this.roomService = roomService;

        if (!canAccess()) {
            return;
        }

        setPadding(false);
        setSpacing(false);
        setWidthFull();
        setHeightFull();
        setAlignItems(Alignment.STRETCH);
        addClassNames(LumoUtility.Background.CONTRAST_5);
        getStyle().set("overflow", "auto");


        add(buildStickyBar(), buildShopSection());
    }

    private Div buildStickyBar() {
        Div bar = new Div();
        bar.setWidthFull();
        bar.addClassNames(LumoUtility.Background.BASE, LumoUtility.BoxShadow.SMALL);

        VerticalLayout inner = new VerticalLayout();
        inner.setWidthFull();
        inner.setPadding(true);
        inner.setSpacing(true);


        UserSession session = UserSession.get();
        H2 hello = new H2("Hi, " + session.getName());
        hello.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.XLARGE);

        Span roleBadge = new Span(String.valueOf(session.getRole()));
        roleBadge.addClassNames(
                LumoUtility.Background.PRIMARY_10,
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.Horizontal.SMALL,
                LumoUtility.FontSize.XSMALL,
                LumoUtility.FontWeight.BOLD);

        Button logout = new Button(VaadinIcon.SIGN_OUT.create(), e -> {
            UserSession.logout();
            UI.getCurrent().navigate(LoginView.class);
        });
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout topRow = new HorizontalLayout();
        topRow.setWidthFull();
        topRow.setAlignItems(FlexComponent.Alignment.CENTER);
        topRow.add(hello, roleBadge);

        Span spacer = new Span();
        topRow.add(spacer, logout);
        topRow.expand(spacer);

        Span sub = new Span("选一家店铺开始点餐，或加入好友的房间。");
        sub.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        Button joinRoom = new Button("加入房间", VaadinIcon.HOME.create(),
                e -> new JoinRoomDialog(roomService).open());
        joinRoom.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        joinRoom.setWidthFull();

        inner.add(topRow, sub, joinRoom);
        bar.add(inner);
        return bar;
    }

    private VerticalLayout buildShopSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setPadding(true);
        section.setSpacing(true);
        section.addClassNames(LumoUtility.Padding.Bottom.XLARGE);

        H3 heading = new H3("店铺列表");
        heading.addClassNames(LumoUtility.Margin.Top.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.FontSize.LARGE);
        section.add(heading);

        List<Shop> shops = shopService.getShops();
        if (shops.isEmpty()) {
            Span empty = new Span("暂时还没有可用店铺，稍后再来看看吧。");
            empty.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.TextAlignment.CENTER, LumoUtility.Padding.Top.LARGE);
            empty.setWidthFull();
            section.add(empty);
            return section;
        }

        shops.forEach(shop -> section.add(buildShopRow(shop)));
        return section;
    }

    private VerticalLayout buildShopRow(Shop shop) {
        VerticalLayout card = new VerticalLayout();
        card.setWidthFull();
        card.setPadding(true);
        card.setSpacing(false);
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.XSMALL,
                LumoUtility.Gap.SMALL
        );


        H3 name = new H3(shop.getName());
        name.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.LARGE);

        Span desc = new Span(shop.getDescription());
        desc.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        Button infoBtn = new Button("查看详情", e -> new ShopInfoDialog(shop).open());
        infoBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

        Button createBtn = new Button("创建房间", VaadinIcon.PLUS.create(), e -> {
            String roomId = roomService.createRoom(UserSession.get().getId(), shop.getId());
            roomService.joinRoom(UserSession.get().getId(), roomId);
            UI.getCurrent().navigate(PurchaseRoomView.class);
        });
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

        HorizontalLayout actions = new HorizontalLayout(infoBtn, createBtn);
        actions.setWidthFull();
        actions.setPadding(false);
        actions.setJustifyContentMode(JustifyContentMode.END);
        actions.addClassNames(LumoUtility.Margin.Top.SMALL);

        card.add(name, desc, actions);
        return card;
    }

    @Override
    public boolean canAccess() {
        return UserSession.isLoggedIn();
    }

    @Override
    public void doWithAccessDenied(BeforeEnterEvent event) {
        Notification.show("你还没有登录，请先登录。", 3000, Notification.Position.TOP_END);
        event.rerouteTo(LoginView.class);
    }

}