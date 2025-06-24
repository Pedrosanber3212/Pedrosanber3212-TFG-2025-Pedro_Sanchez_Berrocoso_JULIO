import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProductCategoriesModifyForm } from './admin-product-categories-modify-form';

describe('AdminProductCategoriesModifyForm', () => {
  let component: AdminProductCategoriesModifyForm;
  let fixture: ComponentFixture<AdminProductCategoriesModifyForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminProductCategoriesModifyForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProductCategoriesModifyForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
