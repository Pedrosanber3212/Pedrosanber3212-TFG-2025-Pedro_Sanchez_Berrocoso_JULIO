import {Component} from '@angular/core';
import {
  ShoppingCartDto,
  ShoppingCartItemDto,
  ShoppingCartItemPlusDto
} from '../../../DTO/SHOPPING-CART/ShoppingCartDtos';
import {UserService} from '../../../SERVICE/user-service';
import {OrderService} from '../../../SERVICE/order-service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {ShoppingCartService} from '../../../SERVICE/shopping-cart-service';
import {ProductService} from '../../../SERVICE/product-service';
import {HttpResponse} from '@angular/common/http';
import {catchError, switchMap} from 'rxjs/operators';
import {combineLatest, map, Observable, of, throwError} from 'rxjs';
import {OrderDto, OrderItemDto, OrderItemPlusDto} from '../../../DTO/ORDERS/ShoppingCartDtos';

@Component({
  selector: 'app-show-all-my-orders-page',
  standalone: false,
  templateUrl: './show-all-my-orders-page.html',
  styleUrl: './show-all-my-orders-page.scss'
})
export class ShowAllMyOrdersPage {
  src: string = "https://i.pinimg.com/736x/cf/d8/cd/cfd8cddd259b44090f6cdaa4b502c9ee.jpg";
  listOrderDto: CustomOrderDto[] = []
  userService: UserService;
  isLoading = false;

  constructor(private orderService: OrderService, private snackBar: MatSnackBar, private router: Router, private shoppingCartService: ShoppingCartService, private productService: ProductService, private userService2: UserService) {
    this.userService = userService2;
    this.getOrders()

  }



  getOrders() {
    this.isLoading = true;

    this.orderService.getAllMyOrders().pipe(
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
          return throwError(() => new Error("No tiene órdenes disponibles"));
        }
      }),
      catchError(error => {
        console.error("Error cargando órdenes:", error);
        return of([]); // Devuelve lista vacía si falla
      })
    ).subscribe({
      next: (customOrderDtos: CustomOrderDto[]) => {
        this.listOrderDto = customOrderDtos;
        console.log("Órdenes procesadas:", customOrderDtos);
        this.isLoading = false;
      },
      error: () => {
        this.listOrderDto = [];
        this.isLoading = false;
      }
    });
  }

}
    export interface CustomOrderDto{
  uuid: string;                 // UUID de la orden
  items: OrderItemPlusDto[];       // Lista de ítems en la orden
  totalPrice: number,
  status: 'PENDING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED'; // Enum de estado

}


