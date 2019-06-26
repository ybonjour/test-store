import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryGridComponent } from './history-grid.component';

describe('HistoryGridComponent', () => {
  let component: HistoryGridComponent;
  let fixture: ComponentFixture<HistoryGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoryGridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoryGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
