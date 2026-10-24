package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.ValueContext;
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

    public ProductPurchaseLayout(Product product,
                                 int initQuantity,
                                 SharedValueSignal<Integer> quantitiesInRoomSignal,
                                 Consumer<Integer> quantityChangeConsumer) {
        this.quantityChangeConsumer = quantityChangeConsumer;
        this.quantity = initQuantity;
        setSpacing(true);
        setPadding(false);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        setWidthFull();

        Span nameLabel = new Span(product.getName());
        nameLabel.setWidth("100px");

        HorizontalLayout quantityLayout = buildQuantityLayout(initQuantity);

        Span totalQuantitySpan = new Span();
        Signal.effect(this, () -> {
            Integer total = quantitiesInRoomSignal.get();
            String totalAmount = total == null ? "0" : String.valueOf(total);
            totalQuantitySpan.setText("全员总数: " + totalAmount);
        });

        add(new VerticalLayout(nameLabel, totalQuantitySpan), quantityLayout);
    }

    private HorizontalLayout buildQuantityLayout(int initQuantity) {
        HorizontalLayout quantityLayout = new HorizontalLayout();
        quantityLayout.setSpacing(true);
        quantityLayout.setPadding(false);
        quantityLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        quantityLayout.setWidthFull();

        minusButton = new Button("-");
        minusButton.setWidth("40px");
        minusButton.setAriaLabel("减少数量");
        minusButton.addClickListener(e -> updateQuantity(quantity - 1));


        quantityField = new IntegerField();
        quantityField.setValue(initQuantity);
        quantityField.setAriaLabel("当前数量");
        quantityField.setWidth("60px");
        quantityField.setMin(0);
        quantityField.addValueChangeListener(e -> {
            Integer newValue = e.getValue();
            if (newValue != null && newValue >= 0) {
                updateQuantity(newValue);
            } else {
                updateQuantity(0);
            }
        });

        setQuantityControlsVisible(initQuantity > 0);

        Button plusButton = new Button("+");
        plusButton.setWidth("40px");
        plusButton.setAriaLabel("增加数量");
        plusButton.addClickListener(e -> updateQuantity(quantity + 1));

        quantityLayout.add(minusButton, quantityField, plusButton);
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
            this.quantityChangeConsumer.accept(newQuantity);
        }
    }

    private void setQuantityControlsVisible(boolean visible) {
        setControlVisible(minusButton, visible);
        setControlVisible(quantityField, visible);
    }

    private void setControlVisible(Component component, boolean visible) {
        component.getStyle().set("visibility", visible ? "visible" : "hidden");
        component.getStyle().set("pointer-events", visible ? "auto" : "none");
        if (component instanceof HasEnabled hasEnabled) {
            hasEnabled.setEnabled(visible);
        }
    }

}
