
drop table CmdRec;
create table CmdRec (
  cmdID text NOT NULL,
  cmdType text NOT NULL,
  cmdState text NOT NULL,
  doc text NOT NULL,
  ts timestamp NOT NULL DEFAULT now(),
  userID text,
  PRIMARY KEY(cmdID)
);

CREATE INDEX CmdRec_State_Idx ON CmdRec (cmdState);

drop table CmdEventRec;
create table CmdEventRec (
  eventID text NOT NULL,
  eventType text NOT NULL,
  eventState text NOT NULL,
  eventDue timestamp NOT NULL DEFAULT now(),
  cmdSourceID text,
  cmdTargetID text,
  ts timestamp NOT NULL DEFAULT now(),
  userID text,
  PRIMARY KEY(eventID)
);

CREATE INDEX CmdEventRec_State_Idx ON CmdEventRec (eventState);

drop table CmdMutexRec;
create table CmdMutexRec (
  mutexID text NOT NULL,
  mutexType text NOT NULL,
  mutexOwner text NOT NULL,
  expireTs timestamp NOT NULL DEFAULT now() + '1 minute',
  PRIMARY KEY(mutexID, mutexType)
);

CREATE INDEX CmdMutexRec_Owner_Idx ON CmdMutexRec (mutexID, mutexType, mutexOwner);

-- drop table CmdTimerRec;
-- create table CmdTimerRec (
--   timerID text NOT NULL,
--   timerState text NOT NULL,
--   timerCriteria text not NULL,
--   timerRepeats boolean not NULL,
--   timerDue timestamp NOT NULL,
--   ts timestamp NOT NULL DEFAULT now(),
--   userID text,
--   PRIMARY KEY(timerID)
-- );
--
-- CREATE INDEX CmdTimerRec_State_Idx ON CmdTimerRec (timerState);
-- CREATE INDEX CmdTimerRec_When_Idx ON CmdTimerRec (timerDue);
