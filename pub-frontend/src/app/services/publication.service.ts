import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, Publication } from '../components/publication-list/publication-list.component';


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

  update(id: number, payload: any): Observable<Publication> {
    return this.http.put<Publication>(`${this.apiUrl}/${id}`, payload);
  }
}
