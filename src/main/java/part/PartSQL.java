package part;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Iterator;
import java.util.List;

/**
 */
public interface PartSQL {

    //////////////////////////////
    // Part
    //////////////////////////////
    
    /**
     *  
     */   
    static String CREATE_TABLE_PART = ""
            + "create table part ( "
            + "supplierID text REFERENCES Supplier, "
            + "name text, "
            + "fcn text, "
            + "PRIMARY KEY(partID) "
            + ");";

    @SqlUpdate(CREATE_TABLE_PART)
    void createPartTable();

    @SqlUpdate("drop table Part")
    void dropPartTable();

    @SqlQuery("select * from part where partID = :partID")
    @Mapper(PartMapper.class)
    Part getPartByID(@Bind("partID") String partID);

    @SqlUpdate ("insert into part (partID, supplierID, name, fcn) values (:partID, :supplierID, :name, :fcn)")
    void inserPart(@Bind("partID") String partID, @Bind("supplierID") String supplierID, @Bind("name") String name, @Bind("fcn") String fcn);

    @SqlQuery("select name from part")
    Iterator<String> getAllPartNames();

    @SqlQuery("select * from part")
    @Mapper(PartMapper.class)
    Iterator<Part> getAllParts();

    @SqlQuery("select * from part")
    @Mapper(PartMapper.class)
    List<Part> getPartsList();


    //////////////////////////////
    // PartDoc
    //////////////////////////////

    /**
     * SupplierDoc
     */
    static String CREATE_TABLE_PART_DOC = ""
            + "create table PartDoc ("
            + "partID text REFERENCES Part, "
            + "batchID text REFERENCES SupplierBatch,"
            + "doc text,"
            + "ts timestamp with time zone NOT NULL DEFAULT now()"
            + ");";

    @SqlUpdate(CREATE_TABLE_PART_DOC)
    void createPartDocTable();

    @SqlUpdate("drop table PartDoc")
    void dropPartDocTable();

    @SqlUpdate("insert into PartDoc (partID, batchID, doc) values (:partID, :supplierID, :doc)")
    void insertPartDoc(@Bind("partID") String partID, @Bind("batchID") String batchID, @Bind("doc") String doc);

    @SqlQuery("select * from PartDoc where partID = :partID and ts=(select max(ts) from PartDoc where partID = :partID)")
    @Mapper(PartDocMapper.class)
    PartDoc getLatestPartDoc(@Bind("partID") String partID);

    @SqlQuery("select * from PartDoc")
    @Mapper(PartDocMapper.class)
    List<PartDoc> getAllPartDocs();


    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close();
}
