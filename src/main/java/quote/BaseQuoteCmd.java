package quote;

import cmd.BaseCmd;
import quote.Quote;

/**
 *
 */
public class BaseQuoteCmd extends BaseCmd {

    private Quote quote;

    public BaseQuoteCmd() {
        super();
        quote = new Quote();
    }

    public BaseQuoteCmd(Quote quote) {
        super();
        this.quote = quote;
    }

    public Quote getQuote () {
        return quote;
    }
}
