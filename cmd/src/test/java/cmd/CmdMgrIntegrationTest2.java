package cmd;

import ch.qos.logback.classic.Logger;
import cmd.dao.CmdRec2;
import cmd.dao.CmdSQL2;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This test requires that Postgres 'catalog' database be started prior to running.
 */
public class CmdMgrIntegrationTest2 {

    private static final Logger _log = (Logger) LoggerFactory.getLogger(CmdMgrIntegrationTest2.class);
    private CmdSQL2 dao;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Cmd2> cmdCleanup = new ArrayList<>();
    private Boolean cleanupRecords = true;

    @Before
    public void setup() {
        // TODO change this to work with a DW configuration
        DBI dbi = new DBI("jdbc:postgresql:catalog", "program", "fiddlesticks");
        dao = dbi.onDemand(CmdSQL2.class);
    }

    @After
    public void cleanup() {
        if (!cleanupRecords) {
            return;
        }

        for (Cmd2 cmd : cmdCleanup) {
            dao.deleteCmd(cmd.getID());
        }
    }

    /**
     * test Cmd persistence and retrieval by ID
     */
    @Test
    public void testCmdBaseCRUD () {
        CmdMgr2 mgr = new CmdMgr2(dao,"100100");

        mgr.addHandler(new ICmdHandler2<Cmd2>() {
            @Override
            public String getCmdType() {
                return Cmd2.class.getSimpleName();
            }

            @Override
            public void process(CmdMgr2 mgr, CmdRec2 cmdRecord) {
                Cmd2 cmd = convert(cmdRecord);
            }

            @Override
            @SuppressWarnings("unchecked")
            public Cmd2 convert(CmdRec2 row) {
                try {
                    return mapper.readValue(row.getDoc(), Cmd2.class);
                } catch (Exception e) {
                    throw new CmdRuntimeException2(e.getMessage());
                }
            }
        });

        Cmd2 cmd = new Cmd2("C-1000");
        cmd.log("this is step 1");
        cmd.log("proceeding to step 2");
        cmd.log("finally we have step 3");
        mgr.createCmd(cmd);
        cmdCleanup.add(cmd);

        cmd.setState(CmdState2.completed);
        cmd.log("showing as completed");
        mgr.updateCmd(cmd);

        Cmd2 cmd2 = mgr.getCmd(cmd.getID());
        Assert.assertEquals(cmd.getID(), cmd2.getID());
        Assert.assertEquals(cmd.getState(), cmd2.getState());
        Assert.assertEquals(cmd.getLogEntries(), cmd2.getLogEntries());
    }

    /**
     * test Cmd persistence and retrieval by ID
     */
    @Test
    public void testCmdCRUD () {
        CmdMgr2 mgr = new CmdMgr2(dao,"300300");

        mgr.addHandler(new ICmdHandler2<TestCmd>() {
            @Override public String getCmdType() {
                return TestCmd.class.getSimpleName();
            }
            @Override public void process(CmdMgr2 mgr, CmdRec2 cmdRecord) {
            }
            @Override @SuppressWarnings("unchecked")
            public TestCmd convert(CmdRec2 row) {
                try {
                    return mapper.readValue(row.getDoc(), TestCmd.class);
                } catch (Exception e) {
                    throw new CmdRuntimeException2(e.getMessage());
                }
            }
        });

        TestCmd cmd1 = new TestCmd(mgr.newID());
        cmd1.someInt = 300;
        cmd1.someInteger = 5000;
        cmd1.someFloat = 0.1F;
        cmd1.someString = "whatever";
        cmd1.someMap.put("uno", 1);
        cmd1.someMap.put("hundred", 100);
        cmd1.someMap.put("million", 1000000);
        mgr.createCmd(cmd1);
        cmdCleanup.add(cmd1);

        TestCmd cmd2 = mgr.getCmd(cmd1.getID());
        Assert.assertEquals(cmd1.someInt, cmd2.someInt);
        Assert.assertEquals(cmd1.someInteger, cmd2.someInteger);
        Assert.assertEquals(cmd1.someFloat, cmd2.someFloat);
        Assert.assertEquals(cmd1.someMap, cmd2.someMap);

        Assert.assertTrue(cmd2.getState() != CmdState2.completed);
        cmd2.setState(CmdState2.completed);
        cmd2.someInt = 25000;
        mgr.updateCmd(cmd2);

        TestCmd cmd3 = mgr.getCmd(cmd2.getID());
        Assert.assertEquals(cmd2.getState(), cmd3.getState());
        Assert.assertEquals(cmd3.getState(), CmdState2.completed);
        Assert.assertEquals(cmd3.someInt, 25000);
    }

    @Test
    public void testWorkerThreads () throws Exception {
        CmdMgr2 mgr = new CmdMgr2(dao,"340503");
        mgr.start();
        Thread.sleep(1000);
        mgr.stop();
    }

    @Test
    public void testCmdRescheduling() throws Exception {
        CmdMgr2 mgr = new CmdMgr2(dao, "78750");
        _log.info("testCmdRescheduling");
        final int expectedValue = 2;

        mgr.addHandler(new ICmdHandler2<TestCmd>() {
            @Override
            public String getCmdType() {
                return TestCmd.class.getSimpleName();
            }

            @Override
            public void process(CmdMgr2 mgr, CmdRec2 cmdRecord) {
                TestCmd cmd = convert(cmdRecord);

                if (cmd.someInt == expectedValue) {
                    cmd.setState(CmdState2.completed);
                    mgr.updateCmd(cmd);
                    _log.info("done");
                }
                else {
                    cmd.setState(CmdState2.waiting);
                    cmd.someInt++;
                    mgr.scheduleCmd(cmd, 10L);
                    _log.info("incremented to {}", cmd.someInt);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            public TestCmd convert(CmdRec2 row) {
                try {
                    return mapper.readValue(row.getDoc(), TestCmd.class);
                } catch (Exception e) {
                    throw new CmdRuntimeException2(e.getMessage());
                }
            }
        });

        // start manager threads
        mgr.start();

        String cmdID = mgr.newID();
        TestCmd cmd = new TestCmd(cmdID);
        mgr.createCmd(cmd);
        cmdCleanup.add(cmd);

        // get the cmd record via the event
        for (int retry = 10; retry > 0; retry--) {
            CmdRec2 result = mgr.getCmdRecord(cmdID);
            _log.info("result CmdRec2 state {}", result.getCmdState());
            if (result.getCmdState().equals(CmdState2.completed.name())) {
                TestCmd test = mgr.getCmd(cmdID);
                Assert.assertEquals(expectedValue, test.someInt);
                return; // success
            }
            Thread.sleep(1000);
        }
        Assert.fail("scheduled cmd not updated");
    }

    /////////////////////////////
    // Cmds for Testing
    /////////////////////////////

    private static class TestCmd extends Cmd2 {
        @JsonProperty int someInt;
        @JsonProperty Integer someInteger;
        @JsonProperty Float someFloat;
        @JsonProperty String someString;
        @JsonProperty Map<String, Integer> someMap = Maps.newHashMap();
        public TestCmd () {}
        public TestCmd (String ID) {
            super(ID);
        }
    }

    private static class TestCmd2 extends Cmd2 {
        @JsonProperty int value;
        public TestCmd2() {}
        public TestCmd2(String ID) {
            super(ID);
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
    }

}
