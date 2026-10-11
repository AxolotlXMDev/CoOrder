package github.axolotl.coorder.view.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class JoinRoomDialog extends Dialog {
    public JoinRoomDialog() {
        setWidth("400px");
        setHeight("300px");
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.TERTIARY);
        getHeader().add(closeButton);

        TextField roomIdField = new TextField("房间ID");
        roomIdField.setWidthFull();
        roomIdField.setAutofocus(true);

        Button joinButton = new Button("加入房间");
        joinButton.setWidthFull();
        joinButton.addClickShortcut(Key.ENTER);
        joinButton.addClickListener(event -> {
            String roomId = roomIdField.getValue();
            //TODO 加入房间的逻辑
        });
        joinButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(new VerticalLayout(roomIdField, joinButton));
    }
}
