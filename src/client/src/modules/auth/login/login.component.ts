import { Component, Inject } from '@angular/core';
import { AUTH_SERVICE } from '../../../constants/injection.constant';
import { IAuthService } from '../../../services/auth/auth-service.interface';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  public formLogin!: FormGroup;

  constructor(@Inject(AUTH_SERVICE) private authService: IAuthService, private router: Router) {
    this.createForm();
  }

  private createForm(): void {
    this.formLogin = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.maxLength(50)]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    })
  }

  public onSubmit(): void {
    const data = this.formLogin.value;
    this.authService.login(data).subscribe((result: any) => {
      if (result) {
        console.log('Login successfully!');
        this.router.navigate(['/']);
      }
    })
  }

}
