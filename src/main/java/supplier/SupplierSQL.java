package supplier;

import java.util.Iterator;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 */
public interface SupplierSQL {

    //////////////////////////////
    // SupplierRec
    //////////////////////////////


//    @SqlUpdate("insert into supplier (supplierID, name) values (:supplierID, :name)")
//    void insertPerson(@Bind("supplierID") String supplierID, @Bind("name") String name);
//
//    @SqlQuery("select name from supplier where id > :from and id < :to")
//    List<String> findNamesBetween(@Bind("from") int from, @Bind("to") int to);

    @SqlQuery("select * from supplier where supplierID = :supplierID")
    @Mapper(SupplierRecMapper.class)
    SupplierRec getSupplierRecByID(@Bind("supplierID") String supplierID);

    @SqlUpdate ("insert into supplier (supplierID, name) values (:supplierID, :name)")
    void insertSupplierRec(@Bind("supplierID") String supplierID, @Bind("name") String name);

    @SqlUpdate ("update supplier set name = :name where supplierID = :supplierID")
    void updateSupplierRec(@Bind("supplierID") String supplierID, @Bind("name") String name);

    @SqlQuery("select name from supplier")
    Iterator<String> getAllSupplierNames();

    @SqlQuery("select * from supplier")
    @Mapper(SupplierRecMapper.class)
    Iterator<SupplierRec> getAllSuppliers();

    @SqlQuery("select * from supplier")
    @Mapper(SupplierRecMapper.class)
    List<SupplierRec> getSuppliersList();


    //////////////////////////////
    // SupplierDoc
    //////////////////////////////

    @SqlUpdate("insert into supplierDoc (supplierID, supplierDocID, current, doc) values (:supplierID, :supplierDocID, :current, :doc)")
    void insertSupplierDoc(@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID, @Bind("current") boolean current, @Bind("doc") String doc);

    @SqlUpdate("delete from supplierDoc where supplierID = :supplierID and supplierDocID = :supplierDocID")
    void deleteSupplierDoc(@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID );

    @SqlUpdate("update supplierDoc set current = false where supplierID = :supplierID and supplierDocID != :supplierDocID")
    void demoteOthers (@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID);

    @SqlUpdate("update supplierDoc set current = true where supplierID = :supplierID and supplierDocID = :supplierDocID")
    void promoteToCurrent(@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID);

    @SqlQuery("select count(*) from supplierDoc where supplierID = :supplierID and supplierDocID = :supplierDocID")
    Integer isSupplierDocPresent (@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID);

    @SqlQuery("select count(*) from supplierDoc where supplierID = :supplierID and supplierDocID = :supplierDocID and current = false")
    Integer isSupplierDocArchived (@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID);

    @SqlQuery("select count(*) from supplierDoc where supplierID = :supplierID and supplierDocID = :supplierDocID and current = true")
    Integer isSupplierDocCurrent (@Bind("supplierID") String supplierID, @Bind("supplierDocID") String supplierDocID);

    @SqlQuery("select * from supplierDoc where supplierID = :supplierID and current = true")
    @Mapper(SupplierDocMapper.class)
    SupplierDoc getCurrentSupplierDoc(@Bind("supplierID") String supplierId);

    @SqlQuery("select * from supplierDoc where current = true")
    @Mapper(SupplierDocMapper.class)
    List<SupplierDoc> getAllSupplierDocs();


    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close ();
}
