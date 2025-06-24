import {Component, OnInit} from '@angular/core';
import {NavBarService} from '../../../../SERVICE/nav-bar-service';
import {UserService} from '../../../../SERVICE/user-service';
import {ProductService} from '../../../../SERVICE/product-service';
import {ProductCategoryService} from '../../../../SERVICE/product-category-service';
import {ShoppingCartService} from '../../../../SERVICE/shopping-cart-service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ProductCategoryDto} from '../../../../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';
import {ProductDto} from '../../../../DTO/PRODUCTS/ProductsDtos';
import {ShoppingCartDto} from '../../../../DTO/SHOPPING-CART/ShoppingCartDtos';
import {HttpResponse} from '@angular/common/http';
import {ShowMyShoppingCartPage} from '../../../ShoppingCart/show-my-shopping-cart-page/show-my-shopping-cart-page';
import {MatDialog} from '@angular/material/dialog';
import {AdminProductsModifyForm} from '../admin-products-modify-form/admin-products-modify-form';
import {AdminProductsCreateForm} from '../admin-products-create-form/admin-products-create-form';
import {Router} from '@angular/router';

@Component({
  selector: 'app-admin-show-all-product-page',
  standalone: false,
  templateUrl: './admin-show-all-product-page.html',
  styleUrl: './admin-show-all-product-page.scss'
})
export class AdminShowAllProductPage implements OnInit {
  showFiller: boolean = false;
  productCategoryList: ProductCategoryDto[] = [];
  productList: ProductDto[] = [];
  public userService: UserService;

  pageSelected: number = 0
  sizeSelected: number = 8
  sortSelected: string = "name,asc"
  nameSelected?: string
  minPriceSelected?: number = 0;
  maxPriceSelected?: number = 1000;
  categoryUUID?: string

  visiblePages: number[] = [];
  hasNextPage: boolean = false;

  constructor(private router: Router,  private dialog: MatDialog, private navBarService: NavBarService, private userService2: UserService, private productService: ProductService, private productCategoryService: ProductCategoryService, private shoppingCartService: ShoppingCartService, private snackBar: MatSnackBar) {


    this.userService = userService2;
    //llamada INICIAL a productos con un get PARA CONSEGUIR Y MOSTRA LOS 8
    this.productService.getFilteredProducts(this.pageSelected, this.sizeSelected, this.sortSelected, this.nameSelected, this.minPriceSelected, this.maxPriceSelected, this.categoryUUID)
      .subscribe({
        next: (productDtoList: ProductDto[]) => {
          console.log("productList: " + productDtoList)
          this.productList = productDtoList
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })

    //llamada get all categorias
    this.productCategoryService.getAll()
      .subscribe({
        next: (productCategoryDtoList: ProductCategoryDto[]) => {
          console.log("productCategoryDtoList: " + productCategoryDtoList)
          this.productCategoryList = productCategoryDtoList;
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })


    //subscripcion a nav-bar-service-observable
    this.navBarService.getSearchBarObservable().subscribe(
      {
        next: (searchBarValue: string ) => {
          this.nameSelected = searchBarValue
          this.applyFilters();
        }
        , error: error => console.log(error)
      }
    )

  }
  ngOnInit(): void {
    this.fetchProducts();
  }

  moveForwardButton() {
    this.pageSelected++; //aumenta 1

    this.productService.getFilteredProducts(this.pageSelected, this.sizeSelected, this.sortSelected, this.nameSelected, this.minPriceSelected, this.maxPriceSelected, this.categoryUUID)
      .subscribe({
        next: (productDtoList: ProductDto[]) => {
          console.log("productList: " + productDtoList)
          this.productList = productDtoList
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })

  }


  moveBackButton() {
    if (this.pageSelected > 0) {
      this.pageSelected--; //disminuye en 1
    }
    ; // disminuye en 1 si es >0

    this.productService.getFilteredProducts(this.pageSelected, this.sizeSelected, this.sortSelected, this.nameSelected, this.minPriceSelected, this.maxPriceSelected)
      .subscribe({
        next: (productDtoList: ProductDto[]) => {
          console.log("productList: " + productDtoList)
          this.productList = productDtoList
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })
  }


  applyFilters() {
    this.pageSelected = 0;
    console.log("filtros: " + [this.pageSelected, this.sizeSelected, this.sortSelected, this.nameSelected, this.minPriceSelected, this.maxPriceSelected])

    this.productService.getFilteredProducts(this.pageSelected, this.sizeSelected, this.sortSelected, this.nameSelected, this.minPriceSelected, this.maxPriceSelected, this.categoryUUID)
      .subscribe({
        next: (productDtoList: ProductDto[]) => {
          console.log(" apply filters -  productList: " + productDtoList)
          this.productList = productDtoList
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })

    //llamada get all categorias
    this.productCategoryService.getAll()
      .subscribe({
        next: (productCategoryDtoList: ProductCategoryDto[]) => {
          console.log("apply filters - productCategoryDtoList: " + productCategoryDtoList)
          this.productCategoryList = productCategoryDtoList;
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })
  }



  editProduct(uuid: string) {

    this.dialog.open(AdminProductsModifyForm, {
      width: '95vw',
      height: '60vh',
      position: {
        top: '10vh',
        left: '35vw'
      },
      panelClass: 'custom-modal-class'
      ,  data: { uuid }
    });
  }

  createNewProduct() {
    this.dialog.open(AdminProductsCreateForm, {
      width: '90vw',
      height: '80vh',
      position: {
        top: '10vh',
        left: '35vw'
      },
      panelClass: 'custom-modal-class'

    });
  }

  deleteProduct(uuid: string) {

    this.productService.deleteProduct(uuid).subscribe(
      {

        next: (httResponse: HttpResponse<ProductDto>) => {
          if (httResponse.status === 200) {


            this.snackBar.open('Success al eliminar el producto', 'Cerrar', {
              duration: 3000,
              panelClass: ['snackbar-success']
            });

          } else {
            console.log("Error actualizando el producto")
            this.snackBar.open('Error al eliminar el producto', 'Cerrar', {
              duration: 3000,
              panelClass: ['snackbar-error']
            });


          }
        }
        , error: (error) => {
          this.snackBar.open('Error al eliminar el producto', 'Cerrar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }

      }
    );
    setTimeout(()=>window.location.reload(),300)


  }

  
  private updatePagination(totalItems: number) {
    const pageSize = this.sizeSelected;
    const maxVisiblePages = 5;
    const totalPages = Math.ceil(totalItems / pageSize);
    const half = Math.floor(maxVisiblePages / 2);
  
    let start = Math.max(this.pageSelected - half, 0);
    let end = Math.min(start + maxVisiblePages, totalPages);
  
    if (end - start < maxVisiblePages) {
      start = Math.max(end - maxVisiblePages, 0);
    }
  
    this.visiblePages = Array.from({ length: end - start }, (_, i) => start + i);
    this.hasNextPage = this.pageSelected + 1 < totalPages;
  }
  
  goToPage(page: number) {
    this.pageSelected = page;
    this.fetchProducts();
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
      next: (productDtoList: ProductDto[]) => {
        this.productList = productDtoList;
        this.checkNextPage(); 
      }
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
      next: (nextPageProducts: ProductDto[]) => {
        this.hasNextPage = nextPageProducts.length > 0;
        this.updatePagination(this.pageSelected * this.sizeSelected + this.productList.length + (this.hasNextPage ? 1 : 0));
      }
    });
  }
  

}
