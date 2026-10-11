package github.axolotl.coorder.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import github.axolotl.coorder.entity.UserSession;

@Route("/")
public class LobbyView extends VerticalLayout implements BeforeEnterObserver {
    public LobbyView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UserSession.isLoggedIn()) {
            Notification.show("你还没有登录，请先登录。", 3000,
                    Notification.Position.TOP_END);
            event.forwardTo(LoginView.class);
            return;
        }

        add("%s[%s]，欢迎登录！ ".formatted(UserSession.get().getName(), UserSession.get().getRole()));
    }
}