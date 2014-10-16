package logic;

import cmd.*;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import supplier.Supplier;
import supplier.SupplierDoc;
import supplier.SupplierSQL;
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
    private static final ObjectMapper mapper = new ObjectMapper();

    public SupplierMgr (SupplierSQL sql) {
        this.sql = sql;
    }

    /////////////////////////////
    // Supplier Import / Export
    /////////////////////////////

    public void importSuppliers (ImportSuppliersCmd cmd) throws Exception {
        // open the file

        URI uri = cmd.getInputFilePath();
        List<Supplier> suppliers = new FileUtil().importSuppliers(uri);
        for(Supplier s : suppliers) {
            Supplier found = sql.getSupplierByID(s.getId());
            if (found != null) {
                cmd.log("supplier " + s.getName() + " exists, left undisturbed");
                continue;
            }

            String json = mapper.writeValueAsString(s);
            sql.insertSupplier (s.getId(), s.getName());
            sql.insertSupplierDoc (s.getId(), json);
            cmd.log("supplier " + s.getName() + " added");
        }

        cmd.setState("completed");

        // the supplier index holds the name and ID
        // if a record exists for the supplier, accept it.
        // if not, create one

        // if there is an extended object, persist the Json
        // in the supplier doc table.
    }

    public void exportSuppliers (ExportSuppliersCmd cmd) throws Exception {
        URI uri = cmd.getOutputFilePath();
        cmd.log("evaluating URI");

        File file = new File (uri);
        if (file.isDirectory()) {
            cmd.log("URI is a directory.  specify a directory and file to create");
            cmd.setState("aborted");
            return;
        }

        if (file.isFile() || file.exists()) {
            cmd.log("file exists.  specify a new file in an existing directory.");
            cmd.setState("aborted");
            return;
        }

        //List<Supplier> suppliers = sql.getSuppliersList();

        List<Supplier> suppliers = new ArrayList<>();
        for (SupplierDoc doc : sql.getAllSupplierDocs()) {
            Supplier supplier = mapper.readValue(doc.getDoc(), Supplier.class);
            suppliers.add(supplier);
        }

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(file, suppliers);
        cmd.setState("completed");
    }

    /////////////////////////
    // Supplier CRUD
    /////////////////////////

    public void createSupplier (BaseCmd cmd) {
    }

    public void updateSupplier (BaseCmd cmd) {
    }

    public void deleteSupplier (BaseCmd cmd) {
    }

    public void getSupplier () {
    }

    public void getSuppliers () {
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
