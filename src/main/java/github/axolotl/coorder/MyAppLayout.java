package github.axolotl.coorder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import github.axolotl.coorder.view.AdminView;
import github.axolotl.coorder.view.LobbyView;
import github.axolotl.coorder.view.LoginView;
import github.axolotl.coorder.view.PurchaseRoomView;

@Layout
public class MyAppLayout extends AppLayout {

    public MyAppLayout() {
        getStyle().set("--vaadin-app-layout-drawer-width", "16em");

        SideNav nav = createSideNav();
        nav.setWidth("100%");

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        scroller.setHeight("100%");

        addToNavbar(getDrawerToggle());
        addToDrawer(scroller);


    }

    public static HorizontalLayout getDrawerToggle() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().setHeight("var(--lumo-size-s)").setWidth("var(--lumo-size-s)");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.add(drawerToggle);
        horizontalLayout.add(new Text("CoOrder"));
        return horizontalLayout;
    }

    private static SideNav createSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem("主页", LobbyView.class, VaadinIcon.DASHBOARD.create()),
                new SideNavItem("管理员配置", AdminView.class, VaadinIcon.COG.create()),
                new SideNavItem("下单页", PurchaseRoomView.class, VaadinIcon.COG.create()),
                new SideNavItem("登录", LoginView.class, VaadinIcon.USER.create()));
        return nav;
    }

}
