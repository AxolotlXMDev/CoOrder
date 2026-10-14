package github.axolotl.coorder.view;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public interface CheckBeforeEnterObserver extends BeforeEnterObserver {

    boolean canAccess();

    default void doWithAccessDenied(BeforeEnterEvent event) {
    }

    default void doWithAccessGranted(BeforeEnterEvent event) {
    }

    @Override
    default void beforeEnter(com.vaadin.flow.router.BeforeEnterEvent event) {
        if (canAccess()) {
            doWithAccessGranted(event);
        } else {
            doWithAccessDenied(event);
        }
    }
}
