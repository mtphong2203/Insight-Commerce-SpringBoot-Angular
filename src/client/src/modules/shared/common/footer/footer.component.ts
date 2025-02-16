import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule, IconDefinition } from '@fortawesome/angular-fontawesome';
import { faFacebook, faTiktok, faYoutube } from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink, FontAwesomeModule],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {

  public currentYear: Number = new Date().getFullYear();

  public facebookIcon: IconDefinition = faFacebook;
  public youtubeIcon: IconDefinition = faYoutube;
  public tikTokIcon: IconDefinition = faTiktok;

}
