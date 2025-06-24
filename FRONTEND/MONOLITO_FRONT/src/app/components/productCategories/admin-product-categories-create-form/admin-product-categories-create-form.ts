import {Component, Optional} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductCategoryService } from '../../../SERVICE/product-category-service';
import { MatDialogRef } from '@angular/material/dialog';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-product-categories-create-form',
  standalone: false,
  templateUrl: './admin-product-categories-create-form.html',
  styleUrl: './admin-product-categories-create-form.scss'
})
export class AdminProductCategoriesCreateForm {

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private categoryService: ProductCategoryService,
    @Optional()  private dialogRef: MatDialogRef<AdminProductCategoriesCreateForm>
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.categoryService.createCategory(this.form.value).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Categoría creada',
          text: 'Se ha creado la categoría correctamente.',
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
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error al crear',
          text: 'No se pudo crear la categoría.',
          confirmButtonColor: '#007aff',
          confirmButtonText: 'Cerrar',
          customClass: {
            popup: 'custom-swal-popup',
            confirmButton: 'custom-swal-confirm-button'
          }
        });
      }
    });
  }
}
