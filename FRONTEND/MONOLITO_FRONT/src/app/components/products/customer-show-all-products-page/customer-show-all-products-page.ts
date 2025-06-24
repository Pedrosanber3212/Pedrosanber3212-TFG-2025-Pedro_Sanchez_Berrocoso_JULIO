import {Component} from '@angular/core';
import {ProductCategoryDto} from '../../../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';
import {ProductDto} from '../../../DTO/PRODUCTS/ProductsDtos';
import {ProductService} from '../../../SERVICE/product-service';
import {environment} from '../../../../environments/environment';
import {ProductCategoryService} from '../../../SERVICE/product-category-service';
import {MatSelect} from '@angular/material/select';
import {ShoppingCartService} from '../../../SERVICE/shopping-cart-service';
import {ShoppingCartDto} from '../../../DTO/SHOPPING-CART/ShoppingCartDtos';
import {MatSnackBar} from '@angular/material/snack-bar';
import {HttpResponse} from '@angular/common/http';
import {UserService} from '../../../SERVICE/user-service';
import {NavBarService} from '../../../SERVICE/nav-bar-service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-customer-show-all-products-page',
  standalone: false,
  templateUrl: './customer-show-all-products-page.html',
  styleUrl: './customer-show-all-products-page.scss'
})
export class CustomerShowAllProductsPage {
  productCategoryList: ProductCategoryDto[] = [];
  productList: ProductDto[] = [];
  visiblePagesArray: number[] = [];
  hasNextPageBoolean: boolean = false;

  public userService: UserService;

  pageSelected: number = 0;
  sizeSelected: number = 4;
  sortSelected: string = "name,asc";
  nameSelected?: string;
  minPriceSelected?: number = 0;
  maxPriceSelected?: number = 1000;
  categoryUUID?: string;

  constructor(
    private navBarService: NavBarService,
    private userService2: UserService,
    private productService: ProductService,
    private productCategoryService: ProductCategoryService,
    private shoppingCartService: ShoppingCartService,
    private snackBar: MatSnackBar
  ) {
    this.userService = userService2;
    this.fetchProducts();

    this.productCategoryService.getAll().subscribe({
      next: (categories) => (this.productCategoryList = categories),
      error: (err) => console.log("ERROR CATEGORIES: ", err),
    });

    this.navBarService.getSearchBarObservable().subscribe({
      next: (searchBarValue: string) => {
        this.nameSelected = searchBarValue;
        this.applyFilters();
      },
      error: (error) => console.log(error)
    });
  }

  fetchProducts() {
    this.productService.getFilteredProducts(
      this.pageSelected,
      this.sizeSelected,
      this.sortSelected,
      this.nameSelected,
      this.minPriceSelected,
      this.maxPriceSelected,
      this.categoryUUID
    ).subscribe({
      next: (products) => {
        this.productList = products;
        this.checkNextPage();
      },
      error: (err) => console.log("ERROR PRODUCTOS: ", err)
    });
  }

  checkNextPage() {
    this.productService.getFilteredProducts(
      this.pageSelected + 1,
      this.sizeSelected,
      this.sortSelected,
      this.nameSelected,
      this.minPriceSelected,
      this.maxPriceSelected,
      this.categoryUUID
    ).subscribe({
      next: (nextPageProducts) => {
        this.hasNextPageBoolean = nextPageProducts.length > 0;
        this.updatePagination(this.pageSelected * this.sizeSelected + this.productList.length + (this.hasNextPageBoolean ? 1 : 0));
      }
    });
  }

  moveForwardButton() {
    if (this.hasNextPageBoolean) {
      this.pageSelected++;
      this.fetchProducts();
    }
  }

  moveBackButton() {
    if (this.pageSelected > 0) {
      this.pageSelected--;
      this.fetchProducts();
    }
  }

  goToPage(page: number) {
    this.pageSelected = page;
    this.fetchProducts();
  }

  updatePagination(totalItems: number) {
    const pageSize = this.sizeSelected;
    const maxVisiblePages = 5;
    const totalPages = Math.ceil(totalItems / pageSize);
    const half = Math.floor(maxVisiblePages / 2);

    let start = Math.max(this.pageSelected - half, 0);
    let end = Math.min(start + maxVisiblePages, totalPages);

    if (end - start < maxVisiblePages) {
      start = Math.max(end - maxVisiblePages, 0);
    }

    this.visiblePagesArray = Array.from({ length: end - start }, (_, i) => start + i);
  }

  applyFilters() {
    this.pageSelected = 0;
    this.fetchProducts();
  }

  addToCart(uuid: string) {
    this.shoppingCartService.addItem(uuid).subscribe({
      next: (response: HttpResponse<ShoppingCartDto>) => {
        Swal.fire({
          icon: 'success',
          title: 'Producto añadido',
          text: 'El producto se ha añadido correctamente al carrito',
          showConfirmButton: false,
          timer: 2000,
          toast: true,
          position: 'top-end',
          background: '#ffffff',
          color: '#1d1d1f',
          customClass: {
            popup: 'swal2-toast',
          }
        });
      },
      error: (error) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo añadir el producto al carrito.',
          confirmButtonText: 'Cerrar',
          background: '#fff0f0',
          color: '#1d1d1f',
          confirmButtonColor: '#ff4d4f'
        });
      }
    });
  }
}
