import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserService} from '../../../../SERVICE/user-service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-login-form',
  templateUrl: './user-login-form.html',
  standalone: false,
  styleUrls: ['./user-login-form.scss']
})
export class UserLoginForm {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router,   private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      this.userService.login(username, password).subscribe({
        next: (httpResponse) => {
         if (httpResponse.status === 200) {
              this.userService.userStatus = true;

              Swal.fire({
                icon: 'success',
                title: 'Inicio de sesión exitoso',
                text: 'Bienvenido de nuevo',
                confirmButtonColor: '#007aff',
                customClass: {
                  popup: 'custom-swal-popup',
                  confirmButton: 'custom-swal-confirm-button'
                }
              }).then(() => {
                this.router.navigate(['']);
              });
            }
        },
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Credenciales incorrectas',
            confirmButtonColor: '#ff3b30',
            confirmButtonText: 'Intenta de nuevo',
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
