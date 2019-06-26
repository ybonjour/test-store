import { TestBed } from '@angular/core/testing';

import { RunService } from './run.service';

describe('RunService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RunService = TestBed.get(RunService);
    expect(service).toBeTruthy();
  });
});
