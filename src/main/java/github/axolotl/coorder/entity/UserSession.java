package github.axolotl.coorder.entity;

import com.vaadin.flow.server.VaadinSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSession {
    public enum Role {ADMIN, GUEST}

    private String id;
    private String name;
    private Role role;

    public static UserSession get() {
        return (UserSession) VaadinSession.getCurrent().getAttribute("USER_SESSION");
    }

    public static void set(UserSession session) {
        VaadinSession.getCurrent().setAttribute("USER_SESSION", session);
    }

    public static boolean isLoggedIn() {
        return get() != null;
    }

    public static boolean isAdmin() {
        UserSession session = get();
        return session != null && session.getRole() == Role.ADMIN;
    }

    public static void login(String username, Role role) {
        UserSession session = new UserSession(
                String.valueOf(VaadinSession.getCurrent().getNextUIid()), username, role);
        set(session);
    }
}