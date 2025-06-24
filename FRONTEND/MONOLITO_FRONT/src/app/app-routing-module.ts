import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  AdminProductsCreateForm
} from './components/products/admin/admin-products-create-form/admin-products-create-form';
import {
  AdminProductsModifyForm
} from './components/products/admin/admin-products-modify-form/admin-products-modify-form';
import {
  AdminShowAllProductPage
} from './components/products/admin/admin-show-all-product-page/admin-show-all-product-page';
import {
  CustomerShowAllProductsPage
} from './components/products/customer-show-all-products-page/customer-show-all-products-page';
import {UserRegisterForm} from './components/users/user-register-form/user-register-form';
import {UserModifyForm} from './components/users/user-modify-form/user-modify-form';
import {ShowAllMyOrdersPage} from './components/orders/show-all-my-orders-page/show-all-my-orders-page';
import {AdminShowAllOrdersPage} from './components/orders/admin/admin-show-all-orders-page/admin-show-all-orders-page';
import {ShowMyShoppingCartPage} from './components/ShoppingCart/show-my-shopping-cart-page/show-my-shopping-cart-page';
import {UserLoginForm} from './components/users/user-login-form/user-login-form/user-login-form';
import {
  AdminProductCategoriesCreateForm
} from './components/productCategories/admin-product-categories-create-form/admin-product-categories-create-form';
import {
  AdminShowAllProductCategoriesPage
} from './components/productCategories/admin-show-all-product-categories-page/admin-show-all-product-categories-page';
import {AdminRegisterAdminForm} from './components/users/admin-register-admin-form/admin-register-admin-form';

const routes: Routes = [
  { path: '', redirectTo: 'customer_show_all_products_page', pathMatch: 'full' },
  { path: 'customer_show_all_products_page', component: CustomerShowAllProductsPage},
  { path: 'admin/products_create_form', component: AdminProductsCreateForm },
  { path: 'admin/products_modify_form', component: AdminProductsModifyForm },
  { path: 'admin/show_all_products_page', component: AdminShowAllProductPage },


  { path: 'user_login_form', component: UserLoginForm },
  { path: 'user_register_form', component: UserRegisterForm },
  { path: 'user_modify_form', component: UserModifyForm },


  { path: 'show_all_my_orders_page', component: ShowAllMyOrdersPage },
  { path: 'admin/show_all_orders_page', component: AdminShowAllOrdersPage },

  { path: 'show_my_shoppingCart_page', component: ShowMyShoppingCartPage },

  { path: 'admin/create_productCategories', component: AdminProductCategoriesCreateForm },
  { path: 'admin/show_all_productCategories', component: AdminShowAllProductCategoriesPage },
  { path: 'admin/register_new_admin', component: AdminRegisterAdminForm },


];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
