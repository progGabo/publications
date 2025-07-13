import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicationService } from '../../services/publication.service';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { SortByOrderPipe } from '../../services/sort-by-order.pipe';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { RouterModule, Router } from '@angular/router';
import { Toolbar } from 'primeng/toolbar';
import { Select } from "primeng/select";
import { FormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import { InputTextModule } from 'primeng/inputtext';


export interface Page<T> {
  content: T[];
  totalElements: number;
  number: number;
  size: number;
}

interface AuthorInPublication {
  firstName: string;
  lastName: string;
  authorOrder: number;
}

export interface Publication {
  id: number;
  title: string;
  publicationDate: string;          
  isbnIssn: string;
  edition: string;
  pageCount: number;
  abstractText: string;
  publisher: string;
  category: string;
  type: string;
  language: string;
  authors: AuthorInPublication[];   
}

@Component({
  selector: 'app-publications',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    CardModule,
    ProgressSpinnerModule,
    DialogModule,
    ButtonModule,
    SortByOrderPipe,
    ConfirmDialogModule,
    ToastModule,
    RouterModule,
    Toolbar,
    Select,
    FormsModule,
    TooltipModule,
    InputTextModule
],
  templateUrl: './publication-list.component.html',
  styleUrls: ['./publication-list.component.scss']
})
export class PublicationList implements OnInit {
  publications: Publication[] = [];
  loading = true;

  selectedPub: Publication | null = null;
  displayDialog = false;

  filteredPublications: Publication[] = [];
  filtersVisible = true;
  categoryOptions: { label: string, value: string }[] = [];
  languageOptions: { label: string, value: string }[] = [];
  typeOptions: { label: string, value: string }[] = [];
  publisherOptions: {label: string, value: string} [] = [];

  searchTitle: string = '';
  selectedCategory: any = null;
  selectedLanguage: any = null;
  selectedType: any = null;
  selectedPublisher: any = null;

  constructor(
    private publicationService: PublicationService,
    private cdRef: ChangeDetectorRef,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPublications();
  }

  selectIcon(): string{
    return this.filtersVisible ? 'pi-arrow-up' : 'pi-arrow-down';
  }

  confirmDeletePublication(pub: Publication): void {
    this.confirmationService.confirm({
      message: `Naozaj chceš zmazať publikáciu '${pub.title}'?`,
      header: 'Potvrdenie vymazania',
      icon: 'pi pi-exclamation-triangle',
      accept: () => this.deletePublication(pub)
    });
  }

  loadPublications(): void {
    this.loading = true;
    this.publicationService.getAll().subscribe({
      next: (page) => {
        this.publications = page.content;
        this.filteredPublications = [...this.publications];

        this.categoryOptions = this.getDistinctOptions('category');
        this.languageOptions = this.getDistinctOptions('language');
        this.typeOptions = this.getDistinctOptions('type');
        this.publisherOptions = this.getDistinctOptions('publisher');


        this.loading = false;
        this.cdRef.detectChanges();
      },
      error: (err) => {
        console.error('Failed to fetch publications:', err);
        this.loading = false;
        this.cdRef.detectChanges();
      }
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
        this.loadPublications();
        this.closeDialog();
      },
      error: (err) => {
        console.error(`Chyba pri mazaní publikácie ${pub.id}:`, err);
        this.messageService.add({ severity: 'error', summary: 'Chyba', detail: `Nepodarilo sa vymazať publikáciu '${pub.title}'.` });
      }
    });
  }

  getDistinctOptions(field: keyof Publication): { label: string; value: string }[] {
    const uniqueValues = Array.from(
      new Set(
        this.publications
          .map(p => p[field])
          .filter((val): val is string | number => val !== null && val !== undefined && typeof val !== 'object')
      )
    );

    return uniqueValues.map(value => ({
      label: String(value),
      value: String(value)
    }));
  } 

  applyFilters() {
    const term = this.searchTitle.trim().toLowerCase();

    this.filteredPublications = this.publications.filter(pub => {
      const matchesTitle = !term || pub.title.toLowerCase().includes(term);

      const matchesAuthor = !term || pub.authors.some(a => {
        const fullName = `${a.firstName} ${a.lastName}`.toLowerCase();
        return fullName.includes(term);
      });

      const matchesText = matchesTitle || matchesAuthor;

      const matchesCategory = !this.selectedCategory || pub.category === this.selectedCategory.value;
      const matchesLanguage = !this.selectedLanguage || pub.language === this.selectedLanguage.value;
      const matchesType = !this.selectedType || pub.type === this.selectedType.value;
      const matchesPublisher = !this.selectedPublisher || pub.publisher === this.selectedPublisher.value;

      return matchesText && matchesCategory && matchesLanguage && matchesType && matchesPublisher;
    });
  }
  resetFilters() {
    this.searchTitle = '';
    this.selectedCategory = null;
    this.selectedLanguage = null;
    this.selectedType = null;
    this.selectedPublisher = null;
    this.filteredPublications = [...this.publications];
  }
}