CREATE TABLE IF NOT EXISTS run_statistics (
    testSuite uuid,
    run uuid,
    result text,
    totalDurationMillis bigint,
    numPassed int,
    numFailed int,
    firstResultTime timestamp,
    lastResultTime timestamp,
    PRIMARY KEY (testSuite, run)
);