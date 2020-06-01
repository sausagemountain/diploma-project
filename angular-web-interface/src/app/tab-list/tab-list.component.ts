import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {MyPlugin} from '../app.component';

@Component({
  selector: 'app-tab-list',
  templateUrl: './tab-list.component.html',
  styleUrls: ['./tab-list.component.css']
})
export class TabListComponent implements OnInit {

  @Input()
  tabList: MyPlugin[];

  @Input()
  selectedTab: MyPlugin;

  @Output()
  tabSelected = new EventEmitter<MyPlugin>();

  tabSelect(tab: MyPlugin){
    this.tabSelected.emit(tab);
  }

  constructor() { }

  ngOnInit(): void {
  }

}
