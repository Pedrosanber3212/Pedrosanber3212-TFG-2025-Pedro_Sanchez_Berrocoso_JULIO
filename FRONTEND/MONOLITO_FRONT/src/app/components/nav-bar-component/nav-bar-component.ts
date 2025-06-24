import {Component, OnInit} from '@angular/core';
import {UserService} from '../../SERVICE/user-service';
import {Subscription} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {UserModalComponent} from '../users/user-modal-component/user-modal-component';
import {Route, Router} from '@angular/router';
import {ShowMyShoppingCartPage} from '../ShoppingCart/show-my-shopping-cart-page/show-my-shopping-cart-page';
import {NavBarService} from '../../SERVICE/nav-bar-service';
import {ShoppingCartService} from '../../SERVICE/shopping-cart-service';
import {ShoppingCartItemDto} from '../../DTO/SHOPPING-CART/ShoppingCartDtos';
import Swal from 'sweetalert2';


@Component({
  selector: 'nav-bar-component',
  standalone: false,
  templateUrl: './nav-bar-component.html',
  styleUrl: './nav-bar-component.scss'
})
export class NavBarComponent implements OnInit {

   searchValue : string  = ""
   public userService2 : UserService | null = null
   showUserMenu = false;



  constructor(private navBarService :NavBarService, private userService:UserService, private dialog: MatDialog ,public router:Router) {

  }
  ngOnInit(): void {
    this.showUserMenu = false;
    this.userService2 = this.userService;

    this.userService.loadRol().subscribe({
      next: () => {
      },
      error: () => {
      }
    });

  }


  search(){
      this.navBarService.next(this.searchValue)
  }


  openUser_Modal1() {

    if(this.userService2?.rol !== 'ROLE_CUSTOMER' && this.userService2?.rol !== 'ROLE_ADMIN' ){
      this.router.navigate(['/user_login_form']);
    }

  }

  openCart_Modal1() {
    this.userService.loadRol().subscribe({
      next: () => {
        if (this.userService.getStatus()) {
          const modalHeight = '25vh';
          const modalWidth = '50vw';
          this.dialog.open(ShowMyShoppingCartPage, {
            width: modalWidth,
            minHeight:'10vh',
            height: 'auto',
            maxHeight:'25vh',
            position: {
              top: '10vh',
              right: '1vw'
            },
            panelClass: 'custom-modal-class'
          });
        } else {
          this.mostrarAlertaLogin();
        }
      },
      error: () => {
        this.mostrarAlertaLogin();
      }
    });
  }

  private mostrarAlertaLogin() {
    Swal.fire({
      icon: 'error',
      title: 'Acceso denegado',
      text: 'Tienes que iniciar sesión',
      confirmButtonText: 'Entendido',
      customClass: {
        popup: 'custom-swal-popup',
        confirmButton: 'custom-swal-confirm-button'
      }
    });
  }


  redirectToProducts(){
    this.router.navigate([""])
  }

  redirectToModifyAccount(){
     this.router.navigate(["/user_modify_form"])
  }


  logout(): void {
    this.userService.logout().subscribe({
      next: (httpResponse) => {
        if (this.userService2) {
          this.userService2.userStatus = false;
          this.userService2.rol = undefined;
        }
        if (httpResponse.status === 200) {
          Swal.fire({
            icon: 'success',
            title: 'Sesión cerrada',
            text: 'Has cerrado sesión correctamente',
            confirmButtonText: 'Aceptar',
            customClass: {
              popup: 'custom-swal-popup',
              confirmButton: 'custom-swal-confirm-button'
            }
          }).then(() => {
            this.router.navigate(['/']);
          });
        }
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Ha ocurrido un error al cerrar la sesión',
          confirmButtonText: 'Entendido',
          customClass: {
            popup: 'custom-swal-popup',
            confirmButton: 'custom-swal-confirm-button'
          }
        });
      }
    });
  }


  modificarAccount(){
     this.router.navigate(["/user_modify_form"])
/*     this.closeMatDialog()
 */  }


  manageAdminProducts() {
    this.router.navigate(["/admin/show_all_products_page"])

  }
  manageAdminProductsCategories() {
    this.router.navigate(["/admin/show_all_productCategories"])

  }

  maange__orders() {
    this.router.navigate(["/admin/show_all_orders_page"])
  }

  customerLink_to_my_orders() {
    this.router.navigate(["/show_all_my_orders_page"])

  }

}
