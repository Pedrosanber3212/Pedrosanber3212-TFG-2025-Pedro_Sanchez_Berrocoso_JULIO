import {Component, Inject} from '@angular/core';
import {CreateProductRequest, ProductDto, UpdateProductRequest} from '../../../../DTO/PRODUCTS/ProductsDtos';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProductCategoryDto} from '../../../../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ProductCategoryService} from '../../../../SERVICE/product-category-service';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserService} from '../../../../SERVICE/user-service';
import {ProductService} from '../../../../SERVICE/product-service';
import {HttpResponse} from '@angular/common/http';
import {FileStorageService} from '../../../../SERVICE/file-storage-service';
import {UserDto} from '../../../../DTO/USERS/UsersDto';
import {of, switchMap} from 'rxjs';
import {catchError} from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-products-create-form',
  standalone: false,
  templateUrl: './admin-products-create-form.html',
  styleUrl: './admin-products-create-form.scss'
})
export class AdminProductsCreateForm {
  public productDto: ProductDto = {uuid: "", categoryUUID:"",
    name: "", description: "",image: "", price:0, stock:0}
  form: FormGroup;

  ProductCategoryDtoList: ProductCategoryDto[] = [];
  productImage: File = new File([""], "empty.txt", { type: "text/plain" });
  updateProductRequest : UpdateProductRequest = {
    name: "",
    description: "",
    price: 0,
    stock: 0,
    categoryUUID: "",
    image: ""
  };


  constructor(private dialogRef: MatDialogRef<AdminProductsCreateForm>
              ,private productCategoryService: ProductCategoryService
              , private router: Router, private snackBar : MatSnackBar
              , private userService: UserService
              , private fb: FormBuilder
              ,private productService: ProductService
              ,private fileStorageService: FileStorageService) {

    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
      stock: ['', Validators.required],
      image: ['', Validators.required],
      categoryUUID: ['', Validators.required]

    });

    //llamada get all categorias
    this.productCategoryService.getAll()
      .subscribe({
        next: (productCategoryDtoList: ProductCategoryDto[]) => {
          console.log("productCategoryDtoList: " + productCategoryDtoList)
          this.ProductCategoryDtoList = productCategoryDtoList;
        }
        , error: error => console.log("ERROR PRODUCT: " + error)
      })


  }



  onSubmitAndCreateProduct(): void {
    if (this.productImage?.size > 0) {
      const productRequest: CreateProductRequest = { ...this.form.value };
      productRequest.image = ""

      this.productService.createProduct(productRequest).pipe(
        switchMap((httpResponse: HttpResponse<ProductDto>) => {
          const createdProduct = httpResponse.body!;
          this.updateProductRequest = createdProduct;
          console.log("1"+ httpResponse.body?.name)
          // return la llamada a uploadProductImage
          return this.fileStorageService.uploadProductImage(createdProduct.uuid, this.productImage!).pipe(
            switchMap((uploadResponseWithUrl: HttpResponse<string>) => {

              let updateProductRequest: UpdateProductRequest = {
                name: createdProduct.name,
                description: createdProduct.description,
                price: createdProduct.price,
                stock: createdProduct.stock,
                categoryUUID: createdProduct.categoryUUID,
                image: uploadResponseWithUrl.body!
              };
              console.log( "url "+uploadResponseWithUrl.body!)
              return this.productService.updateProduct(    `${httpResponse.body?.uuid}` , updateProductRequest);

            })
          );
        }),
        catchError(error => {
          this.snackBar.open('Error durante creación o subida', 'Cerrar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
          return of(null);
        })
      ).subscribe(() => {
          Swal.fire({
            icon: 'success',
            title: 'Producto creado',
            text: 'Se ha creado el producto correctamente.',
            confirmButtonColor: '#007aff',
            confirmButtonText: 'Aceptar',
            customClass: {
              popup: 'custom-swal-popup',
              confirmButton: 'custom-swal-confirm-button'
            }            
          }).then(() => {
            this.dialogRef.close();
            window.location.reload();
          });
        });
        
    } else {
      Swal.fire({
        icon: 'error',
        title: 'Error al actualizar',
        text: 'Hubo un problema al guardar los cambios.',
        confirmButtonColor: '#007aff',
        confirmButtonText: 'Cerrar',
        customClass: {
          popup: 'custom-swal-popup',
          confirmButton: 'custom-swal-confirm-button'
        } 
      });
    }
  }





  onSubmitAndCreateProduct2() {
    let productRequest: CreateProductRequest = this.form.value

    this.productService.createProduct(productRequest).subscribe(
      {

        next: (httResponse: HttpResponse<ProductDto>)=>{
          if(httResponse.status === 200){


            setTimeout(()=>{
              this.dialogRef.close();}, 2000)

            this.snackBar.open('Success al crear el producto', 'Cerrar', {
              duration: 3000,
              panelClass: ['snackbar-success']
            });

          }else{
            console.log("Error creando el producto")
            this.snackBar.open('Error al crear el producto', 'Cerrar', {
              duration: 3000,
              panelClass: ['snackbar-error']
            });

            setTimeout(()=>{
              this.dialogRef.close();}, 2000)
          }
        }
        ,error: (error) =>{
          this.snackBar.open('Error al crear el producto', 'Cerrar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }

      }

    )
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files?.length) {
      this.productImage= input?.files[0];
    }
  }
}
