import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestsuiteTileComponent } from './testsuite-tile.component';

describe('TestsuiteTileComponent', () => {
  let component: TestsuiteTileComponent;
  let fixture: ComponentFixture<TestsuiteTileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestsuiteTileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestsuiteTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
