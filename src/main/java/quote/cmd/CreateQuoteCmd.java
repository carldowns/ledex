package quote.cmd;

import quote.Quote;

/**
 *
 */
public class CreateQuoteCmd extends BaseQuoteCmd {


    public CreateQuoteCmd() {
        super();
    }

    public CreateQuoteCmd(Quote quote) {
        super(quote);
    }

    public void setProjectName (String name) {
        this.getQuote().projectName = name;
    }

    public void setCustomerID (String customerID) {
        this.getQuote().customerID = customerID;
    }

}
