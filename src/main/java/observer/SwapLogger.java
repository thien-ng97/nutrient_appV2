package observer;

import classes.SwapLoggedEvent;

public class SwapLogger implements SwapEventListener {
    @Override
    public void onSwapLogged(SwapLoggedEvent event) {
        System.out.println("Swap Logged:");
        System.out.println(" - Original: " + event.getSuggestion().getOriginalFood());
        System.out.println(" - Suggested: " + event.getSuggestion().getRecommendedFood());
        System.out.println(" - Nutrient: " + event.getNutrientName());
        System.out.println(" - Before: " + event.getSuggestion().getOriginalValue());
        System.out.println(" - After: " + event.getSuggestion().getRecommendedValue());
    }
}

