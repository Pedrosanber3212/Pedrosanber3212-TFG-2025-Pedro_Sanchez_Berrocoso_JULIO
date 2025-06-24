// src/app/services/shopping-cart.service.ts

import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {ShoppingCartDto} from '../DTO/SHOPPING-CART/ShoppingCartDtos';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {
  private baseUrl = `${environment.apiBaseUrl}/api/v1/shoppingCart`;

  constructor(private http: HttpClient) {}

  getCart(): Observable<HttpResponse<ShoppingCartDto>> {
    console.log("ShoppingCartService -> getCart")
    return this.http.get<ShoppingCartDto>(`${this.baseUrl}`, { withCredentials: true,observe: "response" });
  }

  addItem(productUUID: string): Observable<HttpResponse<ShoppingCartDto>> {
    console.log(`${this.baseUrl}/${productUUID}`)
    return this.http.post<ShoppingCartDto>(`${this.baseUrl}/${productUUID}`, null, { withCredentials: true,observe: "response" });
  }

  removeItem(shoppingCartItemUUID: string): Observable<HttpResponse<ShoppingCartDto>> {
    return this.http.delete<ShoppingCartDto>(`${this.baseUrl}/${shoppingCartItemUUID}`, { withCredentials: true,observe: "response" });
  }

  clearCart(): Observable<HttpResponse<ShoppingCartDto>> {
    return this.http.delete<ShoppingCartDto>(`${this.baseUrl}`, { withCredentials: true,observe: "response" });
  }
}
