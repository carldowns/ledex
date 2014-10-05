
----------------------------------------------------------------------
-----------------------------
-- Supplier
-- Describes the supplier, address, contacts, etc
-- Journal table to preserve changes over time
-----------------------------

create table Supplier (
  supplierID text,
  supplierName text,
  PRIMARY KEY(supplierID)
);

create table SupplierJournal (
  supplierID text REFERENCES supplier,
  doc text,
  postedOn timestamp with time zone NOT NULL DEFAULT now()
);

-----------------------------
-- Customer
-- Describes the customer, address, contacts, notes, etc
-- Journal table to preserve changes over time
-----------------------------

create table Customer (
  custID text,
  custName text,
  PRIMARY KEY (custID)
);

create table CustomerJournal (
  custID text references customer,
  doc text,
  postedOn timestamp with time zone NOT NULL DEFAULT now()
);

-----------------------------
-- Job
-- Tracks the interactions during the processing of the job.
-- Journal table to preserve changes over time
-----------------------------

create table Job (
  jobID text,
  orderID text REFERENCES customerOrder,
  quoteID text REFERENCES quote,
  custID text REFERENCES customer,
  jobName text,
  PRIMARY KEY (jobID)
);

create table JobJournal (
  jobID text references job,
  doc text,
  postedOn timestamp with time zone NOT NULL DEFAULT now()
);

----------------------------------------------------------------------
-----------------------------
-- Batch
-- Corresponds to an upload from supplier
-- Immutable
-----------------------------

create table SupplierBatch (
  batchID text,
  supplierID text REFERENCES supplier,
  uploadedOn timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (supplierID, batchID)
);

-----------------------------
-- Quote
-- Pricing and terms related to one or more assembly, at quantity, with 
-- an expiration
-- Immutable
-----------------------------

create table CustomerQuote (
  quoteID text,
  custID text REFERENCES customer,
  doc text,
  createdOn timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (quoteID)
);

-----------------------------
-- Order
-- A quote that has been converted to an actual order.
-- Immutable
-----------------------------

create table CustomerOrder (
  orderID text,
  quoteID text REFERENCES customerquote,
  custID text REFERENCES customer,
  doc text,
  createdOn timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (orderID)
);

----------------------------------------------------------------------
-----------------------------
-- Set
-- Defines a set of functions and rules that must be met in order to 
-- build part assembly.  
-- Immutable
-----------------------------

create table Set (
  setID text,
  setName text,
  PRIMARY KEY (setID)
);

-----------------------------
-- Function
-- A function acts like a role that is to be filled by some part.  It could
-- be power, switch, sensor, etc.  
-- Immutable
-----------------------------

create table Function (
  fcnID text,
  fcnName text,
  fcnType text,
  setID text REFERENCES Set ON DELETE CASCADE,
  PRIMARY KEY (setID, fcnID)
);

-----------------------------
-- Rule
-- A computational rule that can be applied to see if a given assembly is 
-- valid.
-- Immutable
-----------------------------

create table Rule (
  ruleID text,
  setID text,
  fcnID text,
  doc text,
  FOREIGN KEY (setID, fcnID) REFERENCES Function ON DELETE CASCADE,
  PRIMARY KEY (ruleID)
);

-----------------------------
-- Assembly 
-- a set of parts that together satisfies the rules of a set.
-- Can be thought of as a set instance, with specific parts selected.
-- Assemblies will be fielded as examples of what is possible.
-- Customers can build assemblies to see what is possible, and save them.
-- Immutable.
-----------------------------

create table Assembly (
  asmID text,
  setID text REFERENCES set,
  custID text REFERENCES customer,
  asmName text,
  createdOn timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (asmID)
);


-----------------------------
-- Part
-- Defines an atomic part that can serve a function in a set.
-- Immutable
-----------------------------

create table Part (
  partID text,
  supplierID text REFERENCES supplier,
  batchID text REFERENCES supplierbatch,
  partName text,
  skuLabel text,
  fcnType text,
  PRIMARY KEY (partID, supplierID)
);

-----------------------------
-- PartOption
-- Options for a part, choices like color, etc.
-- Immutable
-----------------------------

create table partOption (
  partID text REFERENCES part ON DELETE CASCADE,
  skuLabel text,
);

-----------------------------
-- PartQuant
-- Quantifiers appropriate for a part, such as number of inches, etc
-- Specifies a unit, min, max, increment, and SKU label.
-- Immutable
-----------------------------

create table partQuant (
  partID text REFERENCES part ON DELETE CASCADE,
  skuLabel text,
);

-----------------------------
-- PartPrice
-- Pricing for the part at X quantity
-- Immutable
-----------------------------

create table partPrice (
  partID text REFERENCES part ON DELETE CASCADE,
);

-----------------------------
-- PartDim
-- Shipping weight and dimensions used to calculate shipping costs
-- Immutable
-----------------------------

create table partDim (
  partID text REFERENCES part ON DELETE CASCADE,
);


