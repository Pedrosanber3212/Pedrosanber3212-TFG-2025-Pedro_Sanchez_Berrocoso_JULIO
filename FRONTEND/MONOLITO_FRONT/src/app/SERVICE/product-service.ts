// src/app/services/product.service.ts
import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import {CreateProductRequest, ProductDto, UpdateProductRequest} from '../DTO/PRODUCTS/ProductsDtos';


@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = `${environment.apiBaseUrl}/api/v1/products`;

  constructor(private http: HttpClient) {}

  getProduct(uuid: string): Observable<ProductDto> {
    return this.http.get<ProductDto>(`${this.baseUrl}/${uuid}`, { withCredentials: true });
  }

  getFilteredProducts(
    page: number,
    size: number,
    sort: string,
    name?: string,
    minPrice?: number,
    maxPrice?: number,
    categoryUUID?: string
  ): Observable<ProductDto[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (name){ params = params.set('name', name)};
    if (minPrice !== undefined) {params = params.set('minPrice', minPrice.toString())};
    if (maxPrice !== undefined){ params = params.set('maxPrice', maxPrice.toString())};
    if (categoryUUID) {params = params.set('categoryUUID', categoryUUID)};

    return this.http.get<ProductDto[]>(`${this.baseUrl}/page`, {
      params,
      withCredentials: false
    });
  }

  createProduct(request: CreateProductRequest): Observable<HttpResponse<any>> {
    return this.http.post<void>(this.baseUrl, request, { withCredentials: true, observe:"response" });
  }

  updateProduct(uuid: string, request: UpdateProductRequest): Observable<HttpResponse<ProductDto>> {
    return this.http.put<ProductDto>(`${this.baseUrl}/${uuid}`, request, {
      withCredentials: true, observe:"response"
    });
  }

  deleteProduct(uuid: string):  Observable<HttpResponse<any>> {
    return this.http.delete<void>(`${this.baseUrl}/${uuid}`, { withCredentials: true ,observe:"response"});
  }
}
