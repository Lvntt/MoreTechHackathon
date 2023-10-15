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
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.phrogs.moretechhackathon.domain.usecase.GetAllBankCoordinatesUseCase
import ru.phrogs.moretechhackathon.domain.usecase.GetBankInfoUseCase
import ru.phrogs.moretechhackathon.presentation.uistate.map.BankInfoState
import ru.phrogs.moretechhackathon.presentation.uistate.map.MapState
import ru.phrogs.moretechhackathon.presentation.uistate.map.RouteInfo
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class MapViewModel(
    private val getAllBankCoordinatesUseCase: GetAllBankCoordinatesUseCase,
    private val getBankInfoUseCase: GetBankInfoUseCase
) : ViewModel() {

    val mapState: State<MapState>
        get() = _mapState
    private val _mapState = mutableStateOf<MapState>(MapState.Loading)

    val bankInfoState: State<BankInfoState>
        get() = _bankInfoState
    private val _bankInfoState = mutableStateOf<BankInfoState>(BankInfoState.Loading)

    val currentLocationState: State<Point?>
        get() = _currentLocationState
    private val _currentLocationState = mutableStateOf<Point?>(null)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            for (location in p0.locations) {
                _currentLocationState.value = Point(location.latitude, location.longitude)
            }
        }
    }
    private val locationRequest =
        LocationRequest.Builder(5000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    val startCameraPosition = CameraPosition(
        Point(55.751225, 37.629540), 10.0f, 150.0f, 0f
    )

    val routeState: State<RouteInfo?>
        get() = _routeState
    private val _routeState = mutableStateOf<RouteInfo?>(null)
    private val drivingRouteListener = object : DrivingRouteListener {
        override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
            val route = p0.first()
            _routeState.value = RouteInfo(
                route.geometry, routeAddress, (route.routePosition.timeToFinish() / 60).toInt()
            )
        }

        override fun onDrivingRoutesError(p0: Error) {}
    }
    private var routeAddress = ""

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

    fun loadBankData(bankId: Int) {
        if (bankInfoState.value is BankInfoState.Content && (bankInfoState.value as BankInfoState.Content).bankId == bankId) {
            return
        }
        _bankInfoState.value = BankInfoState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bankInfo = getBankInfoUseCase(bankId)
                var distance = 0.0
                _currentLocationState.value?.let {
                    distance = kmDistanceBetweenTwoPoints(
                        it, Point(bankInfo.latitude.toDouble(), bankInfo.longitude.toDouble())
                    )
                }
                withContext(Dispatchers.Main) {
                    _bankInfoState.value = BankInfoState.Content(bankId, bankInfo, distance)
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    _bankInfoState.value = BankInfoState.Error
                }
            }
        }
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

    fun showRoute(address: String, start: Point, destination: Point) {
        resetRoute()
        routeAddress = address
        buildRoute(start, destination)
    }

    fun resetRoute() {
        _routeState.value = null
    }

    private fun buildRoute(start: Point, destination: Point) {
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
            points, drivingOptions, vehicleOptions, drivingRouteListener
        )
    }

    private fun kmDistanceBetweenTwoPoints(first: Point, second: Point): Double {
        return acos(
            (sin(Math.toRadians(first.latitude)) * sin(Math.toRadians(second.latitude))) + (cos(
                Math.toRadians(first.latitude)
            ) * cos(Math.toRadians(second.latitude))) * (cos(
                Math.toRadians(second.longitude) - Math.toRadians(
                    first.longitude
                )
            ))
        ) * 6371
    }
}