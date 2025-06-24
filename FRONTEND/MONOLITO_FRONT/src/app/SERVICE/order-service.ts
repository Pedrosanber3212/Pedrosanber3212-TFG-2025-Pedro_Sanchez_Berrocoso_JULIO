// src/app/services/order.service.ts
import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import {OrderDto} from '../DTO/ORDERS/ShoppingCartDtos';


@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private baseUrl = `${environment.apiBaseUrl}/api/v1/orders`;

  constructor(private http: HttpClient) {}

  createOrderFromCart(): Observable<HttpResponse<OrderDto>> {
    return this.http.post<OrderDto>(`${this.baseUrl}/create`, null, { withCredentials: true , observe: "response"});
  }

  getOrder(orderUUID: string): Observable<OrderDto> {
    return this.http.get<OrderDto>(`${this.baseUrl}/order/${orderUUID}`, { withCredentials: true });
  }

  getAdminOrder(orderUUID: string): Observable<OrderDto> {
    return this.http.get<OrderDto>(`${this.baseUrl}/admin/order/${orderUUID}`, { withCredentials: true });
  }

  get_all_orders_admin__(): Observable<HttpResponse<OrderDto[]>> {
    return this.http.get<OrderDto[]>(`${this.baseUrl}/admin/allOrders`, { withCredentials: true,  observe: "response" });
  }

  getAllMyOrders(): Observable<HttpResponse<OrderDto[]>> {
    return this.http.get<OrderDto[]>(`${this.baseUrl}/allMyOrders`, { withCredentials: true,  observe: "response" });
  }

  deleteOrderAdmin(orderUUID: string): Observable<OrderDto[]> {
    return this.http.delete<OrderDto[]>(`${this.baseUrl}/admin/deleteOrder/${orderUUID}`, { withCredentials: true });
  }

  updateOrderStatus(orderUUID: string, orderStatus: string): Observable<HttpResponse<OrderDto>> {
    return this.http.put<OrderDto>(`${this.baseUrl}/admin/updateOrder/${orderUUID}/${orderStatus}`, null, { withCredentials: true, observe:"response" });
  }
}
