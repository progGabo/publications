import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { AuthorInPublication } from '../models/author-in-publication';
import { tap, switchMap, catchError } from 'rxjs/operators';
import { RegisterRequest } from '../models/register-request';
import { LoginRequest } from '../models/login-request';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true,
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API = 'http://localhost:8080/api/authors';

  private loggedIn$ = new BehaviorSubject<boolean>(false);
  private currentUser$ = new BehaviorSubject<AuthorInPublication | null>(null);

  constructor(private http: HttpClient) {
    this.loadCurrentUser().subscribe();
  }

  login(req: LoginRequest): Observable<AuthorInPublication | null> {
    return this.http
      .post<void>(`${this.API}/login`, req, httpOptions)
      .pipe(
        switchMap(() => this.loadCurrentUser()),
        tap(user => {
          this.loggedIn$.next(!!user);
        })
      );
  }

  register(req: RegisterRequest): Observable<any> {
    return this.http.post(`${this.API}/register`, req, httpOptions);
  }

  logout(): Observable<any> {
  return this.http.post(`${this.API}/logout`, {}, httpOptions).pipe(
    tap(() => {
      this.loggedIn$.next(false);
      this.currentUser$.next(null);
    })
  );
}
  isLoggedIn(): boolean {
    return this.loggedIn$.value;
  }

  watchLoggedIn(): Observable<boolean> {
    return this.loggedIn$.asObservable();
  }

  watchCurrentUserId(): Observable<number | null> {
    return this.currentUser$.asObservable().pipe(
      switchMap(user => of(user?.id ?? null))
    );
  }

  get currentUserId(): number | null {
    return this.currentUser$.value?.id ?? null;
  }

  get currentUserFirstName(): string {
    return this.currentUser$.value?.firstName || '';
  }

  get currentUserLastName(): string {
    return this.currentUser$.value?.lastName || '';
  }

  get userFullName(): string {
    const u = this.currentUser$.value;
    return u ? `${u.firstName} ${u.lastName}` : '';
  }

  private extractUserIdFromToken(): number | null {
    return this.currentUser$.value?.id ?? null;
  }

  loadCurrentUser(): Observable<AuthorInPublication | null> {
    return this.http
      .get<AuthorInPublication>(`${this.API}/me`, httpOptions)
      .pipe(
        tap(user => {
          this.currentUser$.next(user);
          this.loggedIn$.next(!!user);
        }),
        catchError(() => {
          this.currentUser$.next(null);
          this.loggedIn$.next(false);
          return of(null);
        })
      );
  }
}