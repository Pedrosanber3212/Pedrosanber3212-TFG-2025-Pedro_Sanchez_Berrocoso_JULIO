import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {CommonModule} from '@angular/common';

import { AppRoutingModule } from './app-routing-module';
import {App} from './app';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatGridListModule } from '@angular/material/grid-list';
import {MatIconModule} from '@angular/material/icon';
import {MatSnackBarContainer, MatSnackBarModule} from '@angular/material/snack-bar';
import { NavBarComponent } from './components/nav-bar-component/nav-bar-component';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatBadgeModule} from '@angular/material/badge';
import { UserModalComponent } from './components/users/user-modal-component/user-modal-component';
import { HttpClientModule } from '@angular/common/http';
import {MatDialogModule} from '@angular/material/dialog';
import { AdminProductsCreateForm } from './components/products/admin/admin-products-create-form/admin-products-create-form';
import { AdminProductsModifyForm } from './components/products/admin/admin-products-modify-form/admin-products-modify-form';
import { AdminShowAllProductPage } from './components/products/admin/admin-show-all-product-page/admin-show-all-product-page';
import { CustomerShowAllProductsPage } from './components/products/customer-show-all-products-page/customer-show-all-products-page';
import { UserRegisterForm } from './components/users/user-register-form/user-register-form';
import { UserModifyForm } from './components/users/user-modify-form/user-modify-form';
import { ShowAllMyOrdersPage } from './components/orders/show-all-my-orders-page/show-all-my-orders-page';
import { AdminShowAllOrdersPage } from './components/orders/admin/admin-show-all-orders-page/admin-show-all-orders-page';
import { ShowMyShoppingCartPage } from './components/ShoppingCart/show-my-shopping-cart-page/show-my-shopping-cart-page';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserLoginForm} from './components/users/user-login-form/user-login-form/user-login-form';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatOption, provideNativeDateAdapter} from '@angular/material/core';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatDrawer, MatDrawerContainer} from '@angular/material/sidenav';
import {MatSlider, MatSliderModule} from '@angular/material/slider';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardImage,
  MatCardSubtitle,
  MatCardTitle
} from '@angular/material/card';
import {MatSelect} from '@angular/material/select';
import { AdminProductCategoriesCreateForm } from './components/productCategories/admin-product-categories-create-form/admin-product-categories-create-form';
import { AdminShowAllProductCategoriesPage } from './components/productCategories/admin-show-all-product-categories-page/admin-show-all-product-categories-page';
import {
  AdminProductCategoriesModifyForm
} from './components/productCategories/admin-product-categories-modify-form/admin-product-categories-modify-form';

@NgModule({
  declarations: [
    AdminProductCategoriesModifyForm,
    App,
    NavBarComponent,
    UserModalComponent,
    AdminProductsCreateForm,
    AdminProductsModifyForm,
    AdminShowAllProductPage,
    CustomerShowAllProductsPage,
    UserLoginForm,
    UserRegisterForm,
    UserModifyForm,
    ShowAllMyOrdersPage,
    AdminShowAllOrdersPage,
    ShowMyShoppingCartPage,
    AdminProductCategoriesCreateForm,
    AdminShowAllProductCategoriesPage,
  ],
  imports: [
    MatSliderModule,
    HttpClientModule,
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    MatToolbarModule,
    MatGridListModule,
    MatIconModule,
    MatSnackBarContainer,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatBadgeModule,
    MatDialogModule,
    ReactiveFormsModule,
    FormsModule,
    MatSnackBarModule,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepicker,
    MatProgressSpinner,
    MatDrawer,
    MatDrawerContainer,
    MatSlider,
    MatRadioButton,
    MatCard,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    MatCardActions,
    MatCardImage,
    MatRadioGroup,
    MatSelect,
    MatOption,
    MatFormFieldModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideNativeDateAdapter()
  ],
  bootstrap: [App ]
})
export class AppModule { }
