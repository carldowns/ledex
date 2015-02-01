package quote.cmd;

import cmd.AbstractBaseCmd;
import quote.Quote;

/**
 *
 */
public class QuoteBaseCmd extends AbstractBaseCmd {

    private Quote quote;

    public QuoteBaseCmd() {
        super();
        quote = new Quote();
    }

    public QuoteBaseCmd(Quote quote) {
        super();
        this.quote = quote;
    }

    public Quote getQuote() {
        return quote;
    }
}
