import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { NavigationService } from '../../services/navigation-service/navigation.service';
import { TestsuiteService } from '../../services/testsuite-service/testsuite.service';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-testsuite-create',
  templateUrl: './testsuite-create.component.html',
  styleUrls: ['./testsuite-create.component.scss']
})
export class TestsuiteCreateComponent implements OnInit {

  testsuiteNameInput = new FormControl('', [Validators.required]);

  constructor(private location: Location,
              private router: Router,
              private navigationService: NavigationService,
              private testsuiteService: TestsuiteService) {
  }

  ngOnInit() {
    this.navigationService.setBackLink('/dashboard');
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  createTestsuite(testsuiteName: string): void {
    if (this.testsuiteNameInput.valid) {
      this.testsuiteService.createTestsuite(testsuiteName)
        .subscribe(() => {
          this.router.navigate(['/dashboard']);
        });
    } else {
      this.testsuiteNameInput.markAsTouched();
    }
  }
}
