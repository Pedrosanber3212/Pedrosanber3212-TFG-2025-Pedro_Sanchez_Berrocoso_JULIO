import { Component } from '@angular/core';
import {NavBarComponent} from './components/nav-bar-component/nav-bar-component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.scss',

})
export class App   {
  protected title = 'FRONT-TFG';

  constructor(
    public router : Router
  ){}
}


