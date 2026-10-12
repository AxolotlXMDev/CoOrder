package github.axolotl.coorder.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.ShopService;

@Route("admin")
public class AdminView extends VerticalLayout implements BeforeEnterObserver {

    private final ShopService shopService;

    public AdminView(ShopService shopService) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        this.shopService = shopService;

        if (!UserSession.isAdmin()) {
            return;
        }

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button refreshButton = new Button("重载Shops", VaadinIcon.REFRESH.create(), e -> {
            Notification.show("重载中", 1000, Notification.Position.TOP_END);
            shopService.reloadShopsByUser();
            Notification.show("重载成功，加载了 %d 个店铺".formatted(shopService.getShopCount()), 3000, Notification.Position.TOP_END);
        });

        horizontalLayout.add(refreshButton);
        add(horizontalLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UserSession.isAdmin()) {
            Notification.show("你没有权限访问此页面。", 3000, Notification.Position.TOP_END);
            event.rerouteTo(LoginView.class);
        }
    }
}
