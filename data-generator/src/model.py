import json
import os
import jinja2

class Configuration:
    def __init__(self, num_of_test_classes, num_of_test_cases_for_class, test_fail_probability, test_runs, teststore_server_url, teststore_testsuite_id, revision):
        self.revision = revision
        self.test_runs = test_runs
        self.num_of_test_classes = num_of_test_classes
        self.teststore_server_url = teststore_server_url
        self.test_fail_probability = test_fail_probability
        self.teststore_testsuite_id = teststore_testsuite_id
        self.num_of_test_cases_for_class = num_of_test_cases_for_class

    @staticmethod
    def load_config(config_file):
        with open(config_file) as json_data:
            d = json.load(json_data)
            return Configuration(**d)

class TestRun:
    def __init__(self, test_suite):
        self.test_suite = test_suite

    def render(self, template_path):
        path, filename = os.path.split(template_path)
        return jinja2\
            .Environment(loader=jinja2.FileSystemLoader(path or './'))\
            .get_template(filename).render(self.test_suite.__dict__)

class TestSuite:
    def __init__(self, name="TestSuiteName", tests=10, timestamp="12323"):
        self.time = 0
        self.name = name
        self.failures = 0
        self.tests = tests
        self.testcases = []
        self.timestamp = timestamp

    def add_testcase(self, testcase):
        self.testcases.append(testcase)
        self.time += testcase.time
        if testcase.is_failed:
            self.failures += 1

    def add_failures(self, number_of_failures):
        self.failures = number_of_failures


class TestCase:
    def __init__(self, time, name="TestSample", classname="clazzName"):
        self.name = name
        self.time = time
        self.failure = None
        self.is_failed = False
        self.classname = classname

    def failed(self):
        self.is_failed = True
