package part.dao;

import catalog.FunctionType;
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
    // PartRec
    //////////////////////////////


    @SqlQuery("select * from PartRec where partID = :partID")
    @Mapper(PartRecMapper.class)
    PartRec getPartRecByID(@Bind("partID") String partID);

    @SqlUpdate ("insert into PartRec (partID, supplierID, name, function) values (:partID, :supplierID, :name, :function)")
    void insertPartRec(@Bind("partID") String partID, @Bind("supplierID") String supplierID, @Bind("name") String name, @Bind("function") String function);

    @SqlQuery("select name from PartRec")
    Iterator<String> getAllPartRecNames();

    @SqlQuery("select * from PartRec")
    @Mapper(PartRecMapper.class)
    Iterator<PartRec> getAllPartRecs();

    @SqlQuery("select * from PartRec")
    @Mapper(PartRecMapper.class)
    List<PartRec> getPartRecsList();

    @SqlUpdate ("update partRec set name = :name where partID = :partID")
    void updatePartRec (@Bind("partID") String partID, @Bind("name") String name);

    //////////////////////////////
    // PartDoc
    //////////////////////////////


    @SqlUpdate("insert into PartDoc (partID, partDocID, current, doc) values (:partID, :partDocID, :current, :doc)")
    void insertPartDoc(@Bind("partID") String partID, @Bind("partDocID") String partDocID, Boolean current, @Bind("doc") String doc);

    @SqlQuery("select * from PartDoc where partID = :partID and ts=(select max(ts) from PartDoc where partID = :partID)")
    @Mapper(PartDocMapper.class)
    PartDoc getLatestPartDoc(@Bind("partID") String partID);

    @SqlQuery("select * from PartDoc")
    @Mapper(PartDocMapper.class)
    List<PartDoc> getPartDocsList();

    @SqlQuery("select count(*) from partDoc where partID = :partID and partDocID = :partDocID")
    Integer isPartDocPresent (@Bind("partID") String partID, @Bind("partDocID") String partDocID);

    @SqlUpdate("insert into partDoc (partID, partDocID, current, doc) values (:partID, :partDocID, :current, :doc)")
    void insertPartDoc(@Bind("partID") String partID, @Bind("partDocID") String partDocID, @Bind("current") boolean current, @Bind("doc") String doc);

    @SqlUpdate("update partDoc set current = false where partID = :partID and partDocID != :partDocID")
    void demoteOthers (@Bind("partID") String partID, @Bind("partDocID") String partDocID);

    @SqlUpdate("update partDoc set current = true where partID = :partID and partDocID = :partDocID")
    void promoteToCurrent(@Bind("partID") String partID, @Bind("partDocID") String partDocID);

    @SqlQuery("select count(*) from partDoc where partID = :partID and partDocID = :partDocID and current = false")
    Integer isPartDocArchived (@Bind("partID") String partID, @Bind("partDocID") String partDocID);

    @SqlQuery("select count(*) from partDoc where partID = :partID and partDocID = :partDocID and current = true")
    Integer isPartDocCurrent (@Bind("partID") String partID, @Bind("partDocID") String partDocID);

    @SqlQuery("select * from partDoc where partID = :partID and current = true")
    @Mapper(PartDocMapper.class)
    PartDoc getCurrentPartDoc(@Bind("partID") String supplierId);

    @SqlQuery("select * from partDoc where current = true")
    @Mapper(PartDocMapper.class)
    List<PartDoc> getCurrentPartDocs();

    @SqlQuery("select * from partDoc where current = true and function In [:functions]")
    @Mapper(PartDocMapper.class)
    List<PartDoc> getCurrentPartDocs(@Bind("functions") List<FunctionType> functions);

    @SqlQuery("select * from partDoc where current = true")
    @Mapper(PartDocMapper.class)
    List<PartDoc> getAllCurrentPartDocs();

    @SqlQuery("select partDocID from partDoc where partID = :partID and current = true")
    String getPartDocID(String partID);

    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close();
}
