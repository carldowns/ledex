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


    //////////////////////////////
    // AssemblyDoc
    //////////////////////////////

    @SqlQuery("select * from AssemblyDoc")
    @Mapper(AssemblyDocMapper.class)
    List<AssemblyDoc> getAllCurrentAssemblyDocs();

    @SqlQuery("select assemblyDocID from partDoc where assemblyID = :assemblyID and current = true")
    String getAssemblyDocID(String assemblyID);


    /**
     * this close method is necessary for JDBI Sql Object to close a connection properly
     */
    public void close();
}
