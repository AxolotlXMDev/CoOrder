package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.shared.SharedNumberSignal;
import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.UserSession;
import github.axolotl.coorder.service.RoomService;
import github.axolotl.coorder.service.UserSessionService;
import org.jspecify.annotations.NonNull;

public class RoomMemberPopover extends Popover {
    private final Room room;
    private final String currentUserId;
    private final RoomService roomService;
    private final UserSessionService userSessionService;
    private final VerticalLayout statusLayout = new VerticalLayout();

    public RoomMemberPopover(RoomService roomService, UserSessionService userSessionService, Room room, String currentUserId) {
        super();
        this.room = room;
        this.currentUserId = currentUserId;
        this.roomService = roomService;
        this.userSessionService = userSessionService;
        setWidth("100%");
        setHeight("50%");
        addOpenedChangeListener(e -> reloadContent());

        SharedNumberSignal priceSignal = room.getRoomPriceSignal();
        Signal.effect(this, () -> {
            priceSignal.get();//金额有变化，更新一下这个页面
            //TODO 房间人数有变化时更新
            reloadContent();
        });
    }

    private void reloadContent() {
        this.statusLayout.removeAll();
        String ownerUserId = room.getOwnerUserId();
        room.getUserStatus().forEach(status -> {
            String userId = status.getUserId();
            UserSession userSession = userSessionService.getUserSession(status.getUserId());

            HorizontalLayout statusContainer = new HorizontalLayout();
            statusContainer.setWidthFull();
            statusContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            VerticalLayout numberInfoLayout = new VerticalLayout();
            {
                numberInfoLayout.setSpacing(false);
                numberInfoLayout.setPadding(false);

                Span userNameSpan = new Span();
                userNameSpan.setText(userSession.getName());

                Span priceSpan = new Span();
                priceSpan.getStyle().setColor("#FF4500");
                priceSpan.setText("￥%.2f".formatted(status.getTotalPriceSignal().peek()));

                numberInfoLayout.add(userNameSpan);
                numberInfoLayout.add(priceSpan);
            }
            Button leaveButton = getLeaveButton(ownerUserId, userId, userSession);
            statusContainer.add(numberInfoLayout, leaveButton);

            statusLayout.add(statusContainer);
        });


        add(statusLayout);
    }

    private @NonNull Button getLeaveButton(String ownerUserId, String userId, UserSession userSession) {
        Button leaveButton;
        if (ownerUserId.equals(userId)) {
            leaveButton = new Button("退出(销毁房间)");
            leaveButton.addClickListener(e -> createConfirmDialog(
                    "确认退出房间",
                    "您确定要退出房间吗？退出后房间将被销毁，所有用户将被踢出。",
                    confirmEvent -> {
                        roomService.leaveRoom(userId, room.getRoomId());
                        reloadContent();
                    }).open());
        } else {
            leaveButton = new Button("退出");
            leaveButton.addClickListener(e -> createConfirmDialog(
                    "确认踢出用户",
                    "您确定要将用户 %s 踢出房间吗？".formatted(userSession.getName()),
                    confirmEvent -> {
                        roomService.leaveRoom(userId, room.getRoomId());
                        reloadContent();
                    }).open());
        }
        leaveButton.setEnabled(currentUserId.equals(ownerUserId));//owner全开
        if (currentUserId.equals(userId)) {
            leaveButton.setEnabled(true);//自己的可以点击
        }
        return leaveButton;
    }

    private ConfirmDialog createConfirmDialog(String header, String text, ComponentEventListener<ConfirmDialog.ConfirmEvent> confirmListener) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(header);
        dialog.setText(text);

        dialog.setCloseOnEsc(true);
        dialog.setCancelable(true);
        dialog.setCancelText("取消");

        dialog.setConfirmButton("确认", confirmListener);
        return dialog;
    }


}
