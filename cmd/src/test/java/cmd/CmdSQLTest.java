package cmd;

import cmd.dao.CmdMutexRec;
import cmd.dao.CmdSQL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.util.List;
import java.util.Map;


/**
 *
 */
public class CmdSQLTest {

    private CmdSQL dao;
    private ObjectMapper mapper = new ObjectMapper();
    private List<CmdMutexRec> mutexes = Lists.newArrayList();

    @Before
    public void setup() {
        // TODO change this to work with a DW configuration
        DBI dbi = new DBI("jdbc:postgresql:catalog", "program", "fiddlesticks");
        dao = dbi.onDemand(CmdSQL.class);
    }

    @After
    public void cleanup() {
        for (CmdMutexRec rec : mutexes) {
            dao.deleteMutex(rec.getProcessID(), rec.getProcessID(), rec.getType());
        }
    }

    @Test
    public void testMutexRefresh() {
        String processID = "55555";
        final Integer mutexCount = 10;
        String type = "test";

        // add some mutexes
        for (int x = 0; x < mutexCount; x++) {
            String mutexID = type + x;
            Integer updated = dao.insertMutex(processID, mutexID, type);
            Assert.assertTrue(updated.equals(1));

            // check to see they are there
            CmdMutexRec mRec = dao.selectMutex(processID, mutexID, type);
            mutexes.add(mRec);

            // time skew for timestamps
            Thread.yield();
        }

        //refresh them
        Integer updated = dao.refreshMutexes(processID);
        Assert.assertTrue(updated.equals(mutexCount));

        // verify they were all refreshed
        for (CmdMutexRec mRec : mutexes) {
            CmdMutexRec mRecRefreshed = dao.selectMutex(processID, mRec.getMutexID(), mRec.getType());
            Assert.assertTrue(mRec.getExpireTs() < mRecRefreshed.getExpireTs());
        }
    }
}
