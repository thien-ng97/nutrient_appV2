package decorator;

public abstract class SwapDecorator implements SwapSuggestionComponent {
    protected final SwapSuggestionComponent wrapped;

    public SwapDecorator(SwapSuggestionComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getDetails() {
        return wrapped.getDetails();
    }
}