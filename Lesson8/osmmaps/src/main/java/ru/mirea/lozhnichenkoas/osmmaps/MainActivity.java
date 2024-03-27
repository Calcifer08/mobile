package ru.mirea.lozhnichenkoas.osmmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {

    private boolean isWork = false;
    private static final int REQUEST_CODE_PERMISSION = 100;
    MyLocationNewOverlay locationNewOverlay;
    private MapView mapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        mapView = findViewById(R.id.mapView);

        int	coarsePermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int	finePermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int backgroundPermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if	(coarsePermissionStatus == PackageManager.PERMISSION_GRANTED &&
                finePermissionStatus ==	PackageManager.PERMISSION_GRANTED /*&&
                backgroundPermissionStatus == PackageManager.PERMISSION_GRANTED
                если включить, то ничего не отобразится
                только если вывести в отдельный if и вызывать метод отдельно для него
                работает только при отладке (попаду в меню разрешений),
                иначе ничего не происходит*/) {
            isWork = true;
        } else {
            //	Выполняется запрос к пользователю на получение необходимых разрешений
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION/*,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION*/}, REQUEST_CODE_PERMISSION);
        }

        if (isWork) {
            allFunctions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }

    // кнопки масштаба
    private void scale() {
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);
    }

    // перемещение камеры в определённую точку
    private void cameraControl() {
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);
    }

    //местоположение
    private void myLocate() {
        locationNewOverlay = new MyLocationNewOverlay(new
                GpsMyLocationProvider(getApplicationContext()),mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);
    }

    // компас
    private void compas() {
        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(),
                new InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
    }

    // шкала масштаба
    private void scaleMetrics() {
        final Context context = this.getApplicationContext();
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(displayMetrics.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);
    }

    //маркер и обработка нажатия
    private void setMarker(String nameMarker, GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(), "Маркер " + nameMarker, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(marker);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker.setTitle(nameMarker);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)	{
        //	производится проверка полученного результата от пользователя на запрос разрешения
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if	(requestCode == REQUEST_CODE_PERMISSION) {
            //	permission	granted
            isWork = grantResults.length > 1
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED);
        }

        if (isWork) {
            allFunctions();
        }
    }

    private void allFunctions() {
        scale();
        cameraControl();
        myLocate();
        compas();
        scaleMetrics();
        setMarker("Стромынка", new GeoPoint(55.794229, 37.700772));
        setMarker("ВДНХ", new GeoPoint(55.8222, 37.6416));
        setMarker("Парк Горького", new GeoPoint(55.7297, 37.6094));
    }
}