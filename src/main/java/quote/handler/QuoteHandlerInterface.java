package quote.handler;


import quote.cmd.BaseQuoteCmd;

/**
 * interface for organizing handlers in a pipeline processing model.
 */
public interface QuoteHandlerInterface {

    public void evaluate(BaseQuoteCmd command);
}
