CREATE TABLE IF NOT EXISTS migrations (
    baseVersion int,
    migration int,
    migrationFile text,
    executedAt timestamp,
    executedBy text,
    PRIMARY KEY (baseVersion, migration)
) WITH CLUSTERING ORDER BY (migration DESC);