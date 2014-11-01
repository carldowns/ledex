package mgr;

import catalog.*;
import ch.qos.logback.classic.Logger;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;
import part.PartRec;
import product.Product;
import product.ProductPart;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class AssemblyMgr {

    CatalogMgr catalogMgr;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssemblyMgr.class);

    public AssemblyMgr (CatalogMgr catalogMgr) {
        this.catalogMgr = catalogMgr;
    }

    public void buildCatalog () {

        try {
            for (Assembly assembly : catalogMgr.getAllAssemblies()) {
                List<CandidateProduct> products = buildProductCandidates(assembly);

                for (CandidateProduct candidate : products) {
                    Product product = new Product();

                    for (Map.Entry<FunctionType,CandidatePart> set : candidate.candidateParts.entrySet()) {
                        FunctionType function = set.getKey();
                        CandidatePart part = set.getValue();

                        checkArgument(function.equals(part.getPart().getFunctionName()), "function mismatch %s", function);

                        ProductPart pPart = new ProductPart();

                        pPart.setProductID(null);
                        pPart.setFunction(function);

                        pPart.setAssemblyID(assembly.getAssemblyID());
                        pPart.setAssemblyDocID(null);

                        pPart.setPartID(part.getPart().getPartId());
                        pPart.setPartDocID(null);

                        product.addPart(pPart);
                    }
                    catalogMgr.updateProduct(assembly, product);
                }
            }
        }
        catch (Exception e) {
            logger.error("unable to build catalog", e);
        }
    }

    public List<CandidateProduct> buildProductCandidates(Assembly assembly) {

        // build all candidate combinations (all permutations)
        // pass initial compatibility checks
        // eliminate parts that are incompatible
        // add passed candidates as Products

        CandidateBuilder builder = new CandidateBuilder();
        builder.setFunctions(assembly.getFunctionTypes());

        // TODO: change to Part object,
        // TODO: get only those that match the set of Functions present in the Assembly (use IN clause)
        Iterator<PartRec> parts = catalogMgr.getAllPartRecs();

        while (parts.hasNext()) {
            builder.addCandidate(parts.next());
        }

        RuleEngine engine = new RuleEngine();
        List<CandidateProduct> products = Lists.newArrayList();
        while (!builder.isAllPermutationsReported()) {

            CandidateProduct candidate = builder.nextCandidate();
            CandidateProblem report = engine.evaluate(assembly, candidate);
            if (report.hasProblems()) {
                builder.eliminateParts(report);
                continue;
            }

            products.add(candidate);
        }

        return products;
    }

    public static class CandidateProduct {
        Map<FunctionType,CandidatePart> candidateParts = Maps.newHashMap();

        public void put (FunctionType f, CandidatePart p) {
            candidateParts.put(f, p);
        }

        public Map<FunctionType, CandidatePart> getCandidateParts() {
            return candidateParts;
        }

        public void setCandidateParts(Map<FunctionType, CandidatePart> candidateParts) {
            this.candidateParts = candidateParts;
        }
    }

    /**
     *
     */
    public static class CandidateBuilder {
        CandidateTable table = new CandidateTable();
        boolean allPermutationsReported = false;

        void setFunctions (List<FunctionType> functions) {
            table.setFunctions(functions);
        }

        void addCandidate (PartRec part) {
            table.addCandidate (part);
        }

        boolean isAllPermutationsReported () {
            return allPermutationsReported;
        }

        CandidateProduct nextCandidate () {

            if (isAllPermutationsReported()) {
                return null;
            }

            CandidateProduct candidate = new CandidateProduct();
            allPermutationsReported = table.nextCandidate(candidate);
            return candidate;
        }

        void eliminateParts (CandidateProblem problems) {
            for (CandidatePart candidatePart : problems.problems.values()) {
                candidatePart.setViable(false);
            }
        }
    }

    /**
     *
     */
    public static class CandidateTable {
        // indices indicate the 'next' combination to return
        LinkedHashMap<FunctionType,Integer> indices = Maps.newLinkedHashMap();

        // linkage preserves order-of-insertion support
        LinkedHashMap<FunctionType,CandidateColumn> columns = Maps.newLinkedHashMap();

        void setFunctions (List<FunctionType> functions) {
            for (FunctionType functionType : functions) {
                columns.put(functionType, new CandidateColumn(functionType));
                indices.put(functionType, 0);
            }
        }

        void addCandidate (PartRec part) {
            CandidateColumn column = columns.get(part.getFunctionType());
            column.addPart(part);
        }

        boolean nextCandidate (CandidateProduct candidate) {
            for (CandidateColumn column : columns.values()) {
                column.nextCandidate(candidate, indices);
            }
            return advanceIndices();
        }

        boolean advanceIndices() {

            // starting with left-most column, try to increase
            // the column's index.  If cannot, reset it to 0
            // and move to next column.

            for (CandidateColumn column : columns.values()) {
                int colIndex = indices.get(column.functionType);
                int newColIndex = column.getNextAvailableIndex(colIndex);

                if (newColIndex != -1) {
                    indices.put(column.functionType, newColIndex);
                    return false;
                } else {
                    indices.put(column.functionType, 0);
                }
            }
            // if we get here, that means we have exhausted
            // all permutations of the assembly.  Set the
            // done flag.  Incidentally, the indices will
            // now all be pointing back to the first combination.

            return true;
        }
    }

    /**
     *
     */
    public static class CandidateColumn {
        FunctionType functionType;
        List<CandidatePart> cells = Lists.newArrayList();

        CandidateColumn (FunctionType functionType) {
            this.functionType = functionType;
        }

        void addPart (PartRec part) {
            checkArgument(part.getFunctionName().equals(functionType.toString()), "function mismatch %s", functionType);
            cells.add(new CandidatePart(part));
        }

        int getNextAvailableIndex(int colIndex) {
            // the caller is passing the previous index, so we need to advance it initially
            while (++colIndex < cells.size()) {
                if (cells.get(colIndex).isViable())
                    return colIndex;
            }
            return -1;
        }

        void nextCandidate (CandidateProduct candidate, Map<FunctionType,Integer> indices) {
            CandidatePart cp = cells.get(indices.get(functionType));
            candidate.put(functionType, cp);
        }

    }

    /**
     *
     */
    public static class CandidatePart {
        boolean viable = true;
        PartRec part;

        CandidatePart(PartRec part) {
            this.part = part;
        }

        void setViable(boolean viable) {
            this.viable = viable;
        }

        public boolean isViable() {
            return viable;
        }

        public PartRec getPart() {
            return part;
        }

        @Override
        public String toString () {
            return part.toString();
        }
    }

    /**
     *
     */
    public static class CandidateProblem {
        Map<RuleViolation,CandidatePart> problems;

        public CandidateProblem () {
        }

        public CandidateProblem (RuleViolation violation, CandidatePart candidatePart) {
            addProblem (violation, candidatePart);
        }

        public boolean hasProblems () {
            return !problems.isEmpty();
        }

        public void addProblems (CandidateProblem p) {
            problems.putAll(p.problems);
        }

        public void addProblem (RuleViolation violation, CandidatePart candidatePart) {
            if (problems == null) {
                problems = Maps.newHashMap();
            }

            problems.put(violation, candidatePart);
        }
    }


}
