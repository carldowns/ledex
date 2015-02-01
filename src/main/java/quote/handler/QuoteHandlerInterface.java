package quote.handler;


import quote.cmd.QuoteBaseCmd;

/**
 * interface for organizing handlers in a pipeline processing model.
 */
public interface QuoteHandlerInterface {

    public void evaluate(QuoteBaseCmd command);
}
