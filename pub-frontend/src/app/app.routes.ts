import { Routes } from '@angular/router';
import { PublicationCreate } from './components/publication-create/publication-create.component';
import { PublicationList } from './components/publication-list/publication-list.component';
import { AuthResolver } from './components/auth/auth.resolver';

export const routes: Routes = [
    {path: 'create', component: PublicationCreate, resolve: { user: AuthResolver }},
    {path: 'publications', component: PublicationList, resolve: { user: AuthResolver }},
    {path: '', redirectTo: 'publications', pathMatch: 'full', resolve: { user: AuthResolver }},
    {path: 'publications/:id/edit', component: PublicationCreate, resolve: { user: AuthResolver }} 
];
