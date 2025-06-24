import {Injectable} from '@angular/core';
import {BehaviorSubject, interval, map, Observable, Subscription, throwError} from 'rxjs';
import {switchMap, catchError} from 'rxjs/operators';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {of} from 'rxjs';
import {environment} from '../../environments/environment';
import {UserRegisterForm} from '../components/users/user-register-form/user-register-form';
import {UserDetails, UserDto, UserRegisterRequestDto, UserUpdateRequest} from '../DTO/USERS/UsersDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public userStatus = false;
  public rol?: string = "ROL_CUSTOMER";
  private baseUrl = `${environment.apiBaseUrl}`;


  constructor(private http: HttpClient) {
    this.loadRol().subscribe();

  }

  register(userRegisterRequestDto: UserRegisterRequestDto): Observable<HttpResponse<any>> {
    return this.http.post(
      this.baseUrl + '/api/v1/users/register',
      userRegisterRequestDto,
      {
        withCredentials: true,
        observe: 'response'
      }
    )
  }

  register_new_admin(userRegisterRequestDto: UserRegisterRequestDto): Observable<HttpResponse<any>> {
    return this.http.post(
      this.baseUrl + '/api/v1/users/register_admin',
      userRegisterRequestDto,
      {
        withCredentials: true,
        observe: 'response'
      }
    )
  }

  login(username: string, password: string): Observable<HttpResponse<any>> {

    return this.http.post(
      this.baseUrl + `/login?username=${username}&password=${password}`,
      null,
      {
        withCredentials: true,
        observe: 'response'
      }
    ).pipe(map(httpResp => {

      this.userStatus = httpResp.status === 201
      return httpResp;

    }, catchError(error => {
      this.userStatus = false;
      console.log(error);
      return throwError(error)
    })))

  }

  loadRol(): Observable<HttpResponse<UserDetails>> {
    console.log("LOAD-ROL");
    console.log(this.baseUrl + "/api/v1/users/me3")
    return this.http.get<UserDetails>(
      this.baseUrl + "/api/v1/users/me3",
      {
        withCredentials: true,
        observe: 'response'
      }
    ).pipe(
      map((httpResponse: HttpResponse<UserDetails>) => {
        console.log("map")

        this.userStatus = httpResponse.status === 200;
        console.log("load-rol: userStatus = " + this.userStatus)
        this.rol = httpResponse.body?.authorities[0]?.authority ?? '';
        console.log(httpResponse);
        return httpResponse;
      }),
      catchError(error => {
        console.error("Error en loadRol:", error);
        this.userStatus = false;
        return throwError(() => error);
      })
    );
  }


  logout(): Observable<HttpResponse<any>> {
    console.log("LOGOUT-USER-SERVICE")
    return this.http.post(this.baseUrl + "/logout", null, {withCredentials: true, observe: 'response'})
  }

  getStatus() {
    return this.userStatus
  }

  getUserInfo(): Observable<UserDto> {
    return this.http.get<UserDto>(this.baseUrl + "/api/v1/users/me_info", {withCredentials: true})
  }


  updateUserInfo(userModifyRequestDto: UserUpdateRequest): Observable<HttpResponse<UserDto>> {
    console.log("userModifyRequestDto_email: " + userModifyRequestDto.email)
    return this.getUserInfo().pipe(
      switchMap((userDtoResponse: UserDto) => {
        console.log("userDtoResponse : " + userDtoResponse.uuid + " " +  userDtoResponse.email + " "+ userDtoResponse.uuid + "   ")
        console.log(this.baseUrl + `/api/v1/users/${userDtoResponse.uuid}`)
          return this.http.put<UserDto>(this.baseUrl + `/api/v1/users/${userDtoResponse.uuid}`
            , userModifyRequestDto
            , {withCredentials: true, observe: "response"})
        }),
        catchError(error => {
          console.error('Error actualizando usuario:', error);
          return throwError(() => error);
        })
      );
  }
}

export interface UserStatusDto {
  userStatus: boolean;
  rol: string
}


export interface UserStatusDto {
  userStatus: boolean;
  rol: string;
}

export interface Authority {
  authority: string;
}


