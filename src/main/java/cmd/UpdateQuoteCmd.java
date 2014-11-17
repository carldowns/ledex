package cmd;

import cmd.BaseCmd;
import quote.Quote;

/**
 *
 */
public class UpdateQuoteCmd extends BaseCmd {

    private Quote quote;

    public UpdateQuoteCmd(Quote quote) {
        super();
        this.quote = quote;
    }

    public Quote getQuote () {
        return quote;
    }
}
