package ru.phrogs.moretechhackathon.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.phrogs.moretechhackathon.domain.usecase.GetAllBankCoordinatesUseCase
import ru.phrogs.moretechhackathon.presentation.uistate.map.MapState

class MapViewModel(
    private val getAllBankCoordinatesUseCase: GetAllBankCoordinatesUseCase
) : ViewModel() {

    val mapState: State<MapState>
        get() = _mapState
    private val _mapState = mutableStateOf<MapState>(MapState.Loading)

    val currentLocationState: State<Point?>
        get() = _currentLocationState
    private val _currentLocationState = mutableStateOf<Point?>(null)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            lastLocationResult = p0
            updateLocation()
        }
    }
    private val locationRequest =
        LocationRequest.Builder(5000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
    private lateinit var lastLocationResult: LocationResult
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    var lastSavedCameraPosition = CameraPosition(
        Point(55.751225, 37.629540),
        10.0f,
        150.0f,
        0f
    )
    var forceRedraw = mutableStateOf(false)

    val routeState: State<Polyline?>
        get() = _routeState
    private val _routeState = mutableStateOf<Polyline?>(null)
    val routeFinishTimeState: State<Double?>
        get() = _routeFinishTimeState
    private val _routeFinishTimeState = mutableStateOf<Double?>(null)
    private val drivingRouteListener = object : DrivingRouteListener {
        override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
            val route = p0.first()
            _routeState.value = route.geometry
            route.routePosition.timeToFinish()
        }

        override fun onDrivingRoutesError(p0: Error) {}
    }

    init {
        loadBankCoordinates()
    }

    private fun loadBankCoordinates() {
        _mapState.value = MapState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bankCoordinates = getAllBankCoordinatesUseCase()
                withContext(Dispatchers.Main) {
                    _mapState.value = MapState.Content(bankCoordinates)
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    _mapState.value = MapState.Error
                }
            }
        }
    }

    private fun updateLocation() {
        for (location in lastLocationResult.locations) {
            _currentLocationState.value = Point(location.latitude, location.longitude)
        }
    }

    fun forceUpdateLocation() {
        updateLocation()
    }

    fun startLocationTracking(context: Context) {
        if (!this::fusedLocationClient.isInitialized) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    fun stopLocationTracking() {
        if (!this::fusedLocationClient.isInitialized) return
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun buildRoute(start: Point, destination: Point) {
        val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        val drivingOptions = DrivingOptions().apply {
            routesCount = 1
            departureTime = System.currentTimeMillis()
        }
        val vehicleOptions = VehicleOptions()
        val points = buildList {
            add(RequestPoint(start, RequestPointType.WAYPOINT, null, null))
            add(RequestPoint(destination, RequestPointType.WAYPOINT, null, null))
        }
        drivingRouter.requestRoutes(
            points,
            drivingOptions,
            vehicleOptions,
            drivingRouteListener
        )
    }
}