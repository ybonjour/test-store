from urllib import request
import json

API_CREATE_RUN = '{}/testsuites/{}/runs'
API_INSERT_TEST_RESULT = '{}/runs/{}/results'
CONTENT_TYPE_JSON = {'content-type': 'application/json'}
CONTENT_TYPE_XML = {'content-type': 'application/xml'}

class Endpoint:
    def __init__(self, server_url, testsuite_id):
        self.server_url = server_url
        self.testsuite_id = testsuite_id

    def create_run(self, revision, time):
        url = API_CREATE_RUN.format(self.server_url, self.testsuite_id)
        run = {"revision": revision, "time": time.isoformat()}
        params = json.dumps(run).encode('utf8')
        req = request.Request(url, data = params, headers = CONTENT_TYPE_JSON)
        response = request.urlopen(req).read().decode('utf-8')
        return json.loads(response)['id']

    def insert_test_result(self, run_id, junit_xml):
        url = API_INSERT_TEST_RESULT.format(self.server_url, run_id)
        req = request.Request(url, data = junit_xml.encode(encoding='UTF-8'), headers = CONTENT_TYPE_XML)
        request.urlopen(req).read().decode('utf-8')