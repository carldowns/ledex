package quote;


/**
 * interface for organizing handlers in a pipeline processing model.
 */
public interface QuoteHandler {

    public void evaluate(BaseQuoteCmd command);
}
