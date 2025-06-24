import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerShowAllProductsPage } from './customer-show-all-products-page';

describe('CustomerShowAllProductsPage', () => {
  let component: CustomerShowAllProductsPage;
  let fixture: ComponentFixture<CustomerShowAllProductsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerShowAllProductsPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerShowAllProductsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
