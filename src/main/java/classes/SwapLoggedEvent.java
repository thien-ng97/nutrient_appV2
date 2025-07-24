package classes;

public class SwapLoggedEvent {
    private final SwapSuggestion suggestion;
    private final String nutrientName;

    public SwapLoggedEvent(SwapSuggestion suggestion, String nutrientName) {
        this.suggestion = suggestion;
        this.nutrientName = nutrientName;
    }

    public SwapSuggestion getSuggestion() {
        return suggestion;
    }

    public String getNutrientName() {
        return nutrientName;
    }
}

