import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MyPlugin} from '../app.component';

@Component({
  selector: 'app-sidebar-nav',
  templateUrl: './sidebar-nav.component.html',
  styleUrls: ['./sidebar-nav.component.css']
})
export class SidebarNavComponent implements OnInit {

  @Input()
  items: MyPlugin[];

  @Output()
  itemClicked = new EventEmitter<MyPlugin>();

  constructor() { }

  sendEvent(val: MyPlugin){
    this.itemClicked.emit(val);
  }

  ngOnInit(): void {
  }

}
