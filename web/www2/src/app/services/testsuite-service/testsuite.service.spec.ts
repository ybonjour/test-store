import { TestBed } from '@angular/core/testing';

import { TestsuiteService } from './testsuite.service';

describe('TestsuiteService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TestsuiteService = TestBed.get(TestsuiteService);
    expect(service).toBeTruthy();
  });
});
