import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionUsersComponent } from './gestion-users.component';

describe('GestionUsersComponent', () => {
  let component: GestionUsersComponent;
  let fixture: ComponentFixture<GestionUsersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GestionUsersComponent]
    });
    fixture = TestBed.createComponent(GestionUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
