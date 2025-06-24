import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminShowAllProductPage } from './admin-show-all-product-page';

describe('AdminShowAllProductPage', () => {
  let component: AdminShowAllProductPage;
  let fixture: ComponentFixture<AdminShowAllProductPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminShowAllProductPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminShowAllProductPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
