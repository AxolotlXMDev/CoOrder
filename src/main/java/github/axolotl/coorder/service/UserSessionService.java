package github.axolotl.coorder.service;

import com.vaadin.flow.server.VaadinSession;
import github.axolotl.coorder.entity.UserSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserSessionService {
    private final HashMap<String, UserSession> userSessions = new HashMap<>();

    public void login(String username, UserSession.Role role) {
        UserSession session = new UserSession(
                String.valueOf(VaadinSession.getCurrent().getSession().getId()), username, role);
        UserSession.set(session);
    }

    private void registerUserSession(UserSession userSession) {
        userSessions.put(userSession.getId(), userSession);
    }

    public void destroySession(String sessionId) {
        userSessions.remove(sessionId);
        //TODO: 在Room中清理
    }

}

