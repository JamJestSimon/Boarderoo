import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OurLocationComponent } from './our-location.component';

describe('OurLocationComponent', () => {
  let component: OurLocationComponent;
  let fixture: ComponentFixture<OurLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OurLocationComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OurLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
