import { NgModule } from "@angular/core";
import { PublicationCreate } from "./components/publication-create/publication-create.component";
import { PublicationList } from "./components/publication-list/publication-list.component";
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DatePicker } from 'primeng/datepicker';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClient, HttpClientModule, HttpHandler, provideHttpClient } from "@angular/common/http";
import { App } from "./app";
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import { ConfirmationService } from 'primeng/api';
import { MessageService } from 'primeng/api';
import { provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { RouterOutlet } from "@angular/router";
import { Toolbar } from 'primeng/toolbar';
import { Select } from "primeng/select";
import { FormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import { RouterModule } from '@angular/router';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { DialogModule } from 'primeng/dialog';
import { SortByOrderPipe } from "./services/sort-by-order.pipe";
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { BrowserModule } from "@angular/platform-browser";
import { AuthComponent } from "./components/auth/auth.component";
import { MessageModule } from 'primeng/message';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptors/jwt.interceptor';

@NgModule({
    declarations: [
        App,
        PublicationCreate,
        PublicationList,
        SortByOrderPipe,
        AuthComponent
    ],
    imports: [
        TextareaModule,
        MessageModule,
        BrowserModule,
        ReactiveFormsModule,
        CommonModule,
        ButtonModule,
        InputTextModule,
        HttpClientModule,
        FloatLabelModule,
        SelectModule,
        DatePicker,
        CommonModule,
        TableModule,
        CardModule,
        ProgressSpinnerModule,
        DialogModule,
        ConfirmDialogModule,
        ToastModule,
        RouterModule,
        Toolbar,
        RouterOutlet,
        Select,
        FormsModule,
        TooltipModule,
        RouterModule.forRoot(routes)
    ],
     providers: [
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        ConfirmationService,
        MessageService,
        HttpClient,
        HttpClientModule,
        provideAnimationsAsync(),
        BrowserAnimationsModule,
        provideBrowserGlobalErrorListeners(),
        provideZonelessChangeDetection(),
        provideRouter(routes),
        providePrimeNG({
        theme: {
            preset: Aura
        }
        })
    ],
    bootstrap: [App]
})

export class AppModule { }