import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicationCreate } from './publication-create.component';

describe('PublicationCreate', () => {
  let component: PublicationCreate;
  let fixture: ComponentFixture<PublicationCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicationCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicationCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
