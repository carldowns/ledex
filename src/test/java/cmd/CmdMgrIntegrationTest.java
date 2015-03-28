package cmd;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import mgr.CmdMgr;
import org.junit.*;
import org.skife.jdbi.v2.DBI;
import cmd.Cmd;
import cmd.CmdEvent;
import cmd.CmdHandler;
import cmd.CmdState;
import cmd.dao.CmdRow;
import cmd.dao.CmdSQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * This test requires that Postgres 'catalog' database be started prior to running.
 */
public class CmdMgrIntegrationTest {

    private CmdSQL dao;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Cmd> cleanup = new ArrayList<Cmd>();

    @Before
    public void setup() {
        // TODO change this to work with a DW configuration
        DBI dbi = new DBI("jdbc:postgresql:catalog", "program", "fiddlesticks");
        dao = dbi.onDemand(CmdSQL.class);
    }

    @After
    public void cleanup() {
        for (Cmd cmd : cleanup) {
            dao.deleteCmd(cmd.getID());
        }
    }

    @Test
    public void basicCmdStoreAndRetrieveTest () {
        CmdMgr mgr = new CmdMgr(dao,"100100");

        mgr.addHandler(new CmdHandler<Cmd>() {

            @Override
            public String getCmdType() {
                return Cmd.class.getSimpleName();
            }

            @Override
            public void process(CmdRow row, CmdEvent event) {
                Cmd cmd = convert(row);
            }

            @Override
            @SuppressWarnings("unchecked")
            public Cmd convert(CmdRow row) {
                try {
                    return mapper.readValue(row.getDoc(), Cmd.class);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        Cmd cmd = new Cmd();
        cmd.log("this is step 1");
        cmd.log("proceeding to step 2");
        cmd.log("finally we have step 3");
        mgr.saveCmd(cmd);
        cleanup.add(cmd);

        cmd.setState(CmdState.completed);
        cmd.log("showing as completed");
        mgr.saveCmd(cmd);

        Cmd cmd2 = mgr.getCmd(cmd.getID());
        Assert.assertEquals(cmd.getID(), cmd2.getID());
        Assert.assertEquals(cmd.getState(), cmd2.getState());
        Assert.assertEquals(cmd.getLogEntries(), cmd2.getLogEntries());
    }

    @Test
    public void extendedCmdStoreAndRetrieveTest () {
        CmdMgr mgr = new CmdMgr(dao,"100100");

        mgr.addHandler(new CmdHandler<TestCmd>() {

            @Override
            public String getCmdType() {
                return TestCmd.class.getSimpleName();
            }

            @Override
            public void process(CmdRow row, CmdEvent event) {
                TestCmd cmd = convert(row);
            }

            @Override
            @SuppressWarnings("unchecked")
            public TestCmd convert(CmdRow row) {
                try {
                    return mapper.readValue(row.getDoc(), TestCmd.class);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        TestCmd cmd = new TestCmd();
        cmd.someInt = 300;
        cmd.someInteger = 5000;
        cmd.someFloat = 0.1F;
        cmd.someString = "whatever";
        cmd.someMap.put("uno", 1);
        cmd.someMap.put("hundred", 100);
        cmd.someMap.put("million", 1000000);
        mgr.saveCmd(cmd);
        cleanup.add(cmd);

        TestCmd cmd2 = mgr.getCmd(cmd.getID());
        Assert.assertEquals(cmd.someInt, cmd2.someInt);
        Assert.assertEquals(cmd.someInteger, cmd2.someInteger);
        Assert.assertEquals(cmd.someFloat, cmd2.someFloat);
        Assert.assertEquals(cmd.someMap, cmd2.someMap);
    }

    private static class TestCmd extends Cmd {
        @JsonProperty int someInt;
        @JsonProperty Integer someInteger;
        @JsonProperty Float someFloat;
        @JsonProperty String someString;
        @JsonProperty Map<String, Integer> someMap = Maps.newHashMap();

        public TestCmd () {
            super();
        }
    }

    @Test
    public void extendedCmdHandlerTest () {
        CmdMgr mgr = new CmdMgr(dao,"100100");

        mgr.addHandler(new CmdHandler<TestCmd2>() {

            @Override
            public String getCmdType() {
                return TestCmd2.class.getSimpleName();
            }

            @Override
            public void process (CmdRow row, CmdEvent event) {
                TestCmd2 cmd = convert (row);
            }

            @Override
            @SuppressWarnings("unchecked")
            public TestCmd2 convert (CmdRow row) {
                try {
                    return mapper.readValue(row.getDoc(), TestCmd2.class);
                }
                catch (Exception e) {
                    throw new RuntimeException (e.getMessage());
                }
            }
        });

        TestCmd2 cmd = new TestCmd2();
        cmd.someInt = 555;
        mgr.saveCmd(cmd);
        cleanup.add(cmd);

        CmdRow row = dao.getCmdRow(cmd.getID());
        TestCmd2 cmd2 = mgr.getCmdHandler(row).convert(row);

        Assert.assertEquals(cmd.someInt, cmd2.someInt);

    }

    private static class TestCmd2 extends Cmd {
        @JsonProperty int someInt;

        public TestCmd2 () {
            super();
        }
    }
}
