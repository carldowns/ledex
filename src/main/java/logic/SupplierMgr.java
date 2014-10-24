package logic;

import ch.qos.logback.classic.Logger;
import cmd.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.LoggerFactory;
import supplier.Supplier;
import supplier.SupplierDoc;
import supplier.SupplierSQL;
import util.AppRuntimeException;
import util.FileUtil;

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Supplier CRUD
 * Created by carl_downs on 10/9/14.
 */
public class SupplierMgr {

    private SupplierSQL sql;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(SupplierMgr.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public SupplierMgr(SupplierSQL sql) {
        this.sql = sql;
    }

    /////////////////////////////
    // Supplier Import / Export
    /////////////////////////////

    /**
     * imports suppliers from JSON
     * @param cmd
     */
    public void importSuppliers(ImportSuppliersCmd cmd) {
        // open the file

        try {
            URI uri = new URI(cmd.getInputFilePath());
            List<Supplier> suppliers = new FileUtil().importSuppliers(uri);
            for (Supplier s : suppliers) {
                // TODO do all of this in a transaction
                // TODO: validate each supplier record

                // back to json
                // generate a message digest based on the document content
                // this becomes our document ID
                String json = mapper.writeValueAsString(s);
                byte[] messageDigest = MessageDigest.getInstance("MD5").digest(json.getBytes());
                String docID = new String (Hex.encodeHex(messageDigest));

                // if not in the front table, consider it new and add
                Supplier found = sql.getSupplierByID(s.getId());
                if (found == null) {
                    sql.insertSupplier(s.getId(), s.getName());
                    sql.insertSupplierDoc(s.getId(), docID, true, json);
                    cmd.log("supplier " + s.getName() + " added");
                    continue;
                }

                // if doc is not present, then add it
                Integer presentCount = sql.isSupplierDocPresent(s.getId(), docID);
                if (presentCount == 0) {
                    sql.insertSupplierDoc(s.getId(), docID, true, json);
                    sql.demoteOthers(s.getId(), docID);
                    sql.updateSupplier(s.getId(), s.getName());
                    cmd.log("supplier " + s.getName() + " updated");
                    continue;
                }

                // if it is the current doc for the supplier, no-op
                Integer currentCount = sql.isSupplierDocCurrent(s.getId(), docID);
                if (currentCount == 1) {
                    cmd.log("supplier " + s.getName() + " no change");
                    continue;
                }

                // if doc is present but not current, then we have a reversion
                // to a previous state.  Make that doc the current doc
                if (presentCount == 1) {
                    // TODO build a stored procedure or combined method to promote / demote
                    sql.promoteToCurrent(s.getId(), docID);
                    sql.demoteOthers(s.getId(), docID);
                    sql.updateSupplier(s.getId(), s.getName());
                    cmd.log("supplier " + s.getName() + " reverted to previous state");
                    continue;
                }

                throw new AppRuntimeException("unknown state for document:  " + json);
            }

            cmd.showCompleted();

        } catch (Exception e) {
            logger.error(e.getMessage());
            cmd.log(e.getMessage());
            cmd.log("unable to import suppliers");
            cmd.showFailed();
        }
    }

    /**
     * exports suppliers to JSON
     * @param cmd
     */
    public void exportSuppliers(ExportSuppliersCmd cmd) {
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

            List<Supplier> suppliers = new ArrayList<>();
            for (SupplierDoc doc : sql.getAllSupplierDocs()) {
                String json = doc.getJson();
                if (StringUtil.isBlank(json)) {
                    throw new AppRuntimeException
                            ("json missing for supplier " + doc.getId());
                }

                Supplier supplier = mapper.readValue(json, Supplier.class);
                suppliers.add(supplier);
            }

            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(file, suppliers);
            cmd.showCompleted();

        } catch (AppRuntimeException e) {
            cmd.log(e.getMessage());
            cmd.showFailed();
        } catch (Exception e) {
            logger.error(e.getMessage());
            cmd.log(e.getMessage());
            cmd.log("unable to export suppliers");
            cmd.showFailed();
        }
    }

    /////////////////////////
    // Supplier CRUD
    /////////////////////////

    public void createSupplier(BaseCmd cmd) {
    }

    public void updateSupplier(BaseCmd cmd) {
    }

    public void deleteSupplier(BaseCmd cmd) {
    }

    /**
     * returns Supplier JSON document as POJO
     * @param cmd
     */
    public void getSupplier(GetSupplierCmd cmd) {
        try {
            SupplierDoc doc = sql.getCurrentSupplierDoc(cmd.getSupplierID());
            if (doc == null)
                throw new AppRuntimeException("supplier not found");

            Supplier supplier = mapper.readValue(doc.getJson(), Supplier.class);
            cmd.setSupplier(supplier);
            cmd.showCompleted();

        } catch (AppRuntimeException e) {
            cmd.log(e.getMessage());
            cmd.showFailed();

        } catch (Exception e) {
            String msg = "unable to obtain supplier " + cmd.getSupplierID();
            logger.error(msg, e);
            cmd.log(msg);
            cmd.showFailed();
        }
    }

    public void getSuppliers() {
    }


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
