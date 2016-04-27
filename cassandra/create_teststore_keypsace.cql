CREATE KEYSPACE IF NOT EXISTS teststore WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE teststore;

CREATE TABLE IF NOT EXISTS testsuite (
	id uuid,
	name text,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS run (
	id uuid,
	testsuite uuid,
	revision text,
	time timestamp,
	PRIMARY KEY (id)
);

create index runsByTestsuite ON run (testsuite);

CREATE TABLE IF NOT EXISTS result (
	run uuid,
	testName text,
	retryNum int,
	passed boolean,
	durationMillis bigint,
	PRIMARY KEY (run, testName, retryNum)
);