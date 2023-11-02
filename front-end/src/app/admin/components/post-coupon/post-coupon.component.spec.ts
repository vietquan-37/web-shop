import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostCouponComponent } from './post-coupon.component';

describe('PostCouponComponent', () => {
  let component: PostCouponComponent;
  let fixture: ComponentFixture<PostCouponComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PostCouponComponent]
    });
    fixture = TestBed.createComponent(PostCouponComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
