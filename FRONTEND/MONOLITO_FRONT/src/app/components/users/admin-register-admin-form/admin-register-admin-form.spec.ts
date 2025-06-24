import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRegisterAdminForm } from './admin-register-admin-form';

describe('AdminRegisterAdminForm', () => {
  let component: AdminRegisterAdminForm;
  let fixture: ComponentFixture<AdminRegisterAdminForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminRegisterAdminForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRegisterAdminForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
