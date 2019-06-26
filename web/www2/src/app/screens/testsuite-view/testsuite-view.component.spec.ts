import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestsuiteViewComponent } from './testsuite-view.component';

describe('TestsuiteViewComponent', () => {
  let component: TestsuiteViewComponent;
  let fixture: ComponentFixture<TestsuiteViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestsuiteViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestsuiteViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
