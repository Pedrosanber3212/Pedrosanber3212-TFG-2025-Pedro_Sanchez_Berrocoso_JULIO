import { Component } from '@angular/core';
import {EmailValidator, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../../SERVICE/user-service';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserDto, UserUpdateRequest} from '../../../DTO/USERS/UsersDto';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-modify-form',
  standalone: false,
  templateUrl: './user-modify-form.html',
  styleUrl: './user-modify-form.scss'
})
export class UserModifyForm {
  modifyForm: FormGroup;



  constructor(private fb: FormBuilder, private userService: UserService, private router: Router,   private snackBar: MatSnackBar
  ) {
    this.modifyForm = this.fb.group({
      name: ['', Validators.required],
      birthdate:['', Validators.required],
      email: ['', Validators.required],
      address: ['', Validators.required]
    });

    this.userService.getUserInfo().subscribe({
      next: userDto => {
        this.modifyForm.patchValue({

          name: userDto.name,
          birthdate:userDto.birthdate,
          email: userDto.email,
          address: userDto.address,
        });
      }
    })


  }

  onSubmit() {
    console.log("formulario modify account: " + this.modifyForm.value.toString())
    if (this.modifyForm.valid) {
      const {name, username, birthdate, email, address} = this.modifyForm.value;
      console.log(this.modifyForm.value)
      if (this.modifyForm.valid) {
        console.log("campos formulario validos")

        const formValue = this.modifyForm.value;

        let birthdate: Date = new Date(formValue.birthdate);
        birthdate.setDate(birthdate.getDate() + 0 );

        let birthdte = birthdate.toISOString().split("T")[0]

        const userModifyRequestDto: UserUpdateRequest = {
          ...formValue,
          birthdate: birthdte
        };

        console.log("payload enviado: "+userModifyRequestDto.toString())
        this.userService.updateUserInfo(userModifyRequestDto).subscribe({
          next: (httpResponseUserDto) => {
            if (httpResponseUserDto.status === 200) {
              console.log("Nuevos valores del userDto username nuevo: " + httpResponseUserDto.body?.username)

              Swal.fire({
                icon: 'success',
                title: '¡Perfil actualizado!',
                text: 'Tu perfil se ha actualizado correctamente.',
                confirmButtonText: 'Aceptar',
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
              text: 'No se pudo actualizar tu cuenta. Intenta nuevamente.',
              confirmButtonText: 'Cerrar',
              customClass: {
                popup: 'custom-swal-popup',
                confirmButton: 'custom-swal-confirm-button'
              }
            });
          }
        });
      }
    }else{
      console.log("formulario no valido")
    }
  }

}
