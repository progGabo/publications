import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { filter } from 'rxjs/operators';
import { ButtonModule } from 'primeng/button';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ButtonModule
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App implements OnInit {
  items: MenuItem[] = [
    { label: 'Zoznam', icon: 'pi pi-book', routerLink: '/publications' },
    { label: 'Vytvoriť', icon: 'pi pi-plus',   routerLink: '/create' }
  ];

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe((e: NavigationEnd) => {
        this.items.forEach(item => item.expanded = false);
        const akt = this.items.find(i => i.routerLink === e.urlAfterRedirects);
        if (akt) { akt.expanded = true; }
      });
  }
}
