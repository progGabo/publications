import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sortByOrder',
  standalone: true
})
export class SortByOrderPipe implements PipeTransform {
  transform(authors: any[]): any[] {
    return authors?.slice().sort((a, b) => a.authorOrder - b.authorOrder) ?? [];
  }
}