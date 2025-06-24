import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllMyOrdersPage } from './show-all-my-orders-page';

describe('ShowAllMyOrdersPage', () => {
  let component: ShowAllMyOrdersPage;
  let fixture: ComponentFixture<ShowAllMyOrdersPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowAllMyOrdersPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowAllMyOrdersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
