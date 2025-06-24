import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileStorageService {
  private baseUrl = environment.apiBaseUrl + "/api/v1/fileStorage"
  constructor(private http: HttpClient) {}

  uploadProductImage(productUUID: string, file: File): Observable<HttpResponse<string>> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('productUUID', productUUID);

    return this.http.post(`${this.baseUrl}/productImage`, formData, {withCredentials: true,  responseType: 'text' , observe: "response"});
  }

  deleteProductImage(productUUID: string): Observable<boolean> {
    return this.http.delete<boolean>(`${this.baseUrl}/productImage/${productUUID}`);
  }
}
