import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, FormControl, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Publication } from '../../models/publication';
import { PublicationService } from '../../services/publication.service';
import { EntityType } from '../../models/entity-type';
import { Option } from '../../models/option';
import { AuthorInPublication } from '../../models/author-in-publication';
import { DropdownDataService } from '../../services/dropdown-data.service';
import { AuthService } from '../../services/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  standalone: false,
  selector: 'publication-create',
  templateUrl: './publication-create.component.html',
  styleUrls:['./publication-create.component.scss', '../../../styles.scss'],
})

export class PublicationCreate implements OnInit {
  @Input() publicationToEdit?: Publication;
  isEditMode: boolean = false;
  public readonly EntityType = EntityType;

  publicationForm: FormGroup;

  publisher: Option[] = [];
  language: Option[] = [];
  type: Option[] = [];
  category: Option[] = [];

  showNewInput: Record<string, boolean> = {
    publisher: false,
    language: false,
    type: false,
    category: false
  };

  showResultDialog = false;
  dialogHeader = '';
  isEditDialog = false;
  showErrorDialog = false;
  errorDialogHeader = '';
  errorDialogMessage = '';
  
  currentUserFirstName: string ;
  currentUserLastName: string;

  newControls: Record<string, any> = {
    publisher: new FormControl('', Validators.required),
    language: new FormControl('', Validators.required),
    type: new FormControl('', Validators.required),
    category: new FormControl('', Validators.required)
  };

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private messageService: MessageService,
    private cdRef: ChangeDetectorRef,
    private route: ActivatedRoute, 
    private publicationService: PublicationService,
    private dropdownService: DropdownDataService,
    public auth: AuthService
  ) {
    this.currentUserFirstName = this.auth.currentUserFirstName;
    this.currentUserLastName = this.auth.currentUserLastName;

    this.publicationForm = this.fb.group({
      title: ['', Validators.required],
      publicationDate: ['', Validators.required], 
      isbnIssn: ['', [Validators.required, this.ValidatorIsbnIssn]], 
      edition: ['', Validators.required],
      abstractText: ['', Validators.required],
      pageCount: [null, Validators.required],
      publisherId: [null, Validators.required],
      categoryId: [null, Validators.required],
      typeId: [null, Validators.required],
      languageId: [null, Validators.required],
      authorIds: this.fb.array([
        this.fb.group({
          firstName: [ this.currentUserFirstName, Validators.required ],
          lastName:  [ this.currentUserLastName,  Validators.required ],
        })
      ])
    });
  }

  ngOnInit(): void {
    this.auth.loadCurrentUser().subscribe(user => {
      this.currentUserFirstName = this.auth.currentUserFirstName;
      this.currentUserLastName  = this.auth.currentUserLastName;
    });
    const id = this.route.snapshot.paramMap.get('id');
    if (id !== null) {
      this.isEditMode = true;
    } else {
      this.isEditMode = false;
    }

    this.loadDropdownData()

    if (this.isEditMode && id) {
    this.publicationService.getById(Number(id))
      .subscribe({
        next: (data: Publication) => {
          this.publicationToEdit = data;
          this.patchForm(data);
        }
      });
    }
  }

  get authorIds(): FormArray {
    return this.publicationForm.get('authorIds') as FormArray;
  }

  addAuthor(firstName?: string, lastname?: string) {
    this.authorIds.push(
      this.fb.group({
        firstName: [firstName, Validators.required],
        lastName: [lastname, Validators.required]
      })
    );
  }

  removeAuthor(index: number) {
    if (this.authorIds.length > 1) {
      this.authorIds.removeAt(index);
    }
  }

  patchForm(pub: Publication) {
    const selectedPublisher = this.publisher.find(p => p.name === pub.publisher);
    const selectedLanguage = this.language.find(l => l.name === pub.language);
    const selectedType = this.type.find(t => t.name === pub.type);
    const selectedCategory = this.category.find(c => c.name === pub.category);

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

    let authorArray: FormArray;

    if (pub.authors.length > 0) {
      const groups = pub.authors.map(a =>
        this.fb.group({
          firstName: [a.firstName, Validators.required],
          lastName:  [a.lastName,  Validators.required]
        })
      );
      authorArray = this.fb.array(groups);
    } else {
      authorArray = this.fb.array([
        this.fb.group({
          firstName: ['', Validators.required],
          lastName:  ['', Validators.required]
        })
      ]);
    }

    this.publicationForm.setControl('authorIds', authorArray);
  }

  loadDropdownData() {
    this.dropdownService.loadAll().subscribe({
      next: opts => {
        this.publisher = opts.publishers;
        this.category  = opts.categories;
        this.type      = opts.types;
        this.language  = opts.languages;
      }
    });
  }

  extractId(url: string): number {
    return Number(url.split('/').pop());
  }

  toggleNewInput(entity: string) {
    this.showNewInput[entity] = !this.showNewInput[entity];
  }

  addNewEntity(entity: EntityType) {
    const control = this.newControls[entity];
    let payload: Option;

    const name = control.value?.trim();
    if (!name) return;
    payload = { name };

    let create = this.dropdownService.saveEntity(entity, payload);
    if (!create) return;

    create.subscribe({
      next: newOption => {
        this.loadDropdownData();
        control.reset();
        this.toggleNewInput(entity);
        this.cdRef.detectChanges();
      },
      error: (err) => console.error(`Chyba pri vytváraní ${entity}:`, err)
    });
  }

  ValidatorIsbnIssn(control: AbstractControl): ValidationErrors | null {
    const value = control.value?.toString().trim();
    if (!value) return null;
    const isbn = /^(?:ISBN(?:-13)?:?\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-\ ]){4})[-\ 0-9]{17}$)97[89][-\ ]?[0-9]{1,5}[-\ ]?[0-9]+[-\ ]?[0-9]+[-\ ]?[0-9]$/;
    const issn = /^\d{4}-\d{3}[\dX]$/;
    return isbn.test(value) || issn.test(value)
      ? null : { invalidIsbnIssn: true };
  }

  onSubmit(): void {;
    if (this.publicationForm.invalid) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Chýbajú údaje',
        detail: 'Vyplň prosím všetky povinné polia.',
        life: 3500
      });
      return;
    }

    const raw = this.publicationForm.value as {
      title: string;
      publicationDate: Date;
      isbnIssn: string;
      edition: string;
      abstractText: string;
      pageCount: number;
      publisherId: { id: number };
      categoryId:  { id: number };
      typeId:      { id: number };
      languageId:  { id: number };
      authorIds: Array<{ id?: number; firstName: string; lastName: string }>;
    };

    const authors: AuthorInPublication[] = raw.authorIds.map((a, idx) => ({
      id: a.id,
      firstName: a.firstName.trim(),
      lastName:  a.lastName.trim(),
      authorOrder: idx + 1
    }));

    const payload: any = {
      id:               this.publicationToEdit?.id ?? null,
      title:            raw.title,
      publicationDate:  raw.publicationDate,
      isbnIssn:         raw.isbnIssn,
      edition:          raw.edition,
      abstractText:     raw.abstractText || '',
      pageCount:        raw.pageCount || 0,
      publisherId:      raw.publisherId.id,
      categoryId:       raw.categoryId.id,
      typeId:           raw.typeId.id,
      languageId:       raw.languageId.id,
      authors
    };

    this.publicationService.save(payload).subscribe({
    next: () => {
      this.isEditDialog = this.isEditMode;
      this.dialogHeader = this.isEditMode
        ? 'Publikácia upravená'
        : 'Publikácia vytvorená';
      this.showResultDialog = true;
      //this.cdRef.detectChanges();
    },
    error: (err: HttpErrorResponse) => {
      console.log(err);
      let header = this.isEditMode
        ? 'Chyba pri úprave'
        : 'Chyba pri vytváraní';
      let message = this.isEditMode
        ? 'Nepodarilo sa upraviť publikáciu. Skúste to prosím neskôr.'
        : 'Nepodarilo sa vytvoriť publikáciu. Skúste to prosím neskôr.';

      if (err.status === 409 && err.error.message === 'Duplicate ISBN') {
        header = 'Duplicitné ISBN';
        message = 'Zadané ISBN/ISSN už existuje.';
      }

      this.errorDialogHeader = header;
      this.errorDialogMessage = message;
      this.showErrorDialog = true;
      this.cdRef.detectChanges();
    }
  });
  }

  goHome() {
    this.showResultDialog = false;
    this.router.navigate(['/']); 
  }

  addAnother() {
    this.showResultDialog = false;
    this.publicationForm.reset();

    while (this.authorIds.length) {
      this.authorIds.removeAt(0);
    }
    this.addAuthor(this.currentUserFirstName, this.currentUserLastName);
    this.isEditMode = false;
  }
}
