import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.html',
  styleUrls: ['./app.scss', '../styles.scss']
})

export class App{

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}

