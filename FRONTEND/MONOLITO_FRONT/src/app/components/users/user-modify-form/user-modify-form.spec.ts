import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserModifyForm } from './user-modify-form';

describe('UserModifyForm', () => {
  let component: UserModifyForm;
  let fixture: ComponentFixture<UserModifyForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserModifyForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserModifyForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
