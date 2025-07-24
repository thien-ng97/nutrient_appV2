package decorator;

import classes.SwapSuggestion;


// the base component (wraps the original suggestion)


public class BasicSwapSuggestion implements SwapSuggestionComponent {
    private final SwapSuggestion suggestion;

    public BasicSwapSuggestion(SwapSuggestion suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String getDetails() {
        return "Swap from " + suggestion.getOriginalFood() + " to " + suggestion.getRecommendedFood();
    }

    public SwapSuggestion getSuggestion() {
        return suggestion;
    }
}