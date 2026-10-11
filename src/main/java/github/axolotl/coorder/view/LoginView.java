package github.axolotl.coorder.view;

import com.vaadin.flow.component.login.LoginForm;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.AdminService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.Component;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView(AdminService adminService) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "linear-gradient(160deg, #f7f9fc 0%, #eaeff8 100%)");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("380px");
        card.setMaxWidth("92vw");
        card.setPadding(false);
        card.setSpacing(true);
        card.getStyle()
                .set("background", "#ffffff")
                .set("border-radius", "18px")
                .set("box-shadow", "0 10px 40px rgba(24, 39, 75, 0.10)")
                .set("padding", "40px 36px")
                .set("box-sizing", "border-box")
                .set("gap", "16px");

        H2 title = new H2("欢迎进入");
        title.getStyle()
                .set("margin", "0")
                .set("font-size", "1.55rem")
                .set("font-weight", "600")
                .set("color", "#1b2430")
                .set("letter-spacing", "0.5px");
        Paragraph description = new Paragraph("输入用户名，开始你的体验");
        description.getStyle()
                .set("margin", "0 0 10px")
                .set("color", "#8a94a6")
                .set("font-size", "0.9rem")
                .set("line-height", "1.5");

        TextField usernameField = new TextField("用户名");
        usernameField.setWidthFull();
        usernameField.setPlaceholder("请输入用户名");
        usernameField.setMaxLength(30);
        usernameField.setAutofocus(true);

        PasswordField passwordField = new PasswordField("密码");
        passwordField.setWidthFull();
        passwordField.setPlaceholder("请输入密码（仅管理员需要）");
        passwordField.setMaxLength(30);

        Button submit = new Button("登录");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        submit.setWidthFull();
        submit.getStyle()
                .set("margin-top", "6px")
                .set("border-radius", "10px")
                .set("font-weight", "600")
                .set("letter-spacing", "0.5px")
                .set("cursor", "pointer")
                .set("--lumo-primary-color", "#3b6ef5")
                .set("--lumo-primary-color-50pct", "rgba(59, 110, 245, 0.5)")
                .set("--lumo-primary-color-10pct", "rgba(59, 110, 245, 0.1)");

        submit.addClickListener(event -> {
            String username = usernameField.getValue().trim();

            if (username.isEmpty()) {
                usernameField.setInvalid(true);
                usernameField.setErrorMessage("请输入用户名");
                usernameField.focus();
                return;
            }

            if (username.equals(adminService.getAdminUsername())) {
                if (adminService.verify(username, passwordField.getValue())) {
                    UserSession.login(username, UserSession.Role.ADMIN);
                    UI.getCurrent().navigate(AdminView.class);
                    return;
                }
                passwordField.setInvalid(true);
                passwordField.setErrorMessage("密码错误");
                passwordField.focus();
                return;
            }

            UserSession.login(username, UserSession.Role.GUEST);
            UI.getCurrent().navigate(LobbyView.class);
        });

        usernameField.addValueChangeListener(event -> usernameField.setInvalid(false));
        passwordField.addValueChangeListener(event -> passwordField.setInvalid(false));
        submit.addClickShortcut(Key.ENTER);

        card.add(title, description, usernameField, passwordField, submit);
        add(card);
    }

}