import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProductCategoriesCreateForm } from './admin-product-categories-create-form';

describe('AdminProductCategoriesCreateForm', () => {
  let component: AdminProductCategoriesCreateForm;
  let fixture: ComponentFixture<AdminProductCategoriesCreateForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminProductCategoriesCreateForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProductCategoriesCreateForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
