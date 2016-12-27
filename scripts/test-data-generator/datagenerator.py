# pip3 install jinja2


from model import *
from randomgenerator import *
from teststoreendpoint import *
from datetime import datetime

TEMPLATE_FILE_NAME = 'test-data-generator-template.xml'
CONFIG_FILE_NAME = 'test-data-generator-config.json'


def generate_random_test_run(class_name, test_names, fail_probability, timestamp):
    testsuite = TestSuite(name=class_name, tests=len(test_names), timestamp=timestamp)

    for test_name in test_names:
        testcase = TestCase(time=generate_random_time(),name=test_name, classname=class_name)
        if generate_random_time() < fail_probability:
            testcase.failed()
        testsuite.add_testcase(testcase)

    return TestRun(testsuite).render(TEMPLATE_FILE_NAME)


def main():
    configuration = Configuration.load_config(CONFIG_FILE_NAME)

    endpoint = Endpoint(configuration.teststore_server_url, configuration.teststore_testsuite_id)

    random_data = {}
    package_name = generate_random_package_name()
    class_names = generate_random_class_names(configuration.num_of_test_classes)

    for class_name in class_names:
        random_data[package_name + class_name] = generate_random_test_names(configuration.num_of_test_cases_for_class)

    for test_run in range(0, configuration.test_runs):
        run_id = endpoint.create_run(configuration.revision, datetime.now())

        print('Generating Test Run #:' + str(test_run) + ", id: " + run_id)

        timestamp = generate_random_timestamp()
        for class_name in random_data:
            junit_xml = generate_random_test_run(
                class_name,
                random_data[class_name],
                configuration.test_fail_probability,
                timestamp)
            endpoint.insert_test_result(run_id, junit_xml)


if __name__ == "__main__":
    main()