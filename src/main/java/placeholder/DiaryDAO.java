package placeholder;

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
public interface DiaryDAO {
    static String CREATE_PERSON = 
            "create table PERSON ("
            + "personID integer NOT NULL, "
            + "name text NOT NULL, "
            + "PRIMARY KEY (personID)"
            + ");";
    
    /**
     * PERSON 
     */
    
    @SqlUpdate(CREATE_PERSON)
    void createPersonTable();

    @SqlUpdate("insert into person (name) values (:name)")
    void insertPerson(@Bind("name") String name);

    @SqlQuery("select name from person where id = :id")
    String findNameByPersonId(@Bind("id") int id);
    
    @SqlQuery("select name from person where id > :from and id < :to")
    List<String> findNamesBetween(@Bind("from") int from, @Bind("to") int to);
    
    @SqlQuery("select name from person")
    Iterator<String> findAllNames();

    @SqlQuery("select * from person")
    @Mapper(PersonRepMapper.class)
    Iterator<PersonRep> findAll();

    /**
     * ENTRY
     */
    
    static String CREATE_DIARY = 
            "create table DIARY ("
            + "entryID integer NOT NULL DEFAULT,"
            + "personID integer REFERENCES person ON DELETE CASCADE,"
            + "entry text NOT NULL,"
            + "timestamp timestamp with time zone NOT NULL DEFAULT now(),"
            + "PRIMARY KEY (entryID)"
            + ");";

    @SqlUpdate(CREATE_DIARY)
    void createDiaryTable();

    @SqlUpdate("insert into entry (personID, content) values (:personID, :content)")
    void insertEntry(@Bind("personID") String ID, @Bind("content") String content);

}
