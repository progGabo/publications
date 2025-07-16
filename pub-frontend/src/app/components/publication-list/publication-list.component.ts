import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PublicationService } from '../../services/publication.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { Publication } from '../../models/publication';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Option } from '../../models/option';
import { PublicationFilter } from '../../models/publication-filter';
import { Page } from '../../models/page';
import { DropdownDataService } from '../../services/dropdown-data.service';

@Component({
  selector: 'app-publications',
  standalone: false,
  templateUrl: './publication-list.component.html',
  styleUrls: ['./publication-list.component.scss']
})

export class PublicationList implements OnInit {
  publications: Publication[] = [];
  loading = true;

  selectedPub: Publication | null = null;
  displayDialog = false;

  filtersVisible = true;
  categoryOptions: Option[] = [];
  languageOptions: Option[] = [];
  typeOptions: Option[] = [];
  publisherOptions: Option [] = [];

  searchTitle: string = '';
  selectedCategory: Option | null = null;
  selectedLanguage: Option | null = null;
  selectedType: Option | null = null;
  selectedPublisher: Option | null = null;
  
  filterSubject = new Subject<PublicationFilter>();
  sub!: Subscription;

  constructor(
    private publicationService: PublicationService,
    private cdRef: ChangeDetectorRef,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private router: Router,
    private dropdownService: DropdownDataService
  ) {}

  ngOnInit(): void {
    this.sub = this.filterSubject.pipe(
      debounceTime(300),
      distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
      switchMap(filter => {
        this.loading = true;
        return this.publicationService.search(filter);
      })
    ).subscribe({
      next: (page: Page<Publication>) => {
        this.publications = page.content;
        this.loading = false;
        this.cdRef.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.cdRef.markForCheck();
      }
    });

    this.applyFilters();

    this.loadDropdownData();
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  loadDropdownData(){
    this.dropdownService.loadAll().subscribe({
      next: opts => {
        this.publisherOptions = opts.publishers;
        this.categoryOptions  = opts.categories;
        this.typeOptions      = opts.types;
        this.languageOptions  = opts.languages;
        this.loading = false;
      }
    });
  }

  confirmDeletePublication(pub: Publication): void {
    this.confirmationService.confirm({
      message: `Naozaj chceš zmazať publikáciu '${pub.title}'?`,
      header: 'Potvrdenie vymazania',
      icon: 'pi pi-exclamation-triangle',
      accept: () => this.deletePublication(pub)
    });
  }

  getAuthorNames(pub: Publication): string {
    return pub.authors
      ?.map(a => `${a.firstName} ${a.lastName}`)
      .join(', ') ?? '';
  }

  showDetail(pub: Publication): void {
    this.selectedPub = pub; 
    this.displayDialog = true;
  }

  editPublication(pub: Publication): void {
    this.router.navigate(['/publications', pub.id, 'edit']);
  }

  closeDialog(): void {
    this.displayDialog = false;
    this.selectedPub = null;
  }

  deletePublication(pub: Publication): void {
    this.publicationService.delete(pub.id).subscribe({
      next: () => {
        this.publications = this.publications.filter(p => p.id !== pub.id);
        this.messageService.add({ severity: 'success', summary: 'Vymazané', detail: `Publikácia '${pub.title}' bola vymazaná.` });
        this.applyFilters();
        this.closeDialog();
      },
      error: (err) => {
        console.error(`Chyba pri mazaní publikácie ${pub.id}:`, err);
        this.messageService.add({ severity: 'error', summary: 'Chyba', detail: `Nepodarilo sa vymazať publikáciu '${pub.title}'.` });
      }
    });
  }

  applyFilters(): void {
    const filter: PublicationFilter = {
      searchTerm: this.searchTitle.trim() || undefined,
      publisher: this.selectedPublisher?.name,
      category: this.selectedCategory?.name,
      type: this.selectedType?.name,
      language: this.selectedLanguage?.name,
    };

    this.filterSubject.next(filter);
  }

  resetFilters() {
    this.searchTitle = '';
    this.selectedCategory = null;
    this.selectedLanguage = null;
    this.selectedType = null;
    this.selectedPublisher = null;
    this.applyFilters();
  }
}