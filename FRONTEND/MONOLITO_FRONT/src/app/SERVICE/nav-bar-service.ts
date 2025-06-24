import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ShoppingCartService } from './shopping-cart-service';
import { ShoppingCartDto } from '../DTO/SHOPPING-CART/ShoppingCartDtos';

@Injectable({
  providedIn: 'root'
})
export class NavBarService {

  private navBarSearchBarSubject : BehaviorSubject<string > = new BehaviorSubject<string >("")
  private navBarSearchBarAsObservable: Observable<string > = this.navBarSearchBarSubject.asObservable();
  constructor(private shoppingCartService: ShoppingCartService) {

  }

getSearchBarObservable(){
  return this.navBarSearchBarAsObservable;
}

next(searchBarValue: string){
    console.log("next: value:" + searchBarValue)
   this.navBarSearchBarSubject.next(searchBarValue)
}


}


