import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ProductDto, UpdateProductRequest} from '../../../../DTO/PRODUCTS/ProductsDtos';
import {ProductService} from '../../../../SERVICE/product-service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../../../SERVICE/user-service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {ProductCategoryDto} from '../../../../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';
import {ProductCategoryService} from '../../../../SERVICE/product-category-service';
import {HttpResponse} from '@angular/common/http';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-products-modify-form',
  standalone: false,
  templateUrl: './admin-products-modify-form.html',
  styleUrl: './admin-products-modify-form.scss'
})
export class AdminProductsModifyForm {
  public productDtoAttributes: ProductDto = {uuid: "", categoryUUID:"",
    name: "", description: "",image: "", price:9, stock:9}
  form: FormGroup;
  showProductBoolean = false;

  ProductCategoryDtoList: ProductCategoryDto[] = [];

  constructor(private dialogRef: MatDialogRef<AdminProductsModifyForm>,private productCategoryService: ProductCategoryService, private router: Router, private snackBar : MatSnackBar, private userService: UserService, private fb: FormBuilder,@Inject(MAT_DIALOG_DATA) public data: { uuid: string },private productService: ProductService) {

    this.loadProductDto();
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

    this.userService.getUserInfo().subscribe({
      next: userDto => {
        this.form.patchValue({

          name: this.productDtoAttributes.name ,
          description: this.productDtoAttributes.description,
          price:this.productDtoAttributes.price,
          stock: this.productDtoAttributes.stock,
          image:this.productDtoAttributes.image,
          categoryUUID:this.productDtoAttributes.categoryUUID
        });
      }
    })
  }

  loadProductDto(){
    this.productService.getProduct(this.data.uuid).subscribe(
      {
        next: (productDtoResp: ProductDto)=>{
          this.productDtoAttributes = productDtoResp;
          this.showProductBoolean = true;

       },error: error => {
          console.log("Error")
            this.showProductBoolean = true;

        }
      }
    )
  }

  whenSubmitAndModifyProduct() {
    let productRequest: UpdateProductRequest = this.form.value
    this.productService.updateProduct(this.data.uuid,productRequest).subscribe(
      {
        next: (httResponse: HttpResponse<ProductDto>)=>{
            if(httResponse.status === 200){
              this.form.patchValue({
                name: httResponse.body?.name ,
                description: httResponse.body?.description,
                price:httResponse.body?.price,
                stock: httResponse.body?.stock,
                image:httResponse.body?.image,
                categoryUUID:httResponse.body?.categoryUUID
              });

              Swal.fire({
                icon: 'success',
                title: 'Producto actualizado',
                text: 'Los cambios se han guardado correctamente.',
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

            }else{
              console.log("Error actualizando el producto")
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
              setTimeout(()=>{
                this.dialogRef.close();}, 2000)
            }
        }
        ,error: (error) =>{
          this.snackBar.open('Error al modificar el producto', 'Cerrar', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }

      }

    )
  }
}
