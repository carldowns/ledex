
----------------------------------------------------------------------
-----------------------------
-- Supplier
-----------------------------

create table Supplier (
  supplierID text,
  name text,
  PRIMARY KEY(supplierID)
);

CREATE INDEX SupplierName ON Supplier (lower(name));

create table SupplierDoc (
  supplierDocID text NOT NULL,
  supplierID text REFERENCES Supplier,
  current boolean NOT NULL,
  doc text NOT NULL,
  --md5 text not null,
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(supplierDocID)
);

create table BatchDoc (
  batchDocID text,
  supplierID text REFERENCES Supplier,
  doc text,
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(batchDocID)
);

-----------------------------
-- Part
-----------------------------

create table PartRec (
  partID text,
  supplierID text REFERENCES Supplier,
  name text,
  function text,
  PRIMARY KEY (partID)
);

--CREATE UNIQUE INDEX PartName ON PartRec COLUMN name;

create table PartDoc (
  partDocID text NOT NULL,
  partID text REFERENCES PartRec,
  --batchDocID text REFERENCES BatchDoc,
  current boolean NOT NULL,
  doc text,
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(partDocID)
);

-- select name, function, d.partid, partdocid, current, ts from partrec r, partdoc d where r.partid = d.partid;

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

