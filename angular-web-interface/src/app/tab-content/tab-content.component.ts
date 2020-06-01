import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-tab-content',
  templateUrl: './tab-content.component.html',
  styleUrls: ['./tab-content.component.css']
})
export class TabContentComponent implements OnInit {

  @Input()
  url: string;

  get content(){
    return `<iframe src='${this.url}'></iframe>`;
  }

  constructor() { }

  ngOnInit(): void {
  }

}
