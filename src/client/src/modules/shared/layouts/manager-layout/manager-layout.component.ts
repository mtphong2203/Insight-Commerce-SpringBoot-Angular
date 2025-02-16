import { Component } from '@angular/core';
import { HeaderComponent } from "../../common/header/header.component";
import { RouterModule } from '@angular/router';
import { SidebarComponent } from "../../common/sidebar/sidebar.component";

@Component({
  selector: 'app-manager-layout',
  standalone: true,
  imports: [HeaderComponent, RouterModule, SidebarComponent],
  templateUrl: './manager-layout.component.html',
  styleUrl: './manager-layout.component.css'
})
export class ManagerLayoutComponent {

}
