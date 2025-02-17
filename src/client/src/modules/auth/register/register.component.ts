import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AUTH_SERVICE } from '../../../constants/injection.constant';
import { IAuthService } from '../../../services/auth/auth-service.interface';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  public formRegister!: FormGroup;

  public genders: any[] = [
    { id: 'man', label: 'man', title: 'Man', value: true },
    { id: 'woman', label: 'woman', title: 'Woman', value: false },
  ]

  constructor(@Inject(AUTH_SERVICE) private authService: IAuthService, private router: Router) {
    this.createForm();
  }

  private createForm(): void {
    this.formRegister = new FormGroup({
      fullName: new FormControl('', [Validators.required, Validators.maxLength(50)]),
      username: new FormControl('', [Validators.required, Validators.maxLength(50)]),
      email: new FormControl('', [Validators.required, Validators.maxLength(50)]),
      phoneNumber: new FormControl('', [Validators.required, Validators.maxLength(50)]),
      dateOfBirth: new FormControl(null),
      address: new FormControl('', Validators.maxLength(50)),
      gender: new FormControl(false),
      password: new FormControl('', [Validators.required, Validators.maxLength(50)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    })
  }

  public onSubmit(): void {
    if (this.formRegister.invalid) {
      return;
    }

    const data = this.formRegister.value;
    this.authService.register(data).subscribe((result: any) => {
      if (result) {
        this.router.navigate(['/auth/login']);
        console.log('Register successfully');
      }
    })

  }

}
