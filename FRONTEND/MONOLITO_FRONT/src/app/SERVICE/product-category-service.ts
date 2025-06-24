// src/app/services/product-category.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import {
  CreateProductCategoryRequest,
  ProductCategoryDto,
  UpdateProductCategoryRequest
} from '../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';


@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService {
  private baseUrl = `${environment.apiBaseUrl}/api/v1/productcategories`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProductCategoryDto[]> {
    return this.http.get<ProductCategoryDto[]>(this.baseUrl,{withCredentials: false});
  }

  getByUuid(uuid: string): Observable<ProductCategoryDto> {
    return this.http.get<ProductCategoryDto>(`${this.baseUrl}/${uuid}`);
  }

  createCategory(request: CreateProductCategoryRequest): Observable<ProductCategoryDto> {
    return this.http.post<ProductCategoryDto>(this.baseUrl, request, {
      withCredentials: true
    });
  }

  updateCategory(request: UpdateProductCategoryRequest): Observable<ProductCategoryDto> {
    return this.http.put<ProductCategoryDto>(this.baseUrl, request, {
      withCredentials: true
    });
  }

  deleteCategory(uuid: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${uuid}`, {
      withCredentials: true
    });
  }
}
