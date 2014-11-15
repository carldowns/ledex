package catalog;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import part.PartDoc;
import part.PartDocMapper;
import part.PartRec;
import part.PartRecMapper;

import java.util.Iterator;
import java.util.List;

/**
 */
public interface AssemblySQL {

    //////////////////////////////
    // AssemblyRec
    //////////////////////////////

    @SqlQuery("select * from AssemblyRec")
    @Mapper(AssemblyRecMapper.class)
    Iterator<AssemblyRec> getAllAssemblyRecs();

    @SqlQuery("select * from AssemblyRec where assemblyID = :assemblyID")
    @Mapper(AssemblyRecMapper.class)
    AssemblyRec getAssemblyRecByID(@Bind("assemblyID") String assemblyID);

    @SqlUpdate ("insert into AssemblyRec (assemblyID, name) values (:assemblyID, :name)")
    void insertAssemblyRec(@Bind("assemblyID") String assemblyID, @Bind("name") String name);

    @SqlUpdate ("update AssemblyRec set name = :name where assemblyID = :assemblyID")
    void updateAssemblyRec (@Bind("assemblyID") String assemblyID, @Bind("name") String name);

    //////////////////////////////
    // AssemblyDoc
    //////////////////////////////

//    Table "public.assemblydoc"
//    Column     |            Type             |       Modifiers
//    ---------------+-----------------------------+------------------------
//    assemblydocid | text                        | not null
//    assemblyid    | text                        |
//    current       | boolean                     | not null
//    doc           | text                        |
//    ts            | timestamp without time zone | not null default now()
//    Indexes:
//            "assemblydoc_pkey" PRIMARY KEY, btree (assemblydocid)
//    Foreign-key constraints:
//            "assemblydoc_assemblyid_fkey" FOREIGN KEY (assemblyid) REFERENCES assemblyrec(assemblyid)
//    Referenced by:
//    TABLE "productpart" CONSTRAINT "productpart_assemblydocid_fkey" FOREIGN KEY (assemblydocid) REFERENCES assemblydoc(assemblydocid)

    @SqlQuery("select assemblyDocID from assemblyDoc where assemblyID = :assemblyID and current = true")
    String getAssemblyDocID(String assemblyID);

    @SqlUpdate("insert into AssemblyDoc (assemblyID, assemblyDocID, current, doc) values (:assemblyID, :assemblyDocID, :current, :doc)")
    void insertAssemblyDoc(@Bind("assemblyID") String assemblyID, @Bind("assemblyDocID") String assemblyDocID, @Bind("current") Boolean current, @Bind("doc") String doc);

    @SqlQuery("select count(*) from AssemblyDoc where assemblyID = :assemblyID and assemblyDocID = :assemblyDocID")
    Integer isAssemblyDocPresent (@Bind("assemblyID") String assemblyID, @Bind("assemblyDocID") String assemblyDocID);

    @SqlUpdate("update AssemblyDoc set current = false where assemblyID = :assemblyID and assemblyDocID != :assemblyDocID")
    void demoteOthers (@Bind("assemblyID") String assemblyID, @Bind("assemblyDocID") String assemblyDocID);

    @SqlUpdate("update AssemblyDoc set current = true where assemblyID = :assemblyID and assemblyDocID = :assemblyDocID")
    void promoteToCurrent(@Bind("assemblyID") String assemblyID, @Bind("assemblyDocID") String assemblyDocID);

    @SqlQuery("select count(*) from AssemblyDoc where assemblyID = :assemblyID and assemblyDocID = :assemblyDocID and current = true")
    Integer isAssemblyDocCurrent (@Bind("assemblyID") String assemblyID, @Bind("assemblyDocID") String assemblyDocID);

    @SqlQuery("select * from AssemblyDoc where assemblyID = :assemblyID and current = true")
    @Mapper(AssemblyDocMapper.class)
    AssemblyDoc getCurrentAssemblyDoc(@Bind("assemblyID") String assemblyID);

    @SqlQuery("select * from AssemblyDoc where current = true")
    @Mapper(AssemblyDocMapper.class)
    List<AssemblyDoc> getAllCurrentAssemblyDocs();

    //////////////////
    // ProductPart
    //////////////////

    @SqlUpdate("delete from ProductPart")
    void deleteCatalog ();

    @SqlUpdate("insert into ProductPart (productID, partID, partDocID, assemblyID, assemblyDocID, function, linkable) " +
               "values (:productID, :partID, :partDocID, :assemblyID, :assemblyDocID, :function, :linkable)")
    void insertProductPart (@Bind("productID") String productID,
                            @Bind("partID") String partID, @Bind("partDocID") String partDocID,
                            @Bind("assemblyID") String assemblyID, @Bind("assemblyDocID") String assemblyDocID,
                            @Bind("function") String function, @Bind("linkable") Boolean linkable);

//    Table "public.productpart"
//    Column     |  Type   | Modifiers
//    ---------------+---------+-----------
//    productid     | text    | not null
//    partid        | text    | not null
//    partdocid     | text    |
//    assemblyid    | text    |
//    assemblydocid | text    |
//    function      | text    |
//    linkable      | boolean |

    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close();
}
