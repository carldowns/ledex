package mgr;

import catalog.*;
import ch.qos.logback.classic.Logger;

import static com.google.common.base.Preconditions.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;
import part.Part;
import product.Product;
import product.ProductPart;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class AssemblyMgr {

    CatalogMgr catalogMgr;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssemblyMgr.class);

    public AssemblyMgr(CatalogMgr catalogMgr) {
        this.catalogMgr = catalogMgr;
    }

    /**
     * updates products in the catalog based on active Assembly rules.
     */
    public void buildCatalog() {

        try {
            for (Assembly assembly : catalogMgr.getAllAssemblies()) {
                List<CandidateProduct> products = buildProductCandidates(assembly);

                for (CandidateProduct candidate : products) {
                    Product product = new Product();

                    int count = 100;
                    for (Map.Entry<FunctionType, CandidatePart> set : candidate.candidateParts.entrySet()) {
                        FunctionType function = set.getKey();

                        CandidatePart candidatePart = set.getValue();
                        Part part = candidatePart.getPart();

                        // FIXME come up with something better for product ID assignment
                        ProductPart productPart = new ProductPart();
                        productPart.setProductID(assembly.getAssemblyID() + "." + count++);
                        productPart.setAssemblyID(assembly.getAssemblyID());
                        productPart.setAssemblyDocID(assembly.getAssemblyDocID());
                        productPart.setPartID(part.getPartID());
                        productPart.setPartDocID(part.getPartDocID());
                        productPart.setFunction(function);

                        product.addPart(productPart);
                    }
                    catalogMgr.updateProduct(assembly, product);
                }
            }
        } catch (Exception e) {
            logger.error("unable to build catalog", e);
        }
    }

    /**
     * returns a list of candidate products considered valid and compatible based on the rules
     * expressed in the given Assembly.  The candidate products are ready to be added to the catalog.
     *
     * @param assembly
     * @return
     * @throws Exception
     */
    public List<CandidateProduct> buildProductCandidates(Assembly assembly) throws Exception {

        // build all candidate combinations (all permutations)
        // pass initial compatibility checks
        // eliminate parts that are incompatible
        // add passed candidates as Products

        CandidateBuilder builder = new CandidateBuilder();
        builder.setFunctions(assembly.getFunctionTypes());

        // TODO: get only those that match the set of Functions present in the Assembly (use IN clause)
        for (Part part : catalogMgr.getAllParts()) {
            builder.addCandidate(part);
        }

        RuleEngine engine = new RuleEngine();
        List<CandidateProduct> products = Lists.newArrayList();
        while (!builder.isAllPermutationsReported()) {

            CandidateProduct candidate = builder.nextCandidate();
            CandidateProblem report = engine.evaluate(assembly, candidate);
            if (report.hasProblems()) {
                // hold off on eliminations
                //builder.eliminateParts(report);
                continue;
            }

            products.add(candidate);
        }

        return products;
    }

    public static class CandidateProduct {
        Map<FunctionType, CandidatePart> candidateParts = Maps.newHashMap();

        public void put(FunctionType f, CandidatePart p) {
            candidateParts.put(f, p);
        }

        public Map<FunctionType, CandidatePart> getCandidateParts() {
            return candidateParts;
        }

        public void setCandidateParts(Map<FunctionType, CandidatePart> candidateParts) {
            this.candidateParts = candidateParts;
        }

        public List<String> getPartIDs() {
            List<String> result = Lists.newArrayList();
            for (CandidatePart cp : candidateParts.values()) {
                result.add(cp.getPart().getPartID());
            }
            return result;
        }
    }

    /**
     *
     */
    public static class CandidateBuilder {
        CandidateTable table = new CandidateTable();
        boolean allPermutationsReported = false;

        void setFunctions(List<FunctionType> functions) {
            table.setFunctions(functions);
        }

        void addCandidate(Part part) {
            table.addCandidate(part);
        }

        boolean isAllPermutationsReported() {
            return allPermutationsReported;
        }

        CandidateProduct nextCandidate() {

            if (isAllPermutationsReported()) {
                return null;
            }

            CandidateProduct candidate = new CandidateProduct();
            allPermutationsReported = table.nextCandidate(candidate);
            return candidate;
        }

        // pre-optimization that is backfiring - we would need a rule to carefully
        // know that it's rejection makes the part unsuitable for ANY permutation
        // just to try and cut the number of permutations down.
        // Let's not go there just yet

//        void eliminateParts (CandidateProblem problems) {
//            for (CandidatePart candidatePart : problems.problems.values()) {
//                candidatePart.setViable(false);
//            }
//        }
    }

    /**
     *
     */
    public static class CandidateTable {
        // indices indicate the 'next' combination to return
        LinkedHashMap<FunctionType, Integer> indices = Maps.newLinkedHashMap();

        // linkage preserves order-of-insertion support
        LinkedHashMap<FunctionType, CandidateColumn> columns = Maps.newLinkedHashMap();

        void setFunctions(List<FunctionType> functions) {
            for (FunctionType functionType : functions) {
                columns.put(functionType, new CandidateColumn(functionType));
                indices.put(functionType, 0);
            }
        }

        void addCandidate(Part part) {
            CandidateColumn column = columns.get(part.getFunctionType());
            column.addPart(part);
        }

        boolean nextCandidate(CandidateProduct candidate) {
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

        CandidateColumn(FunctionType functionType) {
            this.functionType = functionType;
        }

        void addPart(Part part) {
            checkArgument(part.getFunctionType() == functionType, "function mismatch %s", functionType);
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

        void nextCandidate(CandidateProduct candidate, Map<FunctionType, Integer> indices) {
            CandidatePart cp = cells.get(indices.get(functionType));
            candidate.put(functionType, cp);
        }

    }

    /**
     *
     */
    public static class CandidatePart {
        boolean viable = true;
        Part part;

        CandidatePart(Part part) {
            this.part = part;
        }

        void setViable(boolean viable) {
            this.viable = viable;
        }

        public boolean isViable() {
            return viable;
        }

        public Part getPart() {
            return part;
        }

        @Override
        public String toString() {
            return part.toString();
        }
    }

    /**
     *
     */
    public static class CandidateProblem {
        Map<RuleViolation, CandidatePart> problems = Maps.newHashMap();

        public CandidateProblem() {
        }

        public CandidateProblem(RuleViolation violation, CandidatePart candidatePart) {
            addProblem(violation, candidatePart);
        }

        public boolean hasProblems() {
            return !problems.isEmpty();
        }

        public void addProblems(CandidateProblem cp) {
            if (cp == null) {
                return;
            }

            problems.putAll(cp.problems);
        }

        public void addProblem(RuleViolation violation, CandidatePart candidatePart) {
            problems.put(violation, candidatePart);
        }
    }


}
