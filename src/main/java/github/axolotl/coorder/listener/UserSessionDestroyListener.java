package github.axolotl.coorder.listener;

import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import github.axolotl.coorder.service.UserSessionService;
import org.springframework.stereotype.Component;

@Component
public class UserSessionDestroyListener implements SessionDestroyListener {

    private final UserSessionService userSessionService;

    public UserSessionDestroyListener(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        userSessionService.destroySession(event.getSession().getSession().getId());
    }
}
