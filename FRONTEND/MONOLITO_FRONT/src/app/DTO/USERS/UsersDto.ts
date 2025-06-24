import {Authority} from '../../SERVICE/user-service';

export interface UserRegisterRequestDto {
  name: string;
  username: string;
  birthdate: string; // formato ISO: YYYY-MM-DD
  address: string;
  email:string;
  password: string;
  role: 'USER' | 'ADMIN'; // según los valores posibles de UserRole
}

export interface UserDto {
  uuid: string;
  name: string;
  username: string,
  email: string;
  address: string;
  role: string;
  birthdate: string; // YYYY-MM-DD
}



export interface UserUpdateRequest {
  name: string;
  email: string;
  address: string;
  birthdate: string;
}
export interface UserDetails {
  password: string | null;
  username: string;
  authorities: Authority[];
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
}
