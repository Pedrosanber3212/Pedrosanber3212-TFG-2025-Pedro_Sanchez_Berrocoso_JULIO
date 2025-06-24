import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminShowAllOrdersPage } from './admin-show-all-orders-page';

describe('AdminShowAllOrdersPage', () => {
  let component: AdminShowAllOrdersPage;
  let fixture: ComponentFixture<AdminShowAllOrdersPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminShowAllOrdersPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminShowAllOrdersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
