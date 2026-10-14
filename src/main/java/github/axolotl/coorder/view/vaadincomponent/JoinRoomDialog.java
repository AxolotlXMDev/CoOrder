package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class JoinRoomDialog extends Dialog {
    private TextField roomIdField;
    public JoinRoomDialog() {
        setWidth("300px");
        setHeight("150px");
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        FormLayout dialogLayout = createDialogLayout();
        add(dialogLayout);

        Button saveButton = createSaveButton(this);
        Button cancelButton = new Button("取消", e -> this.close());
        getFooter().add(cancelButton);
        getFooter().add(saveButton);
    }

    private FormLayout createDialogLayout() {
        roomIdField = new TextField("房间ID");
        roomIdField.setWidthFull();

        FormLayout dialogLayout = new FormLayout(roomIdField);
        dialogLayout.setAutoResponsive(false);
        dialogLayout.setExpandFields(true);

        return dialogLayout;
    }
    private Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("加入", e -> {
            dialog.close();
            String roomId = roomIdField.getValue();
            //TODO 加入房间逻辑
        });
        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        return saveButton;
    }

}
