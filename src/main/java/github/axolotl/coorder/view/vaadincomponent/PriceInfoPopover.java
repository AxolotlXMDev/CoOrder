package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.shared.SharedValueSignal;
import github.axolotl.coorder.entity.Product;
import github.axolotl.coorder.entity.Room;
import github.axolotl.coorder.entity.event.UserPurchaseUpdateEvent;

import java.util.List;

/**
 * 购物车
 */
public class PriceInfoPopover extends Popover {
    private final Room room;
    private final VerticalLayout contentLayout = new VerticalLayout();

    public PriceInfoPopover(Room room) {
        super();
        this.room = room;
        setWidth("100%");
        setHeight("80%");

        contentLayout.setWidthFull();
        contentLayout.setHeightFull();
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);

        Scroller scroller = new Scroller(contentLayout);
        scroller.setHeightFull();

        add(scroller);

        SharedValueSignal<UserPurchaseUpdateEvent> purchaseUpdateEventSignal = room.getPurchaseUpdateEventSignal();
        Signal.effect(this, () -> {
            purchaseUpdateEventSignal.get();
            reloadContent();
        });
    }

    private void reloadContent() {
        contentLayout.removeAll();
        List<Product> purchasedProducts = room.getShop().getAllProducts();
        purchasedProducts.forEach(product -> {
            if (room.getTotalQuantitySignal(product.getId()).peek() > 0)
                contentLayout.add(ProductPurchaseLayout.forRoom(room, product));
        });
    }

}
