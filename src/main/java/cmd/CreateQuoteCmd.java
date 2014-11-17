package cmd;

import cmd.BaseCmd;
import quote.Quote;

/**
 *
 */
public class CreateQuoteCmd extends BaseCmd {

    private Quote quote;

    public CreateQuoteCmd() {
        super();
        quote = new Quote();
    }

    public CreateQuoteCmd(Quote quote) {
        super();
        this.quote = quote;
    }

    public Quote getQuote () {
        return quote;
    }
}
