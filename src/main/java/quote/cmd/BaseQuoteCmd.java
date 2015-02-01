package quote.cmd;

import cmd.AbstractBaseCmd;
import quote.Quote;

/**
 *
 */
public class BaseQuoteCmd extends AbstractBaseCmd {

    private Quote quote;

    public BaseQuoteCmd() {
        super();
        quote = new Quote();
    }

    public BaseQuoteCmd(Quote quote) {
        super();
        this.quote = quote;
    }

    public Quote getQuote() {
        return quote;
    }
}
