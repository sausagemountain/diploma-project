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

  get fetchPlugins(){
    return [
      new MyPlugin('openstreetmap', 'https://www.openstreetmap.org/export/embed.html?bbox=56.1%2C58%2C56.45%2C57.99'),
      new MyPlugin('example.net', 'https://www.example.net'),
      new MyPlugin('disqus', 'https://disqus.com/'),
      new MyPlugin('example.com', 'https://www.example.com'),
      new MyPlugin('example0', 'https://www.example.com'),
      new MyPlugin('example1', 'https://www.example.com'),
      new MyPlugin('example2', 'https://www.example.com')
    ];
  }

  get sourceLastPlugins(){
    let last = [...this.allPlugins].sort((a, b) => {
        return a.lastAccessTime > b.lastAccessTime ? -1 : a.lastAccessTime < b.lastAccessTime ? 1 : 0;
      });
    last = last.filter(((value, index) => index < 4));
    return last;
  }

  selectTab(tab: MyPlugin){
    this.__updateSelected__(tab);
    this.__refreshTabs__();
    this.__selectTab__(tab);
  }

  selectTabNoUpdate(tab: MyPlugin){
    this.__updateSelected__(tab);
    this.__selectTab__(tab);
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
