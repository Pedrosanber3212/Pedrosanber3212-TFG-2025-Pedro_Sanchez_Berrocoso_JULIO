import { Component, OnInit } from '@angular/core';

import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpResponse } from '@angular/common/http';
import { ProductCategoryService } from '../../../SERVICE/product-category-service';
import { ProductCategoryDto } from '../../../DTO/PRODUCT-CATEGORIES/ProductCategoriesDtos';
import Swal from 'sweetalert2';
import { AdminProductCategoriesCreateForm } from '../admin-product-categories-create-form/admin-product-categories-create-form';
import { AdminProductCategoriesModifyForm } from '../admin-product-categories-modify-form/admin-product-categories-modify-form';

@Component({
  selector: 'app-admin-show-all-product-categories-page',
  standalone: false,
  templateUrl: './admin-show-all-product-categories-page.html',
  styleUrls: ['./admin-show-all-product-categories-page.scss']
})
export class AdminShowAllProductCategoriesPage implements OnInit {
  productCategoryList: ProductCategoryDto[] = [];

  pageSelected = 0;
  sizeSelected = 5;
  visiblePages: number[] = [];
  hasNextPage = false;

  constructor(
    private productCategoryService: ProductCategoryService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.fetchCategories();
  }

  fetchCategories(): void {
    this.productCategoryService.getAll().subscribe({
      next: (categories) => {
        const start = this.pageSelected * this.sizeSelected;
        const end = start + this.sizeSelected;
        this.productCategoryList = categories.slice(start, end);
        this.hasNextPage = categories.length > end;
        this.updatePagination(categories.length);
      }
    });
  }

  createNewCategory(): void {
    const dialogRef = this.dialog.open(AdminProductCategoriesCreateForm, {
      width: '70vw',
      height: '60vh',
      panelClass: 'custom-modal-class'
    });

    dialogRef.afterClosed().subscribe(() => this.fetchCategories());
  }

  editCategory(uuid: string): void {
    const dialogRef = this.dialog.open(AdminProductCategoriesModifyForm, {
      width: '70vw',
      height: '60vh',
      panelClass: 'custom-modal-class',
      data: { uuid }
    });

    dialogRef.afterClosed().subscribe(() => this.fetchCategories());
  }

  deleteCategory(uuid: string): void {
    Swal.fire({
      icon: 'warning',
      title: '¿Eliminar categoría?',
      text: 'Esta acción no se puede deshacer.',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#aaa',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      customClass: {
        popup: 'custom-swal-popup',
        confirmButton: 'custom-swal-confirm-button',
        cancelButton: 'custom-swal-confirm-button'
      }
    }).then(result => {
      if (result.isConfirmed) {
        this.productCategoryService.deleteCategory(uuid).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Categoría eliminada',
              text: 'Se ha eliminado correctamente.',
              confirmButtonColor: '#007aff',
              confirmButtonText: 'Aceptar',
              customClass: {
                popup: 'custom-swal-popup',
                confirmButton: 'custom-swal-confirm-button'
              }
            }).then(() => this.fetchCategories());
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo eliminar la categoría.',
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
    });
  }

  moveForwardButton() {
    if (!this.hasNextPage) return;
    this.pageSelected++;
    this.fetchCategories();
  }

  moveBackButton() {
    if (this.pageSelected > 0) {
      this.pageSelected--;
      this.fetchCategories();
    }
  }

  goToPage(page: number) {
    this.pageSelected = page;
    this.fetchCategories();
  }

  private updatePagination(totalItems: number) {
    const totalPages = Math.ceil(totalItems / this.sizeSelected);
    const maxVisiblePages = 5;
    const half = Math.floor(maxVisiblePages / 2);

    let start = Math.max(this.pageSelected - half, 0);
    let end = Math.min(start + maxVisiblePages, totalPages);

    if (end - start < maxVisiblePages) {
      start = Math.max(end - maxVisiblePages, 0);
    }

    this.visiblePages = Array.from({ length: end - start }, (_, i) => start + i);
    this.hasNextPage = this.pageSelected + 1 < totalPages;
  }
}
