package com.example.googlev2map;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.RadioButton;
import android.location.LocationManager;



public class MainActivity extends FragmentActivity  {
	private static final String URL_CONSTANT = "http://maps.googleapis.com/maps/api/geocode/json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
		
	    GoogleMap googleMap;
        googleMap = ((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map))).getMap();
       
        RadioButton r1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton r2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton r3 = (RadioButton) findViewById(R.id.radioButton3);
        
        double lon=0;
        double lat = 0;
        EditText tempText;
        if( r1.isChecked()) { // when longitude and lattitude are provided
        	tempText = (EditText) findViewById(R.id.editText2);
        	lon = Double.parseDouble(tempText.getEditableText().toString());
        	tempText = (EditText) findViewById(R.id.editText3);
        	lat = Double.parseDouble(tempText.getEditableText().toString());
        }
        else if( r2.isChecked()) { // when addresss is providede
        	try {
	        	tempText = (EditText) findViewById(R.id.editText4);
	        	URL url = new URL(URL_CONSTANT + "?latlng="+ URLEncoder.encode(tempText.getEditableText().toString(),
	        			"UTF-8") + "&sensor=false");
	          // Open the Connection
	        	URLConnection conn = url.openConnection();
	        	InputStream in = conn.getInputStream() ;
	        	ObjectMapper mapper = new ObjectMapper();
	        	LocationResponse response = (LocationResponse)mapper.readValue(in,LocationResponse.class);
	        	in.close();
	        	if(response.getStatus().equals("OK")) {
	        	   for(Result result : response.getResults()) {
	        		   lat=Double.parseDouble(result.getGeometry().getLocation().getLat());
	        		   lon=Double.parseDouble(result.getGeometry().getLocation().getLng());
	        	   }
	        	}
        	}
        	catch( Exception e) {
        		System.out.println("Error:"+e.toString());
        		e.printStackTrace();
        	}
        }
       else if( r3.isChecked()) { // for current location
    	   LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	   android.location.Location currentLocation = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
		   lat=currentLocation.getLatitude();
		   lon=currentLocation.getLongitude();
      }
       
        
//        LatLng latLng = new LatLng(-33.796923, 150.922433);
        LatLng latLng = new LatLng(lon, lat);
        
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Spot")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     
        
        //  googleMap.getUiSettings().setCompassEnabled(true);
      //  googleMap.getUiSettings().setZoomControlsEnabled(true);
       // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
		
	}


}
