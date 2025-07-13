import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule, FormControl, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DatePicker } from 'primeng/datepicker';
import { SelectModule } from 'primeng/select';
import { ActivatedRoute } from '@angular/router';

interface AuthorInPublication {
  firstName: string;
  lastName: string;
  authorOrder: number;
}

export interface PublicationDTO {
  id: number;
  title: string;
  publicationDate: string;
  isbnIssn: string;
  edition: string;
  abstractText: string;
  pageCount: number;
  publisher: string;
  category: string;
  type: string;
  language: string;
  authors: AuthorInPublication[];
}

@Component({
  standalone: true,
  selector: 'publication-create',
  templateUrl: './publication-create.component.html',
  styleUrls:['./publication-create.component.scss'],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule,
    ButtonModule,
    InputTextModule,
    FloatLabelModule,
    SelectModule,
    DatePicker
  ]
})
export class PublicationCreate implements OnInit {
  @Input() publicationToEdit?: PublicationDTO;
  isEditMode: boolean = false;

  publicationForm: FormGroup;
  successMessage = '';
  errorMessage = '';

  publisher: any[] = [];
  language: any[] = [];
  type: any[] = [];
  category: any[] = [];

  showNewInput: Record<string, boolean> = {
    publisher: false,
    language: false,
    type: false,
    category: false
  };

  newControls: Record<string, any> = {
    publisher: new FormControl('', Validators.required),
    language: new FormGroup({
      name: new FormControl('', Validators.required),
      code: new FormControl('', Validators.required)
    }),
    type: new FormControl('', Validators.required),
    category: new FormControl('', Validators.required)
  };

  constructor(private fb: FormBuilder, private http: HttpClient, private route: ActivatedRoute) {
    this.publicationForm = this.fb.group({
      title: ['', Validators.required],
      publicationDate: ['', Validators.required],
      isbnIssn: ['', [Validators.required, this.isbnIssnValidator]],
      edition: ['', Validators.required],
      abstractText: [''],
      pageCount: [null],
      publisherId: [null, Validators.required],
      categoryId: [null, Validators.required],
      typeId: [null, Validators.required],
      languageId: [null, Validators.required],
      authorIds: this.fb.array([])
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!id;

    this.loadDropdownData().then(() => {
      if (this.isEditMode && id) {
        this.http.get<PublicationDTO>(`http://localhost:8080/api/publications/${id}`)
          .subscribe({
            next: (data) =>{ this.patchForm(data), 
              this.publicationToEdit = data;
            },
            error: (err) => console.error('Chyba pri načítaní publikácie:', err)
          });
      }
    });
  }

  get authorIds(): FormArray {
    return this.publicationForm.get('authorIds') as FormArray;
  }

  addAuthorId() {
    this.authorIds.push(
      this.fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required]
      })
    );
  }

  removeAuthorId(index: number) {
    if (this.authorIds.length > 1) {
      this.authorIds.removeAt(index);
    }
  }

  patchForm(pub: PublicationDTO) {
    const selectedPublisher = this.publisher.find(p => p.label === pub.publisher);
    const selectedLanguage = this.language.find(l => l.label === pub.language);
    const selectedType = this.type.find(t => t.label === pub.type);
    const selectedCategory = this.category.find(c => c.label === pub.category);

    this.publicationForm.patchValue({
      title: pub.title,
      publicationDate: new Date(pub.publicationDate),
      isbnIssn: pub.isbnIssn,
      edition: pub.edition,
      abstractText: pub.abstractText,
      pageCount: pub.pageCount,
      publisherId: selectedPublisher ?? null,
      categoryId: selectedCategory ?? null,
      typeId: selectedType ?? null,
      languageId: selectedLanguage ?? null
    });

    const sortedAuthors = [...(pub.authors ?? [])].sort((a, b) => a.authorOrder - b.authorOrder);

    const authorArray = this.fb.array(
      sortedAuthors.length > 0
        ? sortedAuthors.map(a =>
            this.fb.group({
              firstName: [a.firstName, Validators.required],
              lastName: [a.lastName, Validators.required]
            })
          )
        : [this.fb.group({ firstName: ['', Validators.required], lastName: ['', Validators.required] })]
    );

    this.publicationForm.setControl('authorIds', authorArray);
  }



  loadDropdownData(): Promise<void> {
    return Promise.all([
      this.http.get<any>('http://localhost:8080/publisher').toPromise(),
      this.http.get<any>('http://localhost:8080/language').toPromise(),
      this.http.get<any>('http://localhost:8080/type').toPromise(),
      this.http.get<any>('http://localhost:8080/category').toPromise()
    ]).then(([publisherRes, languageRes, typeRes, categoryRes]) => {
      this.publisher = publisherRes._embedded.publishers.map((p: any) => ({
        label: p.name,
        value: this.extractId(p._links.self.href)
      }));
      this.language = languageRes._embedded.languages.map((l: any) => ({
        label: l.name,
        value: this.extractId(l._links.self.href)
      }));
      this.type = typeRes._embedded.publicationTypes.map((t: any) => ({
        label: t.name,
        value: this.extractId(t._links.self.href)
      }));
      this.category = categoryRes._embedded.categories.map((c: any) => ({
        label: c.name,
        value: this.extractId(c._links.self.href)
      }));
    });
  }


  extractId(url: string): number {
    return Number(url.split('/').pop());
  }

  toggleNewInput(entity: string) {
    this.showNewInput[entity] = !this.showNewInput[entity];
  }

  addNewEntity(entity: 'publisher' | 'language' | 'type' | 'category') {
    const control = this.newControls[entity];
    let payload: any;

    if (entity === 'language') {
      if (control.invalid) return;
      payload = {
        name: control.get('name').value.trim(),
        code: control.get('code').value.trim()
      };
    } else {
      const name = control.value?.trim();
      if (!name) return;
      payload = { name };
    }

    this.http.post<any>(`http://localhost:8080/${entity}`, payload).subscribe(res => {
      const newOption = {
        label: res.name,
        value: this.extractId(res._links.self.href)
      };
      this[entity].push(newOption);
      this.publicationForm.get(`${entity}Id`)?.setValue(newOption);
      if (entity === 'language') control.reset();
      else control.setValue('');
      this.toggleNewInput(entity);
    });
  }

  isbnIssnValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value?.toString().trim();
    if (!value) return null;
    const isbn10 = /^(?:\d[\ |-]?){9}[\dX]$/;
    const isbn13 = /^(?:\d[\ |-]?){13}$/;
    const issn = /^\d{4}-\d{3}[\dX]$/;
    return isbn10.test(value) || isbn13.test(value) || issn.test(value)
      ? null : { invalidIsbnIssn: true };
  }

  onSubmit() {
    if (this.publicationForm.invalid) return;
    const raw = this.publicationForm.value;

    const authorRequests = raw.authorIds.map((author: any, index: number) => {
      const firstName = author.firstName.trim();
      const lastName = author.lastName.trim();
      return this.http.get<any>(`http://localhost:8080/api/authors/search?firstName=${firstName}&lastName=${lastName}`)
        .toPromise()
        .then(existing => {
          if (existing && existing.id) {
            return { authorId: existing.id, authorOrder: index + 1 };
          } else {
            return this.http.post<any>('http://localhost:8080/api/authors', { firstName, lastName })
              .toPromise()
              .then(newAuthor => ({ authorId: newAuthor.id, authorOrder: index + 1 }));
          }
        });
    });

    Promise.all(authorRequests).then((authors: { authorId: number, authorOrder: number }[]) => {
      const payload: any = {
        id: this.publicationToEdit?.id ?? null,
        title: raw.title,
        publicationDate: raw.publicationDate,
        isbnIssn: raw.isbnIssn,
        edition: raw.edition,
        abstractText: raw.abstractText || '',
        pageCount: raw.pageCount || 0,
        publisherId: Number(raw.publisherId?.value),
        categoryId: Number(raw.categoryId?.value),
        typeId: Number(raw.typeId?.value),
        languageId: Number(raw.languageId?.value),
        authors
      };

      console.log("Payload: ", payload)
      const req = this.publicationToEdit
        ? this.http.put(`http://localhost:8080/api/publications/${this.publicationToEdit.id}`, payload)
        : this.http.post(`http://localhost:8080/api/publications`, payload);

      req.subscribe({
        next: () => {
          this.successMessage = this.publicationToEdit ? 'Publikácia bola upravená!' : 'Publikácia bola vytvorená!';
          this.errorMessage = '';
        },
        error: (err) => {
          this.successMessage = '';
          this.errorMessage = 'Nepodarilo sa uložiť publikáciu.';
          console.error('Chyba pri odosielaní:', err);
        }
      });
    });
  }
}
