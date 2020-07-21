import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-web-interface';

  selectedTab: MyPlugin;

  allPlugins = this.fetchPlugins;

  lastPlugins = this.sourceLastPlugins;

  fetchUrl = 'localhost:8080';

  get fetchPlugins(){
    return [
      new MyPlugin('Inspector', 'https://linghub.ru/inspector_realec/'),
      new MyPlugin('ADWISER', 'https://linghub.ru/adwiser/'),
    ];
  }

  // selectTab(tab: MyPlugin){
  //   this.__updateSelected__(tab);
  //   this.__refreshTabs__();
  //   this.__selectTab__(tab);
  // }
  //
  // selectTabNoUpdate(tab: MyPlugin){
  //   this.__updateSelected__(tab);
  //   this.__selectTab__(tab);
  // }

  get sourceLastPlugins(){
    let last = [...this.allPlugins].sort((a, b) => {
        return a.lastAccessTime > b.lastAccessTime ? -1 : a.lastAccessTime < b.lastAccessTime ? 1 : 0;
      });
    last = last.filter(((value, index) => index < 4));
    return last;
  }

  selectTab(tab: MyPlugin){
    tab.lastAccessTime = new Date();
    this.lastPlugins = this.sourceLastPlugins;
    this.selectedTab = tab;
  }

  selectTabNoUpdate(tab: MyPlugin){
    tab.lastAccessTime = new Date();
    this.selectedTab = tab;
  }

  __updateSelected__(tab: MyPlugin){
    tab.lastAccessTime = new Date();
  }

  __refreshTabs__(){
    this.lastPlugins = this.sourceLastPlugins;
  }

  __selectTab__(tab: MyPlugin){
    this.selectedTab = tab;
  }
}

export class MyPlugin {
  name: string;
  url: string;
  lastAccessTime: Date;
  constructor(name: string, url: string, date?: Date) {
    this.name = name;
    this.url = url;
    if (date) {
      this.lastAccessTime = date;
    }
    else {
      this.lastAccessTime = new Date();
    }
  }
}
