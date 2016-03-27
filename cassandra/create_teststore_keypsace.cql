CREATE KEYSPACE IF NOT EXISTS teststore WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE teststore;

CREATE TABLE IF NOT EXISTS test (
	id uuid,
	name text,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testsuite (
	id uuid,
	name text,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS run (
	id uuid,
	testSuite uuid,
	revision text,
	PRIMARY KEY (id)
);