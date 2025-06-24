import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProductsModifyForm } from './admin-products-modify-form';

describe('AdminProductsModifyForm', () => {
  let component: AdminProductsModifyForm;
  let fixture: ComponentFixture<AdminProductsModifyForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminProductsModifyForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProductsModifyForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
