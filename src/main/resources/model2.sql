
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
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(supplierDocID)
);

-- select r.supplierid, name, supplierdocid, current, ts from supplier r, supplierdoc d where r.supplierid = d.supplierid order by r.supplierid;

-----------------------------
-- Batches
-----------------------------

--create table BatchDoc (
--  batchDocID text,
--  supplierID text REFERENCES Supplier,
--  doc text,
--  ts timestamp NOT NULL DEFAULT now(),
--  PRIMARY KEY(batchDocID)
--);

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

CREATE INDEX PartRec_Name_Idx ON PartRec (name);
CREATE INDEX PartRec_Function_Idx ON PartRec (function);

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
-- select name, function, supplierid, d.partid, partdocid, current, ts from partrec r, partdoc d where r.partid = d.partid order by supplierid;

-----------------------------
-- Assembly & Product
-----------------------------

create table AssemblyRec (
  assemblyID text,
  name text,
  PRIMARY KEY (assemblyID)
);

create table AssemblyDoc (
  assemblyDocID text NOT NULL,
  assemblyID text REFERENCES AssemblyRec,
  current boolean NOT NULL,
  doc text,
  ts timestamp NOT NULL DEFAULT now(),
  PRIMARY KEY(assemblyDocID)
);

create table ProductPart (
  productID text, -- ties the product set rows together
  partID text REFERENCES PartRec, -- part of the product set
  partDocID text REFERENCES PartDoc, -- specific part definition
  assemblyID text REFERENCES AssemblyRec, -- combiner rule that resulted in this product set
  assemblyDocID text REFERENCES AssemblyDoc, -- specific combiner definition
  function text, -- function of the given part
  PRIMARY KEY (productID, partID)
);

CREATE INDEX ProductPart_Part_Idx ON ProductPart (partID);
CREATE INDEX ProductPart_PartDoc_Idx ON ProductPart (partDocID);
CREATE INDEX ProductPart_Function_Idx ON ProductPart (function);
CREATE INDEX ProductPart_Assembly_Idx ON ProductPart (assemblyID);
CREATE INDEX ProductPart_AssemblyDoc_Idx ON ProductPart (assemblyDocID);

-----------------------------
-- Customer
-----------------------------

create table CustomerRec (
  customerID text,
  name text,
  PRIMARY KEY(supplierID)
);

create table CustomerDoc (
  customerID text REFERENCES CustomerRec,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Quote
-----------------------------

create table QuoteRec (
  quoteID text,
  customerID text REFERENCES Customer,
  PRIMARY KEY (quoteID)
);

create table QuoterDoc (
  quoteID text REFERENCES QuoteRec,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Order
-----------------------------

create table OrderRec (
  orderID text,
  quoteID text REFERENCES OrderRec,
  customerID text REFERENCES CustomerRec,
  PRIMARY KEY (orderID)
);

create table OrderDoc (
  orderID text REFERENCES Order,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
);

-----------------------------
-- Journal
-----------------------------

create table JournalRec (
  journalID text,
  quoteID text REFERENCES QuoteRec,
  customerID text REFERENCES CustomerRec,
  orderID text REFERENCES OrderRec,
  type text,
  state text,
  doc text,
  ts timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (journalID)
);

