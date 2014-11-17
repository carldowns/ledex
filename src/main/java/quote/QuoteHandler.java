package quote;


import catalog.Assembly;
import catalog.CatalogEngine.CandidateProblem;
import catalog.CatalogEngine.CandidateProduct;
import cmd.BaseCmd;

/**
 * interface for organizing handlers in a pipeline processing model.
 */
public interface QuoteHandler {

    public void evaluate(BaseCmd command);
}
