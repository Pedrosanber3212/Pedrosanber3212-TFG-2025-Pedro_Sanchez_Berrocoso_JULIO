import {Component, OnInit} from '@angular/core';
import {UserService, UserStatusDto} from '../../../SERVICE/user-service';
import {firstValueFrom, Subscription} from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-modal-component',
  standalone: false,
  templateUrl: './user-modal-component.html',
  styleUrl: './user-modal-component.scss'
})
export class UserModalComponent implements OnInit{

public userService2 : UserService | null = null
  public isLoading = true;

   ngOnInit() {
    try {
      const response = this.userService.loadRol().subscribe();
      console.log("Rol cargado en ngOnInit UserModalComp:");
      this.userService2 = this.userService;

      setTimeout(() => {
        this.isLoading= false
        console.log("fin espera")
        console.log("userStatus: "+ this.userService.userStatus  )
        this.isLoading= false
        console.log("this.isLoading: " + this.isLoading)
        console.log("userService2?.rol : "+this.userService2?.rol)
      }, 300);



    } catch (error) {
      console.error("Error al cargar rol en userModal:", error);
      this.isLoading= false
      this.userService.userStatus = false
    }
  }
  constructor(private userService: UserService,    private dialogRef: MatDialogRef<UserModalComponent>, private snackBar: MatSnackBar
  ,private router: Router) {

  }
  closeMatDialog(){
    this.dialogRef.close();
  }
  logout(){
    console.log("logut")
    this.userService.logout().subscribe(
      {
        next: (httpResponse) => {
          this.userService.userStatus = false;
          if (httpResponse.status === 200) {
            this.snackBar.open('Sesion cerrada correctamente', 'Cerrar', {
              duration: 3000,
              panelClass: ['snackbar-success']
            });

            setTimeout(() => {
              this.closeMatDialog()
              this.router.navigate(['']);
            }, 100);
          }
        },
        error: () => {
          this.snackBar.open('Error al cerrar sesión', 'Cerrar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }
      })

  }
  modifyAccount(){
     this.router.navigate(["/user_modify_form"])
    this.closeMatDialog()
  }
  manageAdminProducts() {
    this.router.navigate(["/admin/show_all_products_page"])

  }
  manageAdminProductsCategories() {
    this.router.navigate(["/admin/show_all_productCategories"])

  }

  managePurchaseOrders() {
    this.router.navigate(["/admin/show_all_orders_page"])
  }

  customerlinkToMyOrders() {
    this.router.navigate(["/show_all_my_orders_page"])

  }
}


/*
*
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../SERVICE/user-service';
import { Router } from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-login-form',
  templateUrl: './user-login-form.html',
  standalone: false,
  styleUrls: ['./user-login-form.scss']
})
export class UserLoginForm {
  loginForm: FormGroup;
  responseLogin: string = '';

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
            this.snackBar.open('Login correcto', 'Cerrar', {
              duration: 3000,
              panelClass: ['snackbar-success']
            });

            setTimeout(() => {
              this.router.navigate(['']);
            }, 2500);
          }
        },
        error: () => {
          this.snackBar.open('Error al iniciar sesión', 'Cerrar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }
      });
    }
  }

}

*
*
*
*
*
<div class="login-container" style="margin-top: 15vh;">
  <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="login-form" autocomplete="off"
        style="display: flex; flex-direction: column; flex-wrap: wrap; align-items: center; justify-content: space-between;">
      <h1 style="font-family: 'Century Schoolbook',serif; font-size: large; gap: 5vh;margin-bottom: 3vh; ">Iniciar sesión</h1>

m

    <mat-form-field appearance="outline" class="full-width">
      <mat-label>Usuario</mat-label>
      <input matInput formControlName="username"/>
    </mat-form-field>

    <mat-form-field appearance="outline" class="full-width">
      <mat-label>Contraseña</mat-label>
      <input matInput type="password" formControlName="password"/>
    </mat-form-field>

    <button mat-raised-button color="primary" type="submit" class="full-width">Submit</button>
  </form>


</div>


*/
