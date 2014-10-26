package logic;

import ch.qos.logback.classic.Logger;
import cmd.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Lists;
import part.Part;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.LoggerFactory;
import part.PartDoc;
import part.PartRec;
import part.PartSQL;
import util.AppRuntimeException;
import util.FileUtil;

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.util.List;

/**
 */
public class CatalogMgr {

    private PartSQL sql;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CatalogMgr.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public CatalogMgr(PartSQL sql) {
        this.sql = sql;
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
                PartRec found = sql.getPartRecByID(part.getPartID());
                if (found == null) {
                    sql.insertPartRec(part.getPartID(), part.getSupplierID(), part.getName(), part.getFunction());
                    sql.insertPartDoc(part.getPartID(), docID, true, json);
                    cmd.log("part " + part.getName() + " added");
                    continue;
                }

                // if doc is not present, then add it
                Integer presentCount = sql.isPartDocPresent(part.getPartID(), docID);
                if (presentCount == 0) {
                    sql.insertPartDoc(part.getPartID(), docID, true, json);
                    sql.demoteOthers(part.getPartID(), docID);
                    sql.updatePartRec(part.getPartID(), part.getName());
                    cmd.log("part " + part.getName() + " updated");
                    continue;
                }

                // if it is the current doc for the supplier, no-op
                Integer currentCount = sql.isPartDocCurrent(part.getPartID(), docID);
                if (currentCount == 1) {
                    cmd.log("part " + part.getName() + " no change");
                    continue;
                }

                // if doc is present but not current, then we have a reversion
                // to a previous state.  Make that doc the current doc
                if (presentCount == 1) {
                    // TODO build a stored procedure or combined method to manage current T/F state
                    sql.promoteToCurrent(part.getPartID(), docID);
                    sql.demoteOthers(part.getPartID(), docID);
                    sql.updatePartRec(part.getPartID(), part.getName());
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
            for (PartDoc doc : sql.getAllCurrentPartDocs()) {
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

    /**
     * @param cmd
     */
    public void getPart(GetPartCmd cmd) {
        try {
            PartDoc doc = sql.getCurrentPartDoc(cmd.getPartID());
            if (doc == null)
                throw new AppRuntimeException("supplier not found");

            Part part = mapper.readValue(doc.getDoc(), Part.class);
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


    /////////////////////////
    // Assembly CRUD
    /////////////////////////

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
