import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReclamationComponent } from './add-reclamation.component';

describe('AddReclamationComponent', () => {
  let component: AddReclamationComponent;
  let fixture: ComponentFixture<AddReclamationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddReclamationComponent]
    });
    fixture = TestBed.createComponent(AddReclamationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
