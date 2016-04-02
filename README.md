# test-store [![Build Status](https://travis-ci.org/ybonjour/test-store.svg?branch=master)](https://travis-ci.org/ybonjour/test-store)
Stores your test results

## API Usage
Create a testsuite `My Testsuite`:

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
    "name": "aTestSuite"
}
```

Store the test results of a run:

**Request**
```
POST /testsuites/f6da9a37-d7e0-4cfb-b880-a99294c53709/runs
Content-Type: application/json

{
  "revision": "e46ed71567d5da172b00ece3efd7019aab51d756",
  "time": 1459588895366,
  "results": [
    {
      "testName": "ch.yvu.teststore.integration.insert.InsertControllerTest",
      "retryNum": 0,
      "passed": true
    }
  ]
}
```

**Response**
```
200 OK
```
