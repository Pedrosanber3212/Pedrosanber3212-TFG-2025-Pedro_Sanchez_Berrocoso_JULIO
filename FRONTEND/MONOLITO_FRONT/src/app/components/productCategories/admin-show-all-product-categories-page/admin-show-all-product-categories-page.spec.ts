import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminShowAllProductCategoriesPage } from './admin-show-all-product-categories-page';

describe('AdminShowAllProductCategoriesPage', () => {
  let component: AdminShowAllProductCategoriesPage;
  let fixture: ComponentFixture<AdminShowAllProductCategoriesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminShowAllProductCategoriesPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminShowAllProductCategoriesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
