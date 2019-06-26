import { Component, Input, OnInit } from '@angular/core';
import { RunService } from '../../services/run-service/run.service';
import { Run } from '../../domain/Run';
import { Testsuite } from '../../domain/Testsuite';

@Component({
  selector: 'app-testsuite-tile',
  templateUrl: './testsuite-tile.component.html',
  styleUrls: ['./testsuite-tile.component.scss']
})
export class TestsuiteTileComponent implements OnInit {

  lastRun: Run;

  constructor(private runService: RunService) {
  }

  @Input() testsuite: Testsuite;

  ngOnInit() {
    this.runService.getLastRun(this.testsuite.id)
      .subscribe(response => {
          this.lastRun = response;
        },
        _ => {});
  }

  generateRouterLink(): string {
    return '/testsuite/' + this.testsuite.id;
  }
}
