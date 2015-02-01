package quote.cmd;

import quote.Quote;

/**
 *
 */
public class QuoteCreateCmd extends QuoteBaseCmd {


    public QuoteCreateCmd() {
        super();
    }

    public QuoteCreateCmd(Quote quote) {
        super(quote);
    }

    public void setProjectName (String name) {
        this.getQuote().projectName = name;
    }

    public void setCustomerID (String customerID) {
        this.getQuote().customerID = customerID;
    }

}
