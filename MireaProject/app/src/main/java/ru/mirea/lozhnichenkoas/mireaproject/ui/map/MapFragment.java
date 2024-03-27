package ru.mirea.lozhnichenkoas.mireaproject.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

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

import ru.mirea.lozhnichenkoas.mireaproject.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private boolean isWork = false;
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
        isWork = permissions.containsValue(true);

        if (isWork) {
            allFunctions();
        }
    });
    MyLocationNewOverlay locationNewOverlay;
    private MapView mapView = null;
    private FragmentMapBinding fragmentMapBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false);
        View root = fragmentMapBinding.getRoot();

        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));

        mapView = fragmentMapBinding.mapView;

        // поблема - остуствие вериткальной прокрутки
        // решение - запрещаем родителю перехватывать движение
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        requestPermissionLauncher.launch(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                /*android.Manifest.permission.ACCESS_BACKGROUND_LOCATION*/});

        return root;
    }

    private void allFunctions() {
        scale();
        cameraControl();
        myLocate();
        compas();
        scaleMetrics();
        setMarker("РТУ МИРЭА - корпус С-20\n" +
                "ул. Стромынка, 20, Москва", new GeoPoint(55.794229, 37.700772));
        setMarker("Парк ВДНХ\n" +
                "просп. Мира, 119В, Москва", new GeoPoint(55.8262, 37.6377));
        setMarker("Парк Горького\n" +
                "улица Крымский Вал, 9", new GeoPoint(55.7319, 37.6040));
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
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
                GpsMyLocationProvider(requireContext()),mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);
    }

    // компас
    private void compas() {
        CompassOverlay compassOverlay = new CompassOverlay(requireContext(),
                new InternalCompassOrientationProvider(requireContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
    }

    // шкала масштаба
    private void scaleMetrics() {
        final Context context = this.requireContext();
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
                Toast.makeText(requireContext(), nameMarker, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(marker);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker.setTitle(nameMarker);
    }
}