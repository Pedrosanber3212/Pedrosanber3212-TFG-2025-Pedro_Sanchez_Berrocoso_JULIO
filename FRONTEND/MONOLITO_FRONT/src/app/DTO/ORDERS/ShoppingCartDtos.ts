import {ProductDto} from '../PRODUCTS/ProductsDtos';

export interface OrderItemDto {
  uuid: string;
  orderUUID: string;
  productUUID: string;
  quantity: number;
  price: number;
  image: string;
  name: string;
}

export interface OrderDto {
  uuid: string;
  userUUID: string;
  creationDate: string;
  items: OrderItemDto[];
  status: 'PENDING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
}

export interface OrderItemPlusDto {
  uuid: string;
  orderUUID: string;
  productUUID: string;
  quantity: number;
  price: number;
  image: string;
  name: string;
  productDto: ProductDto;
}
