import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../../SERVICE/user-service';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserRegisterRequestDto} from '../../../DTO/USERS/UsersDto';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-register-form',
  standalone: false,
  templateUrl: './user-register-form.html',
  styleUrl: './user-register-form.scss'
})
export class UserRegisterForm {
  loginForm: FormGroup;
  responseLogin: string = '';
  minDate: Date = new Date(1920, 0, 1);
  maxDate: Date = new Date();


  constructor(private fb: FormBuilder, private userService: UserService, private router: Router,   private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', Validators.required],
      address:['', Validators.required],
      birthdate: ['', Validators.required],
      password: ['', Validators.required],
      role: ['CUSTOMER']
    });
  }



  onSubmit() {
    if (this.loginForm.valid) {
      const formValue = this.loginForm.value;

      var birthdate: Date = new Date(formValue.birthdate);
      birthdate.setDate(birthdate.getDate() + 1);

      var birtdateToIso = birthdate.toISOString().split("T")[0]

      const userRegisterRequestDto: UserRegisterRequestDto = {
        ...formValue,
        birthdate: birtdateToIso,
        role: 'CUSTOMER'
      };

    console.log(userRegisterRequestDto)
      this.userService.register(userRegisterRequestDto).subscribe({
        next: (httpResponse) => {
          if (httpResponse.status === 200) {
            Swal.fire({
              icon: 'success',
              title: '¡Registro exitoso!',
              text: 'Tu cuenta se ha creado correctamente.',
              confirmButtonColor: '#007aff',
              confirmButtonText: 'Ir a inicio',
              customClass: {
                popup: 'custom-swal-popup',
                confirmButton: 'custom-swal-confirm-button'
              }
            }).then(() => {
              this.router.navigate(['/user_login_form']);
            });
          }
        },
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Error en el registro',
            text: 'Ha ocurrido un problema al crear la cuenta.',
            confirmButtonColor: '#ff3b30',
            confirmButtonText: 'Intentar de nuevo',
            customClass: {
              popup: 'custom-swal-popup',
              confirmButton: 'custom-swal-confirm-button'
            }
          });
        }
      });
    }
  }

}
