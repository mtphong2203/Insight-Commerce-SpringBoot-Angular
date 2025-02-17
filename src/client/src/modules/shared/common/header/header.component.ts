import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule, IconDefinition } from '@fortawesome/angular-fontawesome';
import { faBars, faClose } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule, FontAwesomeModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  public logoEShop: string = './assets/images/E-Shop.jpg';
  public hamburger: IconDefinition = faBars;
  public close: IconDefinition = faClose;
  public isShow: boolean = false;

  public navItem: any[] = [
    { title: 'Home', link: '' },
    { title: 'Products', link: 'product' },
    { title: 'Management', link: 'management' },
    { title: 'About', link: 'about' },
    { title: 'Contact', link: 'contact' },
  ]

  public profileItem: any[] = [
    { title: 'Login', link: '/auth/login' },
    { title: 'Register', link: '/auth/register' },
  ]

  public toggleMenu(): void {
    this.isShow = true;
  }

  public closeToggle(): void {
    this.isShow = false;
  }

}
