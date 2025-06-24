import {Component} from '@angular/core';
import {
  ShoppingCartDto,
  ShoppingCartItemDto,
  ShoppingCartItemPlusDto
} from '../../../DTO/SHOPPING-CART/ShoppingCartDtos';
import {ProductService} from '../../../SERVICE/product-service';
import {UserService} from '../../../SERVICE/user-service';
import {Router} from '@angular/router';
import {ShoppingCartService} from '../../../SERVICE/shopping-cart-service';
import {HttpResponse} from '@angular/common/http';
import {MatSnackBar} from '@angular/material/snack-bar';
import {catchError, switchMap} from 'rxjs/operators';
import {combineLatest, map, Observable, of, throwError} from 'rxjs';
import {OrderService} from '../../../SERVICE/order-service';
import {OrderDto} from '../../../DTO/ORDERS/ShoppingCartDtos';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-show-my-shopping-cart-page',
  standalone: false,
  templateUrl: './show-my-shopping-cart-page.html',
  styleUrl: './show-my-shopping-cart-page.scss'
})
export class ShowMyShoppingCartPage {
  shoppingCartItemDtoList: ShoppingCartItemPlusDto[] = [];
  userService: UserService;
  isLoading = false;

  constructor(private orderService: OrderService, private snackBar: MatSnackBar, private router: Router, private shoppingCartService: ShoppingCartService, private productService: ProductService, private userService2: UserService) {
    this.userService = userService2;
    this.updateThisShoppingCartItemDto()

  }

  deleteFromCart(uuid: string) {
    this.shoppingCartService.removeItem(uuid).subscribe(
      {
        next: (httpResponseWithShoppingCartDto: HttpResponse<ShoppingCartDto>) => {
          console.log("httpResponseWithShoppingCartDto.status === 200: status= " + httpResponseWithShoppingCartDto.status)
          if (httpResponseWithShoppingCartDto.status === 200)
            console.log("add to cart - httpResponseWithShoppingCartDto: " + httpResponseWithShoppingCartDto)
          Swal.fire({
            icon: 'success',
            title: 'Producto eliminado',
            text: 'Se ha eliminado correctamente del carrito',
            showConfirmButton:false,
            background: '#ffffff',
            color: '#1d1d1f',
            confirmButtonColor: '#0071e3'
          });
          this.updateThisShoppingCartItemDto();
        }
        , error: error => {
          console.log("ERROR PRODUCT: " + error)
          Swal.fire({
            icon: 'error',
            title: 'Error al eliminar',
            text: 'No se pudo eliminar el producto. Inténtalo más tarde.',
            showConfirmButton:false,
            background: '#fff0f0',
            color: '#1d1d1f',
            confirmButtonColor: '#ff4d4f'
          });
        }
      }
    )

  }


  addToCart(uuid: string) {
    this.shoppingCartService.addItem(uuid).subscribe(
      {
        next: (httpResponseWithShoppingCartDto: HttpResponse<ShoppingCartDto>) => {
          console.log("httpResponseWithShoppingCartDto.status === 200: status= " + httpResponseWithShoppingCartDto.status)
          if (httpResponseWithShoppingCartDto.status === 200)
            console.log("add to cart - httpResponseWithShoppingCartDto: " + httpResponseWithShoppingCartDto)
          Swal.fire({
            icon: 'success',
            title: 'Producto añadido',
            text: 'Se ha añadido correctamente al carrito',
            showConfirmButton:false,
            background: '#ffffff',
            color: '#1d1d1f',
            confirmButtonColor: '#0071e3'
          });
        }
        , error: error => {
          console.log("ERROR PRODUCT: " + error)
          Swal.fire({
            icon: 'error',
            title: 'Error al añadir',
            text: 'No se pudo añadir al carrito. Inténtalo más tarde.',
            showConfirmButton:false,
            confirmButtonText: 'Cerrar',
            background: '#fff0f0',
            color: '#1d1d1f',
            confirmButtonColor: '#ff4d4f'
          });
        }
      }
    )
    setTimeout(()=>{this.isLoading = true;this.updateThisShoppingCartItemDto();}, 300)

  }

  updateThisShoppingCartItemDto() {
    this.isLoading = true;

    if (this.userService.userStatus) {
      this.shoppingCartService.getCart().pipe(
        switchMap((httpResponseShCartDto: HttpResponse<ShoppingCartDto>) => {
          if (httpResponseShCartDto.status === 200 && httpResponseShCartDto.body?.items) {
            let arrayShopCartItemPlusObservables: Array<Observable<ShoppingCartItemPlusDto>> = httpResponseShCartDto.body.items.map((shoppingCartItemDto: ShoppingCartItemDto) =>
              this.productService.getProduct(shoppingCartItemDto.productUUID).pipe(
                map(productDto => ({
                  ...shoppingCartItemDto,
                  productDto
                } as ShoppingCartItemPlusDto))
              )
            );
            // Combina todas las peticiones en paralelo
            return arrayShopCartItemPlusObservables.length > 0 ? combineLatest(arrayShopCartItemPlusObservables) : of([]);
          }
          else {
            return  throwError(() => new Error("No tiene ningun producto en el carrito"));
          }
        }),
        catchError(error => {
          console.error("Error obteniendo el carrito o productos:", error);
          return throwError(() => error);
        })
      ).subscribe({
        next: (ShoppingCartItemPlusDtoList: ShoppingCartItemPlusDto[]) => {

          this.shoppingCartItemDtoList = ShoppingCartItemPlusDtoList; // o guarda todo en otro array si lo necesitas
          console.log("Carrito actualizado con información de producto:", ShoppingCartItemPlusDtoList);
        }, error: error => {
          this.shoppingCartItemDtoList = []
          console.log(error);

        }
      });
    }
    this.shoppingCartItemDtoList.sort((a, b) => {
      if (a.name < b.name) return -1;
      if (a.name > b.name) return 1;
      return 0;
    });

    setTimeout(()=>{this.isLoading = false;}, 300)

  }



  createOrder() {
    this.orderService.createOrderFromCart().subscribe({
      next: (httpResponseWithOrderDto: HttpResponse<OrderDto>) => {
        console.log("httpResponseWithOrderDto.status === 200: status= " + httpResponseWithOrderDto.status);
        if (httpResponseWithOrderDto.status === 200) {
          // Mensaje Swal de éxito
          Swal.fire({
            icon: 'success',
            title: '¡Pedido realizado!',
            text: 'Tu pedido ha sido tramitado correctamente',
            confirmButtonText: 'Aceptar',
            background: '#ffffff',
            color: '#1d1d1f',
            confirmButtonColor: '#0071e3'
          });
  
          this.shoppingCartItemDtoList = [];
        }
      },
      error: (error) => {
        console.log("ERROR PRODUCT: " + error);
        Swal.fire({
          icon: 'error',
          title: 'Error al tramitar el pedido',
          text: 'Por favor, inténtalo de nuevo.',
          confirmButtonText: 'Cerrar',
          background: '#fff0f0',
          color: '#1d1d1f',
          confirmButtonColor: '#ff4d4f'
        });
      }
    });
  }
  
}
