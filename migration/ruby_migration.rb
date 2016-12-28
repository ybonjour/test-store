require 'cassandra'

host = ARGV[0]
keyspace = ARGV[1]
migration_file = ARGV[2]

cluster = Cassandra.cluster(hosts: [host])
session = cluster.connect(keyspace)

load(migration_file)
migrate(session)