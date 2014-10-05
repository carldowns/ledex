
----------------------------------------------------------------------
-----------------------------
-- Supplier
-----------------------------

create table Supplier (
  supplierID text,
  name text,
  PRIMARY KEY(supplierID)
);

create table SupplierDoc (
  supplierID text REFERENCES Supplier,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now()
);

create table SupplierBatch (
  batchID text,
  supplierID text REFERENCES Supplier,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY(batchID)
);

-----------------------------
-- Part
-----------------------------

create table Part (
  partID text,
  supplierID text REFERENCES Supplier,
  name text,
  fcn text,
  PRIMARY KEY (partID)
);

create table PartDoc (
  partID text REFERENCES Part,
  batchID text REFERENCES Batch,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now()
);


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
  jobID text,
  quoteID text REFERENCES Quote,
  custID text REFERENCES Customer,
  PRIMARY KEY (jobID)
);

create table JobDoc (
  job text REFERENCES Job,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

