package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.signals.shared.SharedValueSignal;
import github.axolotl.coorder.entity.Product;

import java.util.function.Consumer;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.signals.Signal;

public class ProductPurchaseLayout extends HorizontalLayout {

    private final Consumer<Integer> quantityChangeConsumer;
    private int quantity;
    private Button minusButton;
    private IntegerField quantityField;
    private IntegerField totalQuantityField;
    private SharedValueSignal<Integer> quantitiesInRoomSignal;

    public ProductPurchaseLayout(Product product,
                                 int initQuantity,
                                 SharedValueSignal<Integer> quantitiesInRoomSignal,
                                 Consumer<Integer> quantityChangeConsumer) {
        this.quantityChangeConsumer = quantityChangeConsumer;
        this.quantity = initQuantity;
        this.quantitiesInRoomSignal = quantitiesInRoomSignal;
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        setWidthFull();

        Span nameLabel = new Span(product.getNameAndDescription());
        Span priceLabel = new Span("￥" + product.getPriceWithMinPurchaseQuantity());
        priceLabel.getStyle().setColor("#FF4500");

        VerticalLayout productInfoLayout = new VerticalLayout(nameLabel, priceLabel);
        productInfoLayout.setPadding(false);
        HorizontalLayout quantityLayout = buildQuantityLayout(initQuantity);

        add(productInfoLayout, quantityLayout);
    }

    private HorizontalLayout buildQuantityLayout(int initQuantity) {
        HorizontalLayout quantityLayout = new HorizontalLayout();
        quantityLayout.setPadding(false);
        quantityLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        quantityLayout.setWidthFull();

        minusButton = new Button(new Icon(VaadinIcon.MINUS));
        minusButton.setAriaLabel("减少数量");
        minusButton.addClickListener(e -> updateQuantity(quantity - 1));


        quantityField = new IntegerField();
        quantityField.setValue(initQuantity);
        quantityField.setAriaLabel("当前数量");
        quantityField.setWidth("45px");
        quantityField.setMin(0);
        quantityField.addValueChangeListener(e -> {
            Integer newValue = e.getValue();
            if (newValue != null && newValue >= 0) {
                updateQuantity(newValue);
            } else {
                updateQuantity(0);
            }
        });


        totalQuantityField = new IntegerField();
        totalQuantityField.setValue(initQuantity);
        totalQuantityField.setAriaLabel("房间总数量");
        totalQuantityField.setWidth("45px");
        totalQuantityField.setEnabled(false);
        Signal.effect(this, () -> {
            Integer total = quantitiesInRoomSignal.get();
            totalQuantityField.setValue(total);
            setControlVisible(totalQuantityField, total > 0);
        });

        setQuantityControlsVisible(initQuantity > 0);

        Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
        plusButton.setAriaLabel("增加数量");
        plusButton.addClickListener(e -> updateQuantity(quantity + 1));

        quantityLayout.add(minusButton, quantityField, totalQuantityField, plusButton);
        return quantityLayout;
    }

    /**
     * 更新数量，并触发数量变化的回调
     *
     * @param newQuantity 新的数量
     */
    private void updateQuantity(int newQuantity) {
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        setQuantityControlsVisible(newQuantity > 0);
        quantity = newQuantity;
        if (quantityField.getValue() == null || quantityField.getValue() != newQuantity) {//避免重复触发valueChangeListener
            quantityField.setValue(newQuantity);
        }
        this.quantityChangeConsumer.accept(newQuantity);
    }

    private void setQuantityControlsVisible(boolean visible) {
        setControlVisible(minusButton, visible);
        setControlVisible(quantityField, visible);
    }

    private void setControlVisible(Component component, boolean visible) {
        component.getStyle().set("visibility", visible ? "visible" : "hidden");
        component.getStyle().set("pointer-events", visible ? "auto" : "none");
    }

}
