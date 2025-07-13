import { Routes } from '@angular/router';
import { PublicationCreate } from './components/publication-create/publication-create.component';
import { PublicationList } from './components/publication-list/publication-list.component';

export const routes: Routes = [
    {path: 'create', component: PublicationCreate},
    {path: 'publications', component: PublicationList},
    {path: '', redirectTo: 'publications', pathMatch: 'full'},
    {path: 'publications/:id/edit', component: PublicationCreate} 
];
