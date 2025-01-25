import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import * as mapboxgl from 'mapbox-gl';
import { environment } from '../../environment/environment';

@Component({
  selector: 'app-our-location',
  standalone: true,
  templateUrl: './our-location.component.html',
  styleUrl: './our-location.component.css'
})
export class OurLocationComponent {
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }

  map: mapboxgl.Map | undefined;
  style = 'mapbox://styles/mapbox/streets-v11';
  lat: number = 52.253207569774126;
  lng: number = 20.895554141357806;

  ngOnInit() {
    this.map = new mapboxgl.Map({
      accessToken:environment.maps,
      container: 'map',
      style: this.style,
      zoom: 13,
      center: [this.lng, this.lat],
    });

    new mapboxgl.Marker()
      .setLngLat([this.lng, this.lat])
      .addTo(this.map);
  }
}
