package mgr;

import catalog.Assembly;
import catalog.FunctionType;
import catalog.Product;
import catalog.rule.RuleEngine;
import catalog.rule.RuleViolation;
import ch.qos.logback.classic.Logger;

import static com.google.common.base.Preconditions.*;

import catalog.cmd.CatalogUpdateCmd;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.LoggerFactory;
import part.Part;
import catalog.dao.CatalogPart;
import cmd.CmdState;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Singleton
public class AssemblyMgr {

    CatalogMgr catalogMgr;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssemblyMgr.class);

    @Inject
    public AssemblyMgr(CatalogMgr catalogMgr) {
        this.catalogMgr = catalogMgr;
    }

    /**
     * deletes / rebuilds all products in the catalog based on imported Assemblies and Parts
     */
    public void exec(CatalogUpdateCmd cmd) {

        try {
            cmd.setState(CmdState.started);
            catalogMgr.deleteCatalog();

            cmd.log("rebuilding catalog from scratch");
            cmd.log("catalog contents deleted");

            for (Assembly assembly : catalogMgr.getAllAssemblies()) {
                List<CandidateProduct> products = assembleProductCandidates(assembly);

                int productIDSuffix = 1000;
                for (CandidateProduct candidate : products) {

                    Product product = new Product();
                    String productID = assembly.getAssemblyID() + "-" + productIDSuffix;
                    product.setProductID(productID);

                    for (Map.Entry<FunctionType, CandidatePart> set : candidate.candidateParts.entrySet()) {

                        CandidatePart candidatePart = set.getValue();
                        FunctionType function = set.getKey();
                        Part part = candidatePart.getPart();

                        CatalogPart catalogPart = new CatalogPart();
                        catalogPart.setProductID(productID);
                        catalogPart.setAssemblyID(assembly.getAssemblyID());
                        catalogPart.setAssemblyDocID(assembly.getAssemblyDocID());
                        catalogPart.setPartID(part.getPartID());
                        catalogPart.setPartDocID(part.getPartDocID());
                        catalogPart.setFunction(function);
                        catalogPart.setLinkable(part.isLinkable());

                        product.addPart(catalogPart);
                    }
                    catalogMgr.addToCatalog(product);
                    cmd.log(product.getProductID() + " added");
                    productIDSuffix++;
                }

            }
            cmd.setState(CmdState.completed);
        }
        catch (Exception e) {
            cmd.log(e.toString());
            cmd.setState(CmdState.failed);
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

    @VisibleForTesting
    public List<CandidateProduct> assembleProductCandidates(Assembly assembly) throws Exception {

        // build all candidate combinations (all permutations)
        // pass initial compatibility checks
        // eliminate parts that are incompatible
        // add passed candidates as Products

        CandidateBuilder builder = new CandidateBuilder();
        builder.setFunctions(assembly.getFunctionTypes());

        // TODO: get only those that match the set of Functions present in the Assembly (use IN clause)
        for (Part part : catalogMgr.getAllParts(/*assembly.getFunctionTypes()*/)) {
            builder.addCandidate(part);
        }

        RuleEngine engine = new RuleEngine();
        List<CandidateProduct> products = Lists.newArrayList();
        while (!builder.isAllPermutationsReported()) {

            CandidateProduct candidate = builder.nextCandidate(assembly);
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

        Assembly assembly;
        CandidateProduct (Assembly assembly) {
            this.assembly = assembly;
        }

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

        public Assembly getAssembly() {
            return assembly;
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

        CandidateProduct nextCandidate(Assembly assembly) {

            if (isAllPermutationsReported()) {
                return null;
            }

            CandidateProduct candidate = new CandidateProduct(assembly);
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

            // only add part if its function is specified in the assembly
            if (column != null) {
                column.addPart(part);
            }
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
