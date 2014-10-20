package logic;

import ch.qos.logback.classic.Logger;
import cmd.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.LoggerFactory;
import supplier.Supplier;
import supplier.SupplierDoc;
import supplier.SupplierSQL;
import util.AppRuntimeException;
import util.FileUtil;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    public void importSuppliers(ImportSuppliersCmd cmd) {
        // open the file

        try {
            URI uri = new URI(cmd.getInputFilePath());
            List<Supplier> suppliers = new FileUtil().importSuppliers(uri);
            for (Supplier s : suppliers) {
                Supplier found = sql.getSupplierByID(s.getId());

                // the supplier index holds the name and ID
                // of a record exists for the supplier, accept it.
                // if not, create one

                // if there is an extended object, persist the Json
                // in the supplier doc table.

                if (found != null) {
                    cmd.log("supplier " + s.getName() + " exists, left undisturbed");
                    continue;
                }

                String json = mapper.writeValueAsString(s);
                sql.insertSupplier(s.getId(), s.getName());
                sql.insertSupplierDoc(s.getId(), json);
                cmd.log("supplier " + s.getName() + " added");
            }

            cmd.showCompleted();

        } catch (Exception e) {
            logger.error(e.getMessage());
            cmd.log(e.getMessage());
            cmd.log("unable to import suppliers");
            cmd.showFailed();
        }
    }

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

    public void getSupplier(GetSupplierCmd cmd) {
        try {
            SupplierDoc doc = sql.getLatestSupplierDoc(cmd.getSupplierID());
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
