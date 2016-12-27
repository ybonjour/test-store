
from datetime import datetime
import string
import random

def generate_random_timestamp():
    year = random.choice(range(1950, 2001))
    month = random.choice(range(1, 13))
    day = random.choice(range(1, 29))
    hours = random.choice(range(1, 24))
    minutes = random.choice(range(0, 60))
    seconds = random.choice(range(0, 60))
    return '{0:%Y-%m-%dT%H:%M:%S}'.format(datetime(year, month, day, hours, minutes, seconds))

def generate_random_time():
    return random.uniform(0, 1)

def generate_name(size=6):
    return ''.join(random.choice(string.ascii_lowercase) for _ in range(size))

def generate_random_package_name():
    return '{}.{}.{}.{}.'.format(generate_name(2), generate_name(3), generate_name(6), generate_name(5))

def generate_random_class_names(num__of_test_classes):
    return [(lambda x: generate_name(10).title() + 'Test')(x) for x in range(num__of_test_classes)]

def generate_random_test_names(num_of_test_cases_per_class):
    return [(lambda x: generate_name(4) +  generate_name(15).title() + str(x))(x) for x in range(num_of_test_cases_per_class)]