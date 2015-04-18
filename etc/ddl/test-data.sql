
insert into supplier (supplierID, name) values ('S1', 'Yancheng Golden Idea Optical Tech., LTD');
insert into supplier (supplierID, name) values ('S2', 'Licht Technologies., LTD');
insert into supplier (supplierID, name) values ('S3', 'Novelty');

-----------------------------
-----------------------------

insert into set (setID, setName) values ('LS1000','Basic LED Strip Set');
insert into set (setID, setName) values ('LS1001','Basic LED Flex Set');
insert into set (setID, setName) values ('LS1002','Basic LED Cluster Set');

insert into function (setID, fcnID, fcnName,fcnType) 
values ('LS1000','A','Power Source','POWER');

insert into function (setID, fcnID, fcnName,fcnType) 
values ('LS1000','B','LED Light Source','LED_LIGHT');

insert into function (setID, fcnID, fcnName,fcnType) 
values ('LS1000','C','Wire Lead','WIRE_LEAD');

insert into function (setID, fcnID, fcnName,fcnType)
values ('LS1000','D','Connector','CONNECTOR');

insert into function (setID, fcnID, fcnName,fcnType)
values ('LS1000','E','Switch','SWITCH');

insert into function (setID, fcnID, fcnName,fcnType)
values ('LS1000','F','Plug','PLUG');



insert into function (setID, fcnID, fcnName,fcnType) 
values ('LS1001','A','Power Source','POWER');

insert into function (setID, fcnID, fcnName,fcnType) 
values ('LS1001','B','LED Light Source','LED_LIGHT');



-- unused

insert into function (fcnID,fcnName,fcnType) 
values ('','Sensor','SENSOR');

insert into function (fcnID,fcnName,fcnType) 
values ('','Motor','MOTOR');

insert into function (fcnID,fcnName,fcnType) 
values ('','Wire Harness','WIRE_HARNESS');


insert into function (fcnID,fcnName,fcnType) 
values ('','Button','BUTTON');

-- rules

insert into rule (setID, fcnID, ruleID, doc) 
values ('LS1000','A','whatever','{ a1:value1, a2:value2 }');

insert into rule (setID, fcnID, ruleID, doc) 
values ('LS1000','A','nonsense','{ b1:value1, b2:value2 }');

