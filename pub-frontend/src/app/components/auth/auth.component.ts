import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { LoginRequest } from '../../models/login-request';
import { RegisterRequest } from '../../models/register-request';

@Component({
  standalone: false,
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss', '../../../styles.scss']
})

export class AuthComponent implements OnInit {
  showLoginDialog = false;
  showRegisterDialog = false;
  errorMsg = '';

  loginForm: FormGroup;
  registerForm: FormGroup;

  currentUserFullName: string;

  constructor(
    private fb: FormBuilder,
    private cdRef: ChangeDetectorRef,
    private messageService: MessageService,
    public auth: AuthService
  ) {
    this.loginForm = this.fb.group({
      nickname: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.registerForm = this.fb.group({
      nickname: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
    this.currentUserFullName = this.auth.userFullName;
  }

  ngOnInit(): void {
    this.auth.watchLoggedIn().subscribe(loggedIn => {
      if (loggedIn) {
        this.currentUserFullName = this.auth.userFullName;
      } else {
        this.currentUserFullName = '';
      }
    });
  }

  openLogin() {
    this.errorMsg = '';
    this.showLoginDialog = true;
  }

  openRegister() {
    this.errorMsg = '';
    this.showRegisterDialog = true;
  }

  onLogin() {
  if (this.loginForm.invalid) return;
  const req: LoginRequest = this.loginForm.value;
  this.auth.login(req).subscribe({
    next: () => {
      this.showLoginDialog = false;
      this.loginForm.reset(); 
      this.errorMsg = '';
      this.currentUserFullName = this.auth.userFullName;       
      this.messageService.add({
          severity: 'success',
          summary: 'Prihlásenie',
          detail: 'Úspešne prihlásený',
          life: 1000
        });
    },
    error: (err: HttpErrorResponse) => {this.errorMsg = err.error.error || 'Prihlásenie zlyhalo';
      this.cdRef.detectChanges();
    }
  });
}

onRegister() {
  if (this.registerForm.invalid) return;
  const req: RegisterRequest = this.registerForm.value;
  this.auth.register(req).subscribe({
    next: () => {
      this.showRegisterDialog = false;
      this.registerForm.reset();
      this.errorMsg = '';
      this.openLogin();
      this.messageService.add({
          severity: 'success',
          summary: 'Registrácia',
          detail: 'Úspešne zaregistrovaný',
          life: 1000
        });
    },
    error: (err: HttpErrorResponse) => {this.errorMsg = err.error.error  || 'Registrácia zlyhala';
      this.cdRef.detectChanges();
    }
  });
}

  logout() {
  this.auth.logout().subscribe(() => {
    this.messageService.add({
      severity: 'info',
      summary: 'Odhlásenie',
      detail: 'Boli ste odhlásený',
      life: 1000
    });
  });
}
}
