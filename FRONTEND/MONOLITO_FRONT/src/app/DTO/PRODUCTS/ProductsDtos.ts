

export interface ProductDto {
  uuid: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  image: string;
  categoryUUID: string;
}

export interface CreateProductRequest {
  name: string;
  description: string;
  price: number;
  stock: number;
  image: string;
  categoryUUID: string;
}

export interface UpdateProductRequest {
  name: string;
  description: string;
  price: number;
  stock: number;
  image: string;
  categoryUUID: string;
}
