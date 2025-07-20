import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Page } from '../models/page';
import { Option } from '../models/option';
import { PublisherService } from './publisher.service';
import { CategoryService } from './category.service';
import { TypeService } from './type.service';
import { LanguageService } from './language.service';
import { EntityType } from '../models/entity-type';

interface DropdownOptions {
  publishers: Option[];
  categories: Option[];
  types: Option[];
  languages: Option[];
}

@Injectable({ providedIn: 'root' })
export class DropdownDataService {
  constructor(
    private publisherService: PublisherService,
    private categoryService: CategoryService,
    private typeService: TypeService,
    private languageService: LanguageService,
  ) {}

  private fetchOptions(obs: Observable<Page<Option>>): Observable<Option[]> {
    return obs.pipe(
      map(page => page.content)
    );
  }

  loadAll(): Observable<DropdownOptions> {
    return forkJoin({
      publishers: this.fetchOptions(this.publisherService.getAll()),
      categories: this.fetchOptions(this.categoryService.getAll()),
      types:      this.fetchOptions(this.typeService.getAll()),
      languages:  this.fetchOptions(this.languageService.getAll()),
    });
  }

  saveEntity(entity: EntityType, payload: Option){
    let create$: Observable<Option>;

    switch (entity) {
      case EntityType.Publisher:
        create$ = this.publisherService.save(payload);
        break;
      case EntityType.Category:
        create$ = this.categoryService.save(payload);
        break;
      case EntityType.Type:
        create$ = this.typeService.save(payload);
        break;
      case EntityType.Language:
        create$ = this.languageService.save(payload);
        break;
      default:
        return;
    }

    return create$;
  }
}