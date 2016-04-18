import {Injectable} from 'angular2/core';

@Injectable()
export class TestResultService {
	getResults() {
		return Promise.resolve([{
			"run": "e7add2bc-a2f4-41ab-97ae-a2f210b3a447",
			"testName": "ch.yvu.teststore.common.CassandraRepositoryTest#canSaveAnItem",
			"retryNum": 0,
			"passed": true,
			"durationMillis": 2349
		},
		{
			"run": "e7add2bc-a2f4-41ab-97ae-a2f210b3a447",
			"testName": "ch.yvu.teststore.common.CassandraRepositoryTest#returnsSavedItem",
			"retryNum": 0,
			"passed": false,
			"durationMillis": 3451
		}]);
	}
}