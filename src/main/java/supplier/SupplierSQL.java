package supplier;

import java.util.Iterator;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * CREATE EXTENSION "uuid-ossp”;
 * needed to use the uuid for a column:
 * "entryID UUID NOT NULL DEFAULT uuid_generate_v4(),"
 * @author carl_downs
 *
 */
public interface SupplierSQL {

    //////////////////////////////
    // Supplier
    //////////////////////////////
    
    /**
     *  
     */   
    static String CREATE_TABLE_SUPPLIER = ""
            + "create table supplier ( "
            + "supplierID text, "
            + "name text, "
            + "PRIMARY KEY(supplierID) "
            + ");";

    @SqlUpdate(CREATE_TABLE_SUPPLIER)
    void createSupplierTable();

//    @SqlUpdate("insert into supplier (supplierID, name) values (:supplierID, :name)")
//    void insertPerson(@Bind("supplierID") String supplierID, @Bind("name") String name);
//
//    @SqlQuery("select name from supplier where id > :from and id < :to")
//    List<String> findNamesBetween(@Bind("from") int from, @Bind("to") int to);

    @SqlQuery("select * from supplier where supplierID = :supplierID")
    @Mapper(SupplierMapper.class)
    Supplier getSupplierByID(@Bind("supplierID") String supplierID);

    @SqlUpdate ("insert into supplier (supplierID, name) values (:supplierID, :name)")
    void insertSupplier (@Bind("supplierID") String supplierID, @Bind("name") String name);

    @SqlQuery("select name from supplier")
    Iterator<String> getAllSupplierNames();

    @SqlQuery("select * from supplier")
    @Mapper(SupplierMapper.class)
    Iterator<Supplier> getAllSuppliers();

    @SqlQuery("select * from supplier")
    @Mapper(SupplierMapper.class)
    List<Supplier> getSuppliersList();


    //////////////////////////////
    // SupplierDoc
    //////////////////////////////

    /**
     * SupplierDoc
     */   
    static String CREATE_TABLE_SUPPLIER_DOC = ""
            + "create table SupplierDoc ("
            + "supplierID text REFERENCES Supplier,"
            + "model text,"
            + "ts timestamp with time zone NOT NULL DEFAULT now()"
            + ");";

    @SqlUpdate(CREATE_TABLE_SUPPLIER_DOC)
    void createSupplierDocTable();

    @SqlUpdate("drop table SupplierDoc")
    void dropSupplierDocTable();

    @SqlUpdate("insert into supplierDoc (supplierID, doc) values (:supplierID, :doc)")
    void insertSupplierDoc(@Bind("supplierID") String supplierID, @Bind("doc") String doc);

    @SqlQuery("select * from supplierDoc where supplierID = :supplierID and ts=(select max(ts) from supplierDoc where supplierID = :supplierID)")
    @Mapper(SupplierDocMapper.class)
    SupplierDoc getLatestSupplierDoc(@Bind("supplierID") String supplierId);

    @SqlQuery("select * from supplierDoc")
    @Mapper(SupplierDocMapper.class)
    List<SupplierDoc> getAllSupplierDocs();


    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close ();
}
