
create table CmdRec (
  cmdID text NOT NULL,
  cmdType text NOT NULL,
  cmdState text NOT NULL,
  doc text NOT NULL,
  ts timestamp NOT NULL DEFAULT now(),
  userID text,
  PRIMARY KEY(cmdID)
);

CREATE INDEX Cmd_State_Idx ON CmdRec (cmdState);

create table CmdEvent (
  eventID text NOT NULL,
  eventType text NOT NULL,
  eventState text NOT NULL,
  targetCmdID text NOT NULL,
  sourceCmdID text,
  ts timestamp NOT NULL DEFAULT now(),
  userID text,
  PRIMARY KEY(eventID)
);

CREATE INDEX CmdEvent_State_Idx ON CmdEvent (eventState);

create table CmdTimer (
  timerID text NOT NULL,
  timerState text NOT NULL,
  timerCriteria text not NULL,
  timerRepeats boolean not NULL,
  timerDue timestamp NOT NULL,
  ts timestamp NOT NULL DEFAULT now(),
  userID text,
  PRIMARY KEY(timerID)
);

CREATE INDEX CmdTimer_State_Idx ON CmdTimer (timerState);
CREATE INDEX CmdTimer_When_Idx ON CmdTimer (timerDue);

create table CmdMutex (
  mutexID text NOT NULL,
  mutexType text NOT NULL,
  mutexOwner text NOT NULL,
  expireTs timestamp NOT NULL DEFAULT now() + '1 minute',
  PRIMARY KEY(mutexID, mutexType, mutexOwner)
);

