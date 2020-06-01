import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {TabListComponent} from './tab-list/tab-list.component';
import {TabComponent} from './tab/tab.component';
import {TabContentComponent} from './tab-content/tab-content.component';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import { SafePipe } from './safe.pipe';
import { SidebarNavComponent } from './sidebar-nav/sidebar-nav.component';

@NgModule({
  declarations: [
    AppComponent,
    TabListComponent,
    TabComponent,
    TabContentComponent,
    SafePipe,
    SidebarNavComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
