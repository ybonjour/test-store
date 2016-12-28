def migrate(session)
  result = session.execute("SELECT run, testname, retrynum, passed from result")
  result.each do |row|
    if row['passed']
      session.execute("DELETE log FROM result WHERE run=#{row['run']} AND testname='#{row['testname']}' AND retrynum=#{row['retrynum']}")
    end
  end
end