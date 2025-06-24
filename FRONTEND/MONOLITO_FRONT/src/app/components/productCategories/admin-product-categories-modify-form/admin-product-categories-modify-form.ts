import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { ProductCategoryService } from '../../../SERVICE/product-category-service';
import { ProductCategoryDto } from '../../../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';

@Component({
  selector: 'app-admin-product-categories-modify-form',
  standalone: false,
  templateUrl: './admin-product-categories-modify-form.html',
  styleUrl: './admin-product-categories-modify-form.scss'
})
export class AdminProductCategoriesModifyForm {

  form: FormGroup;
  showCategoryBoolean = false;
  category!: ProductCategoryDto;

  constructor(
    private dialogRef: MatDialogRef<AdminProductCategoriesModifyForm>,
    private categoryService: ProductCategoryService,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: { uuid: string }
  ) {
    this.form = this.fb.group({
      uuid: [''],
      name: ['', [Validators.required, Validators.maxLength(100)]]
    });
  }

  ngOnInit(): void {
    this.categoryService.getByUuid(this.data.uuid).subscribe({
      next: (category) => {
        this.category = category;
        this.form.patchValue(category);
        this.showCategoryBoolean = true;
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error al cargar',
          text: 'No se pudo obtener la categoría.',
          confirmButtonColor: '#007aff',
          confirmButtonText: 'Cerrar',
          customClass: {
            popup: 'custom-swal-popup',
            confirmButton: 'custom-swal-confirm-button',
            cancelButton: 'custom-swal-confirm-button'
          }
        });
        this.dialogRef.close();
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.categoryService.updateCategory(this.form.value).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Categoría actualizada',
          text: 'Los cambios se han guardado correctamente.',
          confirmButtonColor: '#007aff',
          confirmButtonText: 'Aceptar',
          customClass: {
            popup: 'custom-swal-popup',
            confirmButton: 'custom-swal-confirm-button',
            cancelButton: 'custom-swal-confirm-button'
          }
        }).then(() => {
          this.dialogRef.close();
          window.location.reload();
        });
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error al actualizar',
          text: 'Hubo un problema al guardar los cambios.',
          confirmButtonColor: '#007aff',
          confirmButtonText: 'Cerrar',
          customClass: {
            popup: 'custom-swal-popup',
            confirmButton: 'custom-swal-confirm-button',
            cancelButton: 'custom-swal-confirm-button'
          }
        });
      }
    });
  }
}
