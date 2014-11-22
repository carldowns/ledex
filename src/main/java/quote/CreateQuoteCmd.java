package quote;

import catalog.Product;
import catalog.dao.CatalogPart;

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

    public void addLineItem (String quantity, Product product) {

        Quote.LineItem qItem = new Quote.LineItem();
        getQuote().items.add(qItem);

        Quote.QuoteProduct qProduct = new Quote.QuoteProduct();
        qItem.quotedProduct = qProduct;
        qItem.quantity = quantity;

        for (CatalogPart catalogPart : product.getParts()) {
            Quote.QuotePart quotePart = new Quote.QuotePart();
            quotePart.catalogPart = catalogPart;
            quotePart.quantity = product.getQuantityFor(catalogPart);
            qProduct.quotedParts.add(quotePart);
        }
    }

}
