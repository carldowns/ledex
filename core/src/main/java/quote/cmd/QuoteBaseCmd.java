package quote.cmd;

import cmd.Cmd;
import quote.Quote;

/**
 *
 */
public class QuoteBaseCmd extends Cmd {

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
