CREATE KEYSPACE IF NOT EXISTS teststore WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE teststore;

CREATE TABLE IF NOT EXISTS test (
	id text,
	name text,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testsuite (
	id text,
	name text,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS run (
	id text,
	testSuite text,
	revision text,
	PRIMARY KEY (id)
);