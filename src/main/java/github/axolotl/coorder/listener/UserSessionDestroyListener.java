package github.axolotl.coorder.listener;

import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import github.axolotl.coorder.service.RoomService;
import github.axolotl.coorder.service.UserSessionService;
import org.springframework.stereotype.Component;

@Component
public class UserSessionDestroyListener implements SessionDestroyListener {

    private final UserSessionService userSessionService;
    private final RoomService roomService;

    public UserSessionDestroyListener(UserSessionService userSessionService, RoomService roomService) {
        this.userSessionService = userSessionService;
        this.roomService = roomService;
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        String userId = event.getSession().getSession().getId();
        userSessionService.destroySession(userId);
        roomService.destroyUser(userId);
    }
}
