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
	PRIMARY KEY (testsuite, time, id)
) WITH CLUSTERING ORDER BY (time DESC);

CREATE TABLE IF NOT EXISTS runById (
	id uuid,
	testsuite uuid,
	revision text,
	time timestamp,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS revision (
    run uuid,
    revision text,
    time timestamp,
    author text,
    comment text,
    url text,
    PRIMARY KEY (run, time, revision)
) WiTH CLUSTERING ORDER BY (time DESC);

CREATE TABLE IF NOT EXISTS result (
	run uuid,
	testName text,
	retryNum int,
	passed boolean,
	durationMillis bigint,
	time timestamp,
	stackTrace varchar,
	PRIMARY KEY (run, testName, retryNum)
);

CREATE INDEX IF NOT EXISTS resultByTestname ON result (testname);

CREATE TABLE IF NOT EXISTS test_statistics (
    testSuite uuid,
    testName text,
    numPassed int,
    numFailed int,
    PRIMARY KEY (testSuite, testName)
);