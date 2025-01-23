import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import * as mapboxgl from 'mapbox-gl';

@Component({
  selector: 'app-our-location',
  standalone: true,
  templateUrl: './our-location.component.html',
  styleUrl: './our-location.component.css'
})
export class OurLocationComponent {
  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  onClose() {
    this.close.emit(); // Emitowanie zdarzenia
  }

  map: mapboxgl.Map | undefined;
  style = 'mapbox://styles/mapbox/streets-v11';
  lat: number = 52.253207569774126;
  lng: number = 20.895554141357806;

  ngOnInit() {
    this.map = new mapboxgl.Map({
      accessToken: 'pk.eyJ1IjoiaGV2eGlxdSIsImEiOiJjbTV3ZnBhcmowMmI4Mm1zZzdyZjQ2MHgzIn0.qsC-QTv_TXsQAMyj_13IKQ',
      container: 'map',
      style: this.style,
      zoom: 13,
      center: [this.lng, this.lat],
    });

    new mapboxgl.Marker()
      .setLngLat([this.lng, this.lat]) // Ustawienie współrzędnych
      .addTo(this.map); // Dodanie do mapy
  }
}
