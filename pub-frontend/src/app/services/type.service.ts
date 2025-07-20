import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from "../models/page";
import { Option } from "../models/option";

@Injectable({
  providedIn: 'root'
})

export class TypeService{
    private apiUrl = 'http://localhost:8080/api/type';

    constructor(private http: HttpClient) {}

    save(payload: Option): Observable<Option> {
        return this.http.post<any>(this.apiUrl, payload)
    }

    getById(id: number): Observable<Option> {
        return this.http.get<any>(`${this.apiUrl}/${id}`);
    }

    getAll(): Observable<Page<Option>> {
        return this.http.get<Page<Option>>(this.apiUrl);
      }
}