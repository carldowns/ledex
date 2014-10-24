
----------------------------------------------------------------------
-----------------------------
-- Supplier
-----------------------------


create table Supplier (
  supplierID text NOT NULL, -- externally assigned
  name text NOT NULL, -- supplier name
  doc text NOT NULL, -- supplier doc in Json format
  docID text NOT NULL, -- UUID for the doc, written in the doc as well, tracks a version of the supplier detail
  current boolean NOT NULL DEFAULT TRUE, -- indicates which doc is active for the supplier (if multiple versions present)
  hash number NOT NULL, -- doc checksum enables avoidance of unnecessary writes
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(supplierID, supplierUID)
);

-----------------------------
-- Supplier
-----------------------------

create table Batch (
  supplierID text REFERENCES Supplier,
  batchID text NOT NULL,
  name text NOT NULL,
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(supplierID, batchUID)
);

-----------------------------
-- Part
-----------------------------

create table Part (
  supplierID text REFERENCES Supplier, -- foreign key
  batchID text REFERENCES BatchDoc, -- foreign key
  partID text, -- externally assigned
  name text NOT NULL,
  type text NOT NULL,
  function text NOT NULL,
  doc text NOT NULL, -- part doc in Json format
  docID text NOT NULL, -- UUID for the doc, written in the doc as well, tracks a version of the part detail
  current boolean NOT NULL DEFAULT TRUE,
  hash number NOT NULL,
  ts timestamp  NOT NULL DEFAULT now()
  PRIMARY KEY (partID, partUID)
);

-- index by name
-- index by type
-- index by supplierID
-- index by function

-- Indices, later replaced with Elastic Search indexing

--create table PartByNameIndex (
--  partDocID text REFERENCES Part,
--  name text,
--  PRIMARY KEY (name, partID)
--);
--
--create table PartByFunctionIndex (
--  partDocID text REFERENCES Part,
--  function text,
--  PRIMARY KEY (function, partID)
--);
--
--create table PartByTypeIndex (
--  partDocID text REFERENCES Part,
--  type text,
--  PRIMARY KEY (type, partID)
--);
--
--create table PartByTypeFunctionIndex (
--  partDocID text REFERENCES Part,
--  type text,
--  function text,
--  PRIMARY KEY (type, function, partID)
--);

-----------------------------
-- Assembly 
-----------------------------

create table Assembly (
  asmID text,
  custID text REFERENCES customer,
  name text,
  createdOn timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (asmID)
);

-- may need this
--create table AssemblyPart (
--  asmID text REFERENCES Assembly,
--  partID text REFERENCES Part,
--  PRIMARY KEY (asmID, partID)
--);


create table AssemblyDoc (
  asmID text REFERENCES Assembly,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Customer
-----------------------------

create table Customer (
  custID text,
  name text,
  PRIMARY KEY(supplierID)
);

create table CustomerDoc (
  custID text REFERENCES Customer,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Quote
-----------------------------

create table Quote (
  quoteID text,
  custID text REFERENCES Customer,
  PRIMARY KEY (quoteID)
);

create table QuoterDoc (
  quoteID text REFERENCES Quote,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Order
-----------------------------

create table Order (
  orderID text,
  quoteID text REFERENCES Quote,
  custID text REFERENCES Customer,
  PRIMARY KEY (orderID)
);

create table OrderDoc (
  orderID text REFERENCES Order,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Job
-----------------------------

create table Job (
  projectID text,
  quoteID text REFERENCES Quote,
  custID text REFERENCES Customer,
  PRIMARY KEY (jobID)
);

create table JobDoc (
  job text REFERENCES Job,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

