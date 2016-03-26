CREATE KEYSPACE IF NOT EXISTS teststore WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE teststore;

CREATE TABLE IF NOT EXISTS test (
	id text,
	name text,
	PRIMARY KEY (id)
);