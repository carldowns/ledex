package mgr;

import catalog.*;
import ch.qos.logback.classic.Logger;
import cmd.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import part.Part;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.LoggerFactory;
import part.PartDoc;
import part.PartRec;
import part.PartSQL;
import product.Product;
import util.AppRuntimeException;
import util.FileUtil;

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

/**
 */
public class CatalogMgr {

    private PartSQL partSQL;
    private AssemblySQL assemblySQL;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CatalogMgr.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public CatalogMgr(PartSQL sql) {
        this.partSQL = sql;
    }

    /////////////////////////////
    // Part Import / Export
    /////////////////////////////

    /**
     * @param cmd
     */
    public void importParts(ImportPartsCmd cmd) {

        try {
            URI uri = new URI(cmd.getInputFilePath());
            MessageDigest md = MessageDigest.getInstance("MD5");
            List<Part> parts = new FileUtil().importParts(uri);

            for (Part part : parts) {

                // TODO do all of this in a transaction
                // TODO: validate each supplier record
                // TODO: back to json, make this better

                // generate document ID based on the document content
                String json = mapper.writeValueAsString(part);
                byte[] messageDigest = md.digest(json.getBytes());
                String docID = new String (Hex.encodeHex(messageDigest));

                // if not in the core table, consider it new and add
                PartRec found = partSQL.getPartRecByID(part.getPartID());
                if (found == null) {
                    partSQL.insertPartRec(part.getPartID(), part.getSupplierID(), part.getName(), part.getFunctionName());
                    partSQL.insertPartDoc(part.getPartID(), docID, true, json);
                    cmd.log("part " + part.getName() + " added");
                    continue;
                }

                // if doc is not present, then add it
                Integer presentCount = partSQL.isPartDocPresent(part.getPartID(), docID);
                if (presentCount == 0) {
                    partSQL.insertPartDoc(part.getPartID(), docID, true, json);
                    partSQL.demoteOthers(part.getPartID(), docID);
                    partSQL.updatePartRec(part.getPartID(), part.getName());
                    cmd.log("part " + part.getName() + " updated");
                    continue;
                }

                // if it is the current doc for the supplier, no-op
                Integer currentCount = partSQL.isPartDocCurrent(part.getPartID(), docID);
                if (currentCount == 1) {
                    cmd.log("part " + part.getName() + " no change");
                    continue;
                }

                // if doc is present but not current, then we have a reversion
                // to a previous state.  Make that doc the current doc
                if (presentCount == 1) {
                    // TODO build a stored procedure or combined method to manage current T/F state
                    partSQL.promoteToCurrent(part.getPartID(), docID);
                    partSQL.demoteOthers(part.getPartID(), docID);
                    partSQL.updatePartRec(part.getPartID(), part.getName());
                    cmd.log("part " + part.getName() + " reverted to previous change");
                    continue;
                }

                throw new AppRuntimeException("unknown state for document:  " + json);
            }

            cmd.showCompleted();

        } catch (Exception e) {
            logger.error(e.getMessage());
            cmd.log(e.getMessage());
            cmd.log("unable to import parts");
            cmd.showFailed();
        }
    }

    /**
     * exports suppliers to JSON
     * @param cmd
     */
    public void exportParts(ExportPartsCmd cmd) {
        try {
            URI uri = new URI(cmd.getOutputFilePath());

            File file = new File(uri);
            if (file.isDirectory()) {
                throw new AppRuntimeException
                        ("URI is a directory.  specify a directory and file to create");
            }

            if (file.isFile() || file.exists()) {
                throw new AppRuntimeException
                        ("file exists.  specify a new file in an existing directory.");
            }

            List<Part> parts = Lists.newArrayList();
            for (PartDoc doc : partSQL.getAllCurrentPartDocs()) {
                String json = doc.getDoc();
                if (StringUtil.isBlank(json)) {
                    throw new AppRuntimeException
                            ("json missing for part " + doc.getPartID());
                }

                Part part = mapper.readValue(json, Part.class);
                parts.add(part);
            }

            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(file, parts);
            cmd.showCompleted();

        } catch (AppRuntimeException e) {
            cmd.log(e.getMessage());
            cmd.showFailed();
        } catch (Exception e) {
            logger.error(e.getMessage());
            cmd.log(e.getMessage());
            cmd.log("unable to export parts");
            cmd.showFailed();
        }
    }

    /////////////////////////
    // Part CRUD
    /////////////////////////

    public void getPart(GetPartCmd cmd) {
        try {
            Part part = getPart (cmd.getPartID());
            cmd.setPart(part);
            cmd.showCompleted();

        } catch (AppRuntimeException e) {
            cmd.log(e.getMessage());
            cmd.showFailed();

        } catch (Exception e) {
            String msg = "unable to obtain part " + cmd.getPartID();
            logger.error(msg, e);
            cmd.log(msg);
            cmd.showFailed();
        }
    }

    public Part getPart(String partID) {
        try {
            // TODO Guava or some other type of caching yo
            PartDoc doc = partSQL.getCurrentPartDoc(partID);
            if (doc == null)
                throw new AppRuntimeException("part not found " + partID);

            Part part = mapper.readValue(doc.getDoc(), Part.class);
            part.setPartDocID(doc.getPartDocID());
            return part;

        } catch (Exception e) {
            logger.error("unable to retrieve part {}", partID, e);
            throw Throwables.propagate(e);
        }
    }

    public List<Part> getAllParts() throws Exception {

        // TODO Guava or some other type of caching yo
        List<Part> parts = Lists.newArrayList();
        for (PartDoc doc :  partSQL.getCurrentPartDocs()) {
            String json = doc.getDoc();
            if (StringUtil.isBlank(json)) {
                throw new AppRuntimeException
                        ("json missing for part " + doc.getPartID());
            }

            Part part = mapper.readValue(json, Part.class);
            part.setPartDocID(doc.getPartDocID());
            parts.add(part);
        }

        return parts;
    }

    public String getPartDocID (String partID) {
        return partSQL.getPartDocID (partID);
    }

    /////////////////////////
    // Assembly CRUD
    /////////////////////////

    public Iterator<AssemblyRec> getAllAssemblyRecs() {
        return assemblySQL.getAllAssemblyRecs();
    }

    public List<Assembly> getAllAssemblies() throws Exception {
        List<Assembly> assemblies = Lists.newArrayList();
        for (AssemblyDoc doc : assemblySQL.getAllCurrentAssemblyDocs()) {
            String json = doc.getDoc();
            if (StringUtil.isBlank(json)) {
                throw new AppRuntimeException
                        ("json missing for part " + doc.getAssemblyID());
            }

            Assembly assembly = mapper.readValue(json, Assembly.class);
            assembly.setAssemblyDocID(doc.getAssemblyDocID());
            assemblies.add(assembly);
        }

        return assemblies;
    }

    public String getAssemblyDocID (String assemblyID) {
        return assemblySQL.getAssemblyDocID (assemblyID);
    }

    /////////////////////////
    // Catalog Generation
    /////////////////////////

    public void updateProduct (Assembly assembly, Product product) {

    }

    /////////////////////////
    // Catalog Listing
    /////////////////////////

    /////////////////////////
    // Catalog Search
    /////////////////////////

    /////////////////////////
    // Compatible Part
    /////////////////////////

    /////////////////////////
    // Assembly Mutation
    /////////////////////////

    /////////////////////////
    // Part List Import
    /////////////////////////

    /////////////////////////
    // Part List Export
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////

    /////////////////////////
    /////////////////////////


}