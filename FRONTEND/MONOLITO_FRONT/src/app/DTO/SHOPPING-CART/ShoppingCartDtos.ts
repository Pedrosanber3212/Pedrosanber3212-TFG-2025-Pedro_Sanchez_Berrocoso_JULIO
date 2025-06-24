import {ProductDto} from '../PRODUCTS/ProductsDtos';

export interface ShoppingCartDto {
  uuid: string;
  userUUID: string;
  items: ShoppingCartItemDto[];
}

export interface ShoppingCartItemDto {
  uuid: string;
  productUUID: string;
  image: string;
  name: string;
  shoppingCartUUID: string;
  price: number;
  quantity: number;
}


export interface ShoppingCartItemPlusDto {
  uuid: string;
  productUUID: string;
  image: string;
  name: string;
  shoppingCartUUID: string;
  price: number;
  quantity: number;
  productDto: ProductDto;
}

