import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page';
import { Publication } from '../models/publication';
import { HttpParams } from '@angular/common/http';
import { PublicationFilter } from '../models/publication-filter';

@Injectable({
  providedIn: 'root'
})

export class PublicationService {
  private apiUrl = 'http://localhost:8080/api/publications';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Page<Publication>> {
    return this.http.get<Page<Publication>>(this.apiUrl);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getById(id: number): Observable<Publication> {
    return this.http.get<Publication>(`${this.apiUrl}/${id}`);
  }

  save(payload: Publication): Observable<Publication> {
    return payload.id == null ? this.http.post<Publication>(this.apiUrl, payload)
              : this.http.put<Publication>(`${this.apiUrl}/${payload.id}`, payload);
  }

  search(filter: PublicationFilter): Observable<Page<Publication>> {
    let params = new HttpParams();
    Object.entries(filter).forEach(([key, val]) => {
      if (val != null) {
        if (Array.isArray(val)) {
          val.forEach(v => params = params.append(`${key}`, v));
        } else {
          params = params.set(key, val);
        }
      }
    });
    return this.http.get<Page<Publication>>(`${this.apiUrl}/filter`, { params });
  }

}
