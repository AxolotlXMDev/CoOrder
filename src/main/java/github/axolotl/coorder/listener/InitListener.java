package github.axolotl.coorder.listener;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class InitListener implements VaadinServiceInitListener {

    private final UserSessionDestroyListener userSessionDestroyListener;

    public InitListener(UserSessionDestroyListener userSessionDestroyListener) {
        this.userSessionDestroyListener = userSessionDestroyListener;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionDestroyListener(userSessionDestroyListener);
    }
}
