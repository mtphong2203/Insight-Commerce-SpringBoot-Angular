import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  public logoEShop: string = './assets/images/E-Shop.jpg';

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

}
