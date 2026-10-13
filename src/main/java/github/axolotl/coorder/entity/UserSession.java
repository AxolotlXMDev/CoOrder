package github.axolotl.coorder.entity;

import com.vaadin.flow.server.VaadinSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class UserSession {
    public enum Role {ADMIN, GUEST}

    private final String id;
    private final String name;
    private final Role role;

    public UserSession(String id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static UserSession get() {
        return (UserSession) VaadinSession.getCurrent().getAttribute("USER_SESSION");
    }

    public static void set(UserSession session) {
        VaadinSession.getCurrent().setAttribute("USER_SESSION", session);
    }

    public static boolean isLoggedIn() {
        return UserSession.get() != null;
    }

    public static boolean isAdmin() {
        UserSession session = UserSession.get();
        return session != null && session.getRole() == UserSession.Role.ADMIN;
    }

    public static void logout() {
        VaadinSession.getCurrent().setAttribute("USER_SESSION", null);
    }


}