package cmd;

import cmd.dao.CmdEventRec;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import mgr.CmdMgr;
import org.junit.*;
import org.skife.jdbi.v2.DBI;
import cmd.dao.CmdRec;
import cmd.dao.CmdSQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This test requires that Postgres 'catalog' database be started prior to running.
 */
public class CmdMgrIntegrationTest {

    private CmdSQL dao;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Cmd> cmdCleanup = new ArrayList<>();
    private List<CmdEventRec> eventCleanup = new ArrayList<>();

    @Before
    public void setup() {
        // TODO change this to work with a DW configuration
        DBI dbi = new DBI("jdbc:postgresql:catalog", "program", "fiddlesticks");
        dao = dbi.onDemand(CmdSQL.class);
    }

    @After
    public void cleanup() {
        for (Cmd cmd : cmdCleanup) {
            dao.deleteCmd(cmd.getID());
        }

        for (CmdEventRec rec : eventCleanup) {
            dao.deleteEvent(rec.getEventID());
        }
    }

    /**
     * test Cmd persistence and retrieval by ID
     */
    @Test
    public void testCmdBaseCRUD () {
        CmdMgr mgr = new CmdMgr(dao,"100100");

        mgr.addHandler(new CmdHandler<Cmd>() {
            @Override public String getCmdType() {
                return Cmd.class.getSimpleName();
            }
            @Override public void process(CmdMgr mgr, CmdEventRec eventRecord) {}
            @Override public void process(CmdMgr mgr, CmdRec cmdRecord, CmdEventRec eventRecord) {
                Cmd cmd = convert(cmdRecord);
            }
            @Override @SuppressWarnings("unchecked")
            public Cmd convert(CmdRec row) {
                try {
                    return mapper.readValue(row.getDoc(), Cmd.class);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        Cmd cmd = new Cmd("C-1000");
        cmd.log("this is step 1");
        cmd.log("proceeding to step 2");
        cmd.log("finally we have step 3");
        mgr.createCmd(cmd);
        cmdCleanup.add(cmd);

        cmd.setState(CmdState.completed);
        cmd.log("showing as completed");
        mgr.updateCmd(cmd);

        Cmd cmd2 = mgr.getCmd(cmd.getID());
        Assert.assertEquals(cmd.getID(), cmd2.getID());
        Assert.assertEquals(cmd.getState(), cmd2.getState());
        Assert.assertEquals(cmd.getLogEntries(), cmd2.getLogEntries());
    }

    /**
     * test Cmd persistence and retrieval by ID
     */
    @Test
    public void testCmdCRUD () {
        CmdMgr mgr = new CmdMgr(dao,"300300");

        mgr.addHandler(new CmdHandler<TestCmd>() {
            @Override public String getCmdType() {
                return TestCmd.class.getSimpleName();
            }
            @Override public void process(CmdMgr mgr, CmdEventRec eventRecord) {}
            @Override public void process(CmdMgr mgr, CmdRec cmdRecord, CmdEventRec eventRecord) {
            }
            @Override @SuppressWarnings("unchecked")
            public TestCmd convert(CmdRec row) {
                try {
                    return mapper.readValue(row.getDoc(), TestCmd.class);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        TestCmd cmd1 = new TestCmd("C-2000");
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

        Assert.assertTrue(cmd2.getState() != CmdState.completed);
        cmd2.setState(CmdState.completed);
        cmd2.someInt = 25000;
        mgr.updateCmd(cmd2);

        TestCmd cmd3 = mgr.getCmd(cmd2.getID());
        Assert.assertEquals(cmd2.getState(), cmd3.getState());
        Assert.assertEquals(cmd3.getState(), CmdState.completed);
        Assert.assertEquals(cmd3.someInt, 25000);
    }

    /**
     * Testing event persistence and retrieval by ID.
     */
    @Test
    public void testEventCRUD () {
        CmdMgr mgr = new CmdMgr(dao,"220220");
        CmdEventRec ce1 = new CmdEventRec("100", "whatever");
        ce1.setCmdTargetID("target");
        ce1.setCmdSourceID("source");

        mgr.createEvent(ce1);
        eventCleanup.add(ce1);

        CmdEventRec ce2 = mgr.getEvent(ce1.getEventID());
        Assert.assertEquals(ce1, ce2);

        ce2.setEventState(CmdEventRec.CmdEventState.completed);
        mgr.updateEvent(ce2);

        CmdEventRec ce3 = mgr.getEvent(ce2.getEventID());
        Assert.assertEquals(ce2, ce3);
    }

    @Test
    public void testScheduledWorkers () throws Exception {
        CmdMgr mgr = new CmdMgr(dao,"340503");
        mgr.start();
        Thread.sleep(1000);
        mgr.stop();
    }

    @Test
    public void testEventForNewCmdExec () throws Exception {
        CmdMgr mgr = new CmdMgr(dao,"78750");
        final String content = "hello new command!!!";

        mgr.addHandler(new CmdHandler<TestCmd>() {
            @Override public String getCmdType() { return TestCmd.class.getSimpleName();}
            @Override public void process(CmdMgr mgr, CmdRec cmdRecord, CmdEventRec eventRecord) {
                TestCmd cmd = convert (cmdRecord);
                cmd.log("what?");
                mgr.updateCmd(cmd);
            }
            @Override public void process(CmdMgr mgr, CmdEventRec event) {

                // TODO: look at the level of exposure we have to bad things happening here and the effects
                // TODO: stuff like changing the event state in the Manager come to mind

                TestCmd cmd = new TestCmd();
                cmd.setID (mgr.assignUniqueID());
                cmd.someString = content;
                cmd.setState(CmdState.completed);
                mgr.createCmd(cmd);

                event.setCmdTargetID(cmd.getID());
                event.setEventState(CmdEventRec.CmdEventState.completed);
                mgr.updateEvent(event);
            }

            @Override
            @SuppressWarnings("unchecked")
            public TestCmd convert(CmdRec row) {
                try {
                    return mapper.readValue(row.getDoc(), TestCmd.class);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        // set up an event and queue it
        String testEventID = "60000";
        CmdEventRec ce1 = new CmdEventRec(testEventID, TestCmd.class.getSimpleName());
        mgr.createEvent(ce1);

        // start manager threads
        // wait for the gears to turn
        mgr.start();

        // get the cmd record via the event
        for (int retry = 10; retry != 0; retry--) {
            CmdEventRec cer2 = mgr.getEvent(testEventID);

            if (cer2.getCmdTargetID() == null) {
                Thread.sleep(200);
                continue;
            }

            CmdRec cr2 = mgr.getTargetCmdForEvent(cer2);

            Assert.assertNotNull(cr2);
            Assert.assertEquals(cr2.getCmdState(), CmdState.completed.name());

            // get the Cmd implementation
            // check to see if the process method did its thing
            TestCmd cmd2 = mgr.getCmd(cr2.getCmdID());
            Assert.assertNotNull(cmd2);

            eventCleanup.add(cer2);
            cmdCleanup.add(cmd2);

            Assert.assertEquals(content, cmd2.someString);
            Assert.assertEquals(cmd2.getState(), CmdState.completed);
            return;
        }

        Assert.fail("too many retries");
    }

    /////////////////////////////
    // Cmds for Testing
    /////////////////////////////

    private static class TestCmd extends Cmd {
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

}
