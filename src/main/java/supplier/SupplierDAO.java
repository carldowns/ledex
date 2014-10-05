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
public interface SupplierDAO {

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

    @SqlUpdate("insert into supplier (supplierID, name) values (:supplierID, :name)")
    void insertPerson(@Bind("supplierID") String supplierID, @Bind("name") String name);

    @SqlQuery("select name from supplier where supplierID = :supplierID")
    String findNameByPersonId(@Bind("supplierID") int supplierID);
    
    @SqlQuery("select name from supplier where id > :from and id < :to")
    List<String> findNamesBetween(@Bind("from") int from, @Bind("to") int to);
    
    @SqlQuery("select name from supplier")
    Iterator<String> findAllNames();

    @SqlQuery("select * from supplier")
    @Mapper(SupplierEntityMapper.class)
    Iterator<SupplierEntity> findAll();

    
    //////////////////////////////
    // SupplierDoc
    //////////////////////////////

    /**
     * SupplierDoc
     */   
    static String CREATE_TABLE_SUPPLIER_DOC = ""
            + "create table SupplierDoc ("
            + "supplierID text REFERENCES Supplier,"
            + "doc text,"
            + "ts timestamp with time zone NOT NULL DEFAULT now()"
            + ");";
    
    @SqlUpdate(CREATE_TABLE_SUPPLIER_DOC)
    void createSupplierDocTable();

    @SqlUpdate("insert into supplierDoc (supplierID, doc) values (:supplierID, :doc)")
    void insertSupplierDoc(@Bind("supplierID") String supplierID, @Bind("doc") String doc);

    @SqlQuery("select * from supplierDoc where supplierId = :supplierId")
    @Mapper(SupplierDocEntityMapper.class)
    SupplierDocEntity getSupplierDoc(@Bind("supplierId") String supplierId);
}
