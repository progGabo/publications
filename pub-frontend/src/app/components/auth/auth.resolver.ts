import { Injectable } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { AuthorInPublication } from "../../models/author-in-publication";
import { Observable } from "rxjs";
import { Resolve } from "@angular/router";

@Injectable({ providedIn: 'root' })
export class AuthResolver implements Resolve<AuthorInPublication | null> {
  constructor(private auth: AuthService) {}
  resolve(): Observable<AuthorInPublication | null> {
    return this.auth.loadCurrentUser();
  }
}