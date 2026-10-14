package github.axolotl.coorder.view.vaadincomponent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import github.axolotl.coorder.entity.Product;
import github.axolotl.coorder.entity.ProductSection;
import github.axolotl.coorder.entity.Shop;

import java.util.List;

public class ShopInfoDialog extends Dialog {
    private ComponentRenderer<Component, Product> productRenderer = new ComponentRenderer<>(
            product -> {
                Div productDiv = new Div();
                productDiv.add(new H6(product.getNameAndDescription()));
                productDiv.add(new Text("价格: " + product.getPriceWithMinPurchaseQuantity()));
                return productDiv;
            });

    public ShopInfoDialog(Shop shop) {
        setWidth("400px");
        setHeight("80%");
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.TERTIARY);
        getHeader().add(closeButton);

        add(new H2("%s[%s]".formatted(shop.getName(), shop.getId())));
        add(shop.getDescription());

        List<ProductSection> sections = shop.getSections();
        sections.forEach(section -> {
            VerticalLayout sectionList = new VerticalLayout();
            for (Product product : section.getProducts()) {
                sectionList.add(productRenderer.createComponent(product));
            }
            sectionList.setSpacing(false);
            sectionList.setPadding(false);

            Details details = new Details(new H4(section.getName()), sectionList);
            details.setOpened(true);
            add(details);
        });
    }
}
