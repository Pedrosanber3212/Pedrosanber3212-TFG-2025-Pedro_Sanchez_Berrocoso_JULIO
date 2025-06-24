import { Component } from '@angular/core';
import {UserService} from '../../../../SERVICE/user-service';
import {OrderService} from '../../../../SERVICE/order-service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {ShoppingCartService} from '../../../../SERVICE/shopping-cart-service';
import {ProductService} from '../../../../SERVICE/product-service';
import {catchError, switchMap} from 'rxjs/operators';
import {HttpResponse} from '@angular/common/http';
import {OrderDto, OrderItemPlusDto} from '../../../../DTO/ORDERS/ShoppingCartDtos';
import {combineLatest, map, of, throwError} from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-show-all-orders-page',
  standalone: false,
  templateUrl: './admin-show-all-orders-page.html',
  styleUrl: './admin-show-all-orders-page.scss'
})
export class AdminShowAllOrdersPage {
  src: string = "https://i.pinimg.com/736x/cf/d8/cd/cfd8cddd259b44090f6cdaa4b502c9ee.jpg";
  listOrderDto: CustomOrderDto[] = []
  userService: UserService;
  isLoading = false;
  orderStatus: string[]  = ['PENDING', 'PROCESSING', 'SHIPPED' , 'DELIVERED', 'CANCELLED'];

  constructor(private orderService: OrderService, private snackBar: MatSnackBar, private router: Router, private shoppingCartService: ShoppingCartService, private productService: ProductService, private userService2: UserService) {
    this.userService = userService2;
    this.get_all_orders()

  }



  get_all_orders() {
    this.isLoading = true;

    this.orderService.get_all_orders_admin__().pipe(
      switchMap((httpResponseOrderDto: HttpResponse<OrderDto[]>) => {
        if (httpResponseOrderDto.status === 200 && httpResponseOrderDto.body) {
          const ordersDtoList = httpResponseOrderDto.body;

          const observables = ordersDtoList.map(orderDto => {
            const OrderItemPlusDtoListObservables = orderDto.items.map(item =>
              this.productService.getProduct(item.productUUID).pipe(
                map(productDto => ({
                  ...item,
                  productDto
                } as OrderItemPlusDto))
              )
            );

            return OrderItemPlusDtoListObservables.length > 0
              ? combineLatest(OrderItemPlusDtoListObservables).pipe(
                map(itemsWithProduct => ({
                  uuid: orderDto.uuid,
                  items: itemsWithProduct,
                  totalPrice: itemsWithProduct.reduce((acc, item) => acc + item.price * item.quantity, 0),
                  status: orderDto.status
                } as CustomOrderDto))
              )
              : of({
                uuid: orderDto.uuid,
                items: [],
                totalPrice: 0,
                status: orderDto.status
              } as CustomOrderDto);
          });

          return observables.length > 0 ? combineLatest(observables) : of([]);
        } else {
          return throwError(() => new Error("No tienes órdenes disponibles"));
        }
      }),
      catchError(error => {
        console.error("Error cargando órdenes:", error);
        return of([]);
      })
    ).subscribe({
      next: (custom__OrderDtos: CustomOrderDto[]) => {
        this.listOrderDto = custom__OrderDtos;
        console.log("Órdenes procesadas:", custom__OrderDtos);
        this.isLoading = false;
      },
      error: () => {
        this.listOrderDto = [];
        this.isLoading = false;
      }
    });
  }

  statusChanged(orderUUID: string, event: Event) {
    const event__target = event.target as HTMLSelectElement;
    const selected__value = event__target.value;
    this.orderService.updateOrderStatus(orderUUID,selected__value).subscribe(
      {
        next:(httpRespOrderDtoUpd: HttpResponse<OrderDto>)=>{
          if(httpRespOrderDtoUpd.status===200){
            console.log("userDtoNuevo: "+ httpRespOrderDtoUpd.body?.status)
            Swal.fire({
              icon: 'success',
              title: 'Estado actualizado',
              text: `La orden ha sido marcada como "${selected__value}"`,
              confirmButtonText: 'Entendido',
              customClass: {
                popup: 'custom-swal-popup',
                confirmButton: 'custom-swal-confirm-button'
              }
            });
                setTimeout(()=>{
                  this.get_all_orders()
                } , 1000)
          }

        },error: error=>{
          console.log("error en cambiar estado orden de compra")
      Swal.fire({
        icon: 'error',
        title: 'Error al actualizar',
        text: 'No se ha podido modificar el estado de la orden',
        confirmButtonText: 'Cerrar',
        customClass: {
          popup: 'custom-swal-popup',
          confirmButton: 'custom-swal-confirm-button'
        }
      });
          setTimeout(()=>{
            this.get_all_orders()
          } , 1000)
        }
      }
    )
  }
}
export interface CustomOrderDto{
  uuid: string;                 // UUID de la orden
  items: OrderItemPlusDto[];       // Lista de ítems en la orden
  totalPrice: number,
  status: 'PENDING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED'; // Enum de estado

}

