package observer;

import classes.SwapLoggedEvent;

public interface SwapEventListener {
    void onSwapLogged(SwapLoggedEvent event);
}
