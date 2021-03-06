package mgr;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.LoggerFactory;
import supplier.Supplier;
import supplier.cmd.SupplierExportCmd;
import supplier.cmd.SupplierFetchCmd;
import supplier.cmd.SupplierImportCmd;
import supplier.dao.SupplierDoc;
import supplier.dao.SupplierRec;
import supplier.dao.SupplierSQL;
import util.AppRuntimeException;
import util.FileUtil;
import util.HashUtil;

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier CRUD
 * Created by carl_downs on 10/9/14.
 */
@Singleton
public class SupplierMgr {

    private SupplierSQL sql;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(SupplierMgr.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
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
    public void exec(SupplierImportCmd cmd) {
        // open the file

        try {
            URI uri = new URI(cmd.getInputFilePath());
            List<Supplier> suppliers = new FileUtil().importSuppliers(uri);
            for (Supplier supplier : suppliers) {
                // TODO do all of this in a transaction
                // TODO: validate each supplier record

                // back to json
                // generate a message digest based on the document content
                // this becomes our document ID
                String json = mapper.writeValueAsString(supplier);
                byte[] messageDigest = MessageDigest.getInstance("MD5").digest(json.getBytes());
                //String docID = new String (Hex.encodeHex(messageDigest));
                String docID = HashUtil.getChecksumAsString(json);

                // if not in the core table, consider it new and add
                SupplierRec found = sql.getSupplierRecByID(supplier.getSupplierID());
                if (found == null) {
                    sql.insertSupplierRec(supplier.getSupplierID(), supplier.getName());
                    sql.insertSupplierDoc(supplier.getSupplierID(), docID, true, json);
                    cmd.log("supplier " + supplier.getName() + " added");
                    continue;
                }

                // if doc is not present, then add it
                Integer presentCount = sql.isSupplierDocPresent(supplier.getSupplierID(), docID);
                if (presentCount == 0) {
                    sql.insertSupplierDoc(supplier.getSupplierID(), docID, true, json);
                    sql.demoteOthers(supplier.getSupplierID(), docID);
                    sql.updateSupplierRec(supplier.getSupplierID(), supplier.getName());
                    cmd.log("supplier " + supplier.getName() + " updated");
                    continue;
                }

                // if it is the current doc for the supplier, no-op
                Integer currentCount = sql.isSupplierDocCurrent(supplier.getSupplierID(), docID);
                if (currentCount == 1) {
                    cmd.log("supplier " + supplier.getName() + " no change");
                    continue;
                }

                // if doc is present but not current, then we have a reversion
                // to a previous state.  Make that doc the current doc
                if (presentCount == 1) {
                    // TODO build a stored procedure or combined method to manage current T/F state
                    sql.promoteToCurrent(supplier.getSupplierID(), docID);
                    sql.demoteOthers(supplier.getSupplierID(), docID);
                    sql.updateSupplierRec(supplier.getSupplierID(), supplier.getName());
                    cmd.log("supplier " + supplier.getName() + " reverted to previous change");
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
    public void exec(SupplierExportCmd cmd) {
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
                String json = doc.getDoc();
                if (StringUtil.isBlank(json)) {
                    throw new AppRuntimeException
                            ("json missing for supplier " + doc.getSupplierID());
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

//    public void createSupplier(AbstractBaseCmd cmd) {
//    }
//
//    public void updateSupplier(AbstractBaseCmd cmd) {
//    }
//
//    public void deleteSupplier(AbstractBaseCmd cmd) {
//    }

    /**
     * returns Supplier JSON document as POJO
     * @param cmd
     */
    public void exec(SupplierFetchCmd cmd) {
        try {
            SupplierDoc doc = sql.getCurrentSupplierDoc(cmd.getSupplierID());
            if (doc == null)
                throw new AppRuntimeException("supplier not found");

            Supplier supplier = mapper.readValue(doc.getDoc(), Supplier.class);
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
