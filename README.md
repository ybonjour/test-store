# test-store [![Build Status](https://travis-ci.org/ybonjour/test-store.svg?branch=master)](https://travis-ci.org/ybonjour/test-store)
Stores your test results

## API Usage
### Creating a testsuite
**Request**
```
POST /testsuites
name=My Testsuite
```
**Response**
```
201 Created
{
    "id": "f6da9a37-d7e0-4cfb-b880-a99294c53709",
    "name": "My Testsuite"
}
```

### Store entire run with all results
**Request**
```
POST /testsuites/f6da9a37-d7e0-4cfb-b880-a99294c53709/runs
Content-Type: application/json

{
  "revision": "e46ed71567d5da172b00ece3efd7019aab51d756",
  "time": 1459588895366,
  "results": [
    {
      "testName": "ch.yvu.teststore.integration.insert.InsertControllerTest#canInsertRun",
      "retryNum": 0,
      "passed": true,
      "durationMillis": 1230
    }
  ]
}
```
**Response**
```
200 OK
```

### Create an empty run
**Request**
```
POST /testsuites/f6da9a37-d7e0-4cfb-b880-a99294c53709/runs
revision=e46ed71567d5da172b00ece3efd7019aab51d756
time=2016-04-03T01:30:00.001-0200
```
**Response**
```
201 Created
{
    "id": "1b48b134-bb93-4bcd-9c02-92dd83d89157",
    "testSuite": "f6da9a37-d7e0-4cfb-b880-a99294c53709",
    "revision": "e46ed71567d5da172b00ece3efd7019aab51d756",
    "time": 1459654200001
}
```

### Add results to run using JUnit XML format
**Request**
```
POST /runs/1b48b134-bb93-4bcd-9c02-92dd83d89157/results
Content-Type: application/xml

<?xml version="1.0" encoding="UTF-8"?>
<testsuites>
    <testsuite errors="0" skipped="0" tests="1" failures="0" timestamp="2016-03-31T17:51:02">
        <testcase classname="ch.yvu.teststore.integration.insert.InsertControllerTest" name="canInsertRun" time="1.23"/>
    </testsuite>
</testsuites>
```
**Response**
```
200 OK
```

