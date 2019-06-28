import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestsuiteCreateComponent } from './testsuite-create.component';

describe('TestsuiteCreateComponent', () => {
  let component: TestsuiteCreateComponent;
  let fixture: ComponentFixture<TestsuiteCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestsuiteCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestsuiteCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
