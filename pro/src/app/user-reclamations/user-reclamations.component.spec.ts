import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserReclamationsComponent } from './user-reclamations.component';

describe('UserReclamationsComponent', () => {
  let component: UserReclamationsComponent;
  let fixture: ComponentFixture<UserReclamationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserReclamationsComponent]
    });
    fixture = TestBed.createComponent(UserReclamationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
