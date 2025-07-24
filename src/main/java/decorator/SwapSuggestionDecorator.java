package decorator;

import classes.SwapSuggestion;

public abstract class SwapSuggestionDecorator extends SwapSuggestion {
    protected SwapSuggestion suggestion;

    public SwapSuggestionDecorator(SwapSuggestion suggestion) {
        super(suggestion.getOriginalFood(), suggestion.getRecommendedFood(),
              suggestion.getOriginalValue(), suggestion.getRecommendedValue());
        this.suggestion = suggestion;
    }

    public abstract String getDecoratedInfo();
}
