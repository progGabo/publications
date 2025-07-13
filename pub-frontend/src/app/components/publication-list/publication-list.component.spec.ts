import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicationList } from './publication-list.component';

describe('PublicationList', () => {
  let component: PublicationList;
  let fixture: ComponentFixture<PublicationList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicationList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicationList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
