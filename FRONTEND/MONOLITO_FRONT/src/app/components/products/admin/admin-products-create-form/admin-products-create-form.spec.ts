import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProductsCreateForm } from './admin-products-create-form';

describe('AdminProductsCreateForm', () => {
  let component: AdminProductsCreateForm;
  let fixture: ComponentFixture<AdminProductsCreateForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminProductsCreateForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProductsCreateForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
