Migration = Struct.new(:order, :file, :type)
def ordered_migration_files(path)
    (Dir.glob(path + "*.cql") + Dir.glob(path + "*.rb")).map { |file_path|
        file_name = File.basename(file_path)
        tokens = file_name.split("_")
        migration = Migration.new
        migration.order = Integer(tokens.first)
        migration.file = file_path
        migration.type = File.extname(file_name)
        migration
    }.sort_by { |m| m.order }
end

def get_last_migration(host, keyspace, base_version)
    # This should be replaced with proper access to cassandra
    result = `cqlsh #{host} -k #{keyspace} -e "select migration from migrations where baseversion=#{base_version} limit 1" | grep -A 2 'migration' | tail -1 | tr -d '[[:space:]]'`
    if is_integer(result)
        result.to_i
    else
        0
    end
end

def is_integer(s)
    s.to_i.to_s == s
end

def create_migration_table(host, keyspace)
    `cqlsh #{host} -k #{keyspace} -f create_migrations.cql`
end

def execute_migration(host, keyspace, migration)
    if(migration.type == ".cql")
        system("cqlsh #{host} -k #{keyspace} -f #{migration.file}")
    elsif(migration.type == ".rb")
        system("ruby ruby_migration.rb #{host} #{keyspace} #{migration.file}")
    else
        puts "Migration #{migration.file} has unknown type #{migration.type}"
    end
end

def execute(host, keyspace, base_version, migration)
    success = execute_migration(host, keyspace, migration)
    unless success
        raise "Migration #{migration.file} failed"
    end
    insert = "insert into migrations (baseVersion, migration, executedAt, executedBy, migrationFile) VALUES(#{base_version}, #{migration.order}, dateof(now()), 'migration_script', '#{migration.file}')"
    `cqlsh #{host} -k #{keyspace} -e "#{insert}"`
end

def migrate(migration_path, host, keyspace, base_version)
    create_migration_table(host, keyspace)

    last_migration = get_last_migration(host, keyspace, base_version)
    puts "Last migration: #{last_migration}"

    puts "Getting migrations from: #{migration_path}"

    relevant_migrations = ordered_migration_files(migration_path).select{|migration| migration.order > last_migration }

    relevant_migrations.each do |migration|
        puts "Migrating #{migration.file}"
        execute(host, keyspace, base_version, migration)
        puts "Migrated #{migration.file} successfully"
    end
end

def usage
    puts "USAGE: ruby migrate.rb <migrations path> <host> <keyspace> <base version>"
    exit(1)
end

migration_path = ARGV[0]
unless migration_path and File.directory?(migration_path)
    usage()
end
host = ARGV[1]
unless host
    usage()
end
keyspace = ARGV[2]
unless keyspace
    usage()
end
base_version = ARGV[3]
unless base_version
    usage()
end

migrate(migration_path, host, keyspace, base_version)

