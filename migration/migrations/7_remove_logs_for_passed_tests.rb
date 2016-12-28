def migrate(session)
  result = session.execute("SELECT run, testname, retrynum from result")
  result.each do |row|
    session.execute("DELETE log FROM result WHERE run=#{row['run']} AND testname='#{row['testname']}' AND retrynum=#{row['retrynum']}")
  end
end