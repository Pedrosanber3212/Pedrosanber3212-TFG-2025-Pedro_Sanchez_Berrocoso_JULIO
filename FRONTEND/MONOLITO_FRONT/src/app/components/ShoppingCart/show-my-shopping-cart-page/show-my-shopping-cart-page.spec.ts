import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowMyShoppingCartPage } from './show-my-shopping-cart-page';

describe('ShowMyShoppingCartPage', () => {
  let component: ShowMyShoppingCartPage;
  let fixture: ComponentFixture<ShowMyShoppingCartPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowMyShoppingCartPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowMyShoppingCartPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
