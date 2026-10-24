package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.RoomService;
import github.axolotl.coorder.view.PurchaseRoomView;

public class JoinRoomDialog extends Dialog {
    private TextField roomIdField;
    private RoomService roomService;

    public JoinRoomDialog(RoomService roomService) {
        this.roomService = roomService;
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
        //TODO 下拉框横或者直接点击加入
        Button saveButton = new Button("加入", e -> {
            dialog.close();
            String userId = UserSession.get().getId();
            String roomId = roomIdField.getValue();
            boolean joinSucceed = roomService.joinRoom(userId, roomId);
            if (joinSucceed) {
                UI.getCurrent().navigate(PurchaseRoomView.class);
            } else {
                Notification.show("无效的房间号", 3000, Notification.Position.TOP_END);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.PRIMARY);
        return saveButton;
    }

}
