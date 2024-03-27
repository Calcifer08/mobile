package ru.mirea.lozhnichenkoas.yandexdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener {

    private boolean task = true; // для задания
    private boolean isWork = false;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private /*final*/ Point ROUTE_START_LOCATION = new Point(55.670005, 37.479894);
    private /*final*/ Point ROUTE_END_LOCATION = new Point(55.794229, 37.700772);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2);
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private  int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);

        mapView = findViewById(R.id.mapview);

        mapView.getMap().setRotateGesturesEnabled(false);
        // Устанавливаем начальную точку и масштаб
        mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 10, 0, 0));
        // Ининциализируем объект для создания маршрута водителя
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();
//        submitRequest();

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
            submitRequest();
        }
    }

    private void submitRequest() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        // Кол-во альтернативных путей
        drivingOptions.setRoutesCount(4);

        // (задание) получаем координаты устройства
        if (task) {
            ROUTE_START_LOCATION = new Point(55.7935, 37.7013);
            ROUTE_END_LOCATION = new Point(55.822200, 37.641584);
        }
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        // Установка точек маршрута
        requestPoints.add(new RequestPoint(ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        // Отправка запроса на сервер
        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapKitFactory.getInstance().onStop();
        mapView.onStop();
    }

    // обработка полученных маршрутов
    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            // настроиваем цвета для каждого маршрута
            color = colors[i];
            // добавляем маршрут на карту
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }

        // (задание) для маркера
        if (task) {
            PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(ROUTE_END_LOCATION,
                    ImageProvider.fromResource(this, com.yandex.maps.mobile.R.drawable.search_layer_pin_selected_default));

            marker.setIconStyle(new IconStyle().setScale(1f));
            marker.addTapListener(new MapObjectTapListener() {
                @Override
                public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point
                        point) {
                    Toast.makeText(getApplication(),"Метро ВДНХ",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMesage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMesage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMesage = getString(R.string.network_error_message);
        }
        Toast.makeText(MainActivity.this, errorMesage, Toast.LENGTH_SHORT).show();
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
            submitRequest();
        }
    }
}