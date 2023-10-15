package ru.phrogs.moretechhackathon.presentation.ui.screen.map

import android.Manifest
import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.domain.entity.BankCoordinates
import ru.phrogs.moretechhackathon.domain.entity.LoadType
import ru.phrogs.moretechhackathon.presentation.ui.common.lifecycle.observeAsState
import ru.phrogs.moretechhackathon.presentation.ui.common.state.LoadingProgress
import ru.phrogs.moretechhackathon.presentation.ui.navigation.MoreTechDestinations
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.BankOfficeDetails
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.BankRating
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.ClusterView
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.MapHolder
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.MapView
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.NavigationOverlay
import ru.phrogs.moretechhackathon.presentation.ui.theme.BANK_ICON_SCALE
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingMedium
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingSmall
import ru.phrogs.moretechhackathon.presentation.ui.theme.WhiteBlue
import ru.phrogs.moretechhackathon.presentation.uistate.map.BankInfoState
import ru.phrogs.moretechhackathon.presentation.uistate.map.BankRatingState
import ru.phrogs.moretechhackathon.presentation.uistate.map.MapState
import ru.phrogs.moretechhackathon.presentation.uistate.map.RouteInfo
import ru.phrogs.moretechhackathon.presentation.viewmodel.MapViewModel

private val iconStyle = IconStyle().setScale(BANK_ICON_SCALE)
private var previousLocationPoint: PlacemarkMapObject? = null
private var previousRoute: PolylineMapObject? = null

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(context: Context, navController: NavController) {
    val bankImageProvider = ImageProvider.fromResource(context, R.drawable.placemark)
    val geoLocationImageProvider = ImageProvider.fromResource(context, R.drawable.geolocation)
    val clusterListener = ClusterListener { cluster ->
        cluster.appearance.setView(ViewProvider(ClusterView(context).apply {
            setData(cluster.placemarks.size)
        }))
    }

    val mapViewModel: MapViewModel = koinViewModel()
    val mapState by remember { mapViewModel.mapState }
    val bankInfoState by remember { mapViewModel.bankInfoState }
    val bankRatingState by remember { mapViewModel.bankRatingState }
    val locationState by remember { mapViewModel.currentLocationState }
    var shouldShowBankInfoBottomSheet by remember { mutableStateOf(false) }
    var shouldShowBankRatingBottomSheet by remember { mutableStateOf(false) }
    val bankInfoSheetState = rememberModalBottomSheetState()
    val bankRatingSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val placeMarkTapListener = MapObjectTapListener { mapObject, _ ->
        val bankId = mapObject.userData as Int
        mapViewModel.loadBankData(bankId)
        shouldShowBankInfoBottomSheet = true
        true
    }
    val route by remember { mapViewModel.routeState }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Crossfade(targetState = mapState, label = "") { state ->
        when (state) {
            MapState.Loading -> LoadingProgress()
            is MapState.Content -> {
                MapContent(
                    state,
                    locationState,
                    bankImageProvider,
                    clusterListener,
                    locationPermissionsState,
                    navController,
                    route,
                    placeMarkTapListener,
                    mapViewModel::resetRoute,
                    mapViewModel.startCameraPosition,
                    onRecommendationsClick = {
                        mapViewModel.loadRatingData()
                        shouldShowBankRatingBottomSheet = true
                    }
                )
            }

            MapState.Error -> Unit
        }
    }

    if (shouldShowBankInfoBottomSheet) {
        BankInfoBottomSheet({ shouldShowBankInfoBottomSheet = false },
            {
                scope.launch { bankInfoSheetState.hide() }
                    .invokeOnCompletion { shouldShowBankInfoBottomSheet = false }
            },
            bankInfoSheetState,
            bankInfoState,
            locationPermissionsState,
            locationState,
            mapViewModel
        )
    }

    if (shouldShowBankRatingBottomSheet) {
        BankRatingBottomSheet(
            onDismissRequest = {
                shouldShowBankRatingBottomSheet = false
            },
            onCloseButtonPressed = {
                scope.launch { bankInfoSheetState.hide() }
                    .invokeOnCompletion { shouldShowBankRatingBottomSheet = false }
            },
            bankRatingSheetState = bankRatingSheetState,
            bankRatingState = bankRatingState,
            openBankInfoBottomSheet = {
                shouldShowBankInfoBottomSheet = true
            },
            mapViewModel = mapViewModel
        )
    }

    val lifeCycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()

    LifecycleUpdater(lifeCycleState, mapViewModel, locationPermissionsState, context)

    RouteUpdater(route)

    GeolocationUpdater(locationState, geoLocationImageProvider)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
private fun BankInfoBottomSheet(
    onDismissRequest: () -> Unit,
    onCloseButtonPressed: () -> Unit,
    bankInfoSheetState: SheetState,
    bankInfoState: BankInfoState,
    locationPermissionsState: MultiplePermissionsState,
    locationState: Point?,
    mapViewModel: MapViewModel
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bankInfoSheetState,
        containerColor = WhiteBlue,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Crossfade(targetState = bankInfoState, label = "") { state ->
            when (state) {
                is BankInfoState.Content -> BankOfficeDetails(distanceFromClient = state.distance,
                    address = state.bankInfo.address,
                    individualsLoad = LoadType.LOW,
                    entitiesLoad = LoadType.LOW,
                    openHours = state.bankInfo.openHours,
                    openHoursIndividual = state.bankInfo.openHoursIndividual,
                    todayOpenHours = state.bankInfo.openHours.openHours.first(),
                    availableForBlind = state.bankInfo.hasRamp,
                    metroStation = listOf(state.bankInfo.metroStation!!),
                    onRouteClick = {
                        if (!locationPermissionsState.allPermissionsGranted) {
                            locationPermissionsState.launchMultiplePermissionRequest()
                        } else if (locationState != null) {
                            mapViewModel.showRoute(
                                state.bankInfo.address, locationState, Point(
                                    state.bankInfo.latitude.toDouble(),
                                    state.bankInfo.longitude.toDouble()
                                )
                            )
                            onCloseButtonPressed()
                        }
                    })

                BankInfoState.Error -> Unit
                BankInfoState.Loading -> LoadingProgress(0.5f)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BankRatingBottomSheet(
    onDismissRequest: () -> Unit,
    onCloseButtonPressed: () -> Unit,
    bankRatingSheetState: SheetState,
    bankRatingState: BankRatingState,
    openBankInfoBottomSheet: () -> Unit,
    mapViewModel: MapViewModel
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bankRatingSheetState,
        containerColor = WhiteBlue,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Crossfade(targetState = bankRatingState, label = "") { state ->
            when (state) {
                is BankRatingState.Content -> BankRating(
                    rating = state.rating,
                    queryName = "Кредит",
                    onBankEntityClick = {
                        mapViewModel.loadBankData(it)
                        openBankInfoBottomSheet()
                        onCloseButtonPressed()
                    }
                )

                BankRatingState.Error -> Unit
                BankRatingState.Loading -> LoadingProgress(0.5f)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LifecycleUpdater(
    lifeCycleState: Lifecycle.Event,
    mapViewModel: MapViewModel,
    locationPermissionsState: MultiplePermissionsState,
    context: Context
) {
    LaunchedEffect(lifeCycleState) {
        when (lifeCycleState) {
            Lifecycle.Event.ON_PAUSE -> {
                mapViewModel.stopLocationTracking()
                MapHolder.mapView?.onStop()
            }

            Lifecycle.Event.ON_RESUME -> if (locationPermissionsState.allPermissionsGranted) {
                mapViewModel.startLocationTracking(context)
                MapHolder.mapView?.onStart()
            }

            else -> Unit
        }
    }
}

@Composable
private fun RouteUpdater(route: RouteInfo?) {
    LaunchedEffect(key1 = route, key2 = MapHolder.mapView == null) {
        val mapView = MapHolder.mapView
        if (mapView != null && route != null) {
            previousRoute?.let {
                if (it.isValid) {
                    try {
                        mapView.mapWindow.map.mapObjects.remove(it)
                    } catch (_: Exception) {
                    }
                }
            }
            previousRoute = mapView.mapWindow.map.mapObjects.addPolyline(route.geometry)

        } else if (mapView != null && previousRoute != null) {
            previousRoute?.let {
                if (it.isValid) {
                    try {
                        mapView.mapWindow?.map?.mapObjects?.remove(it)
                    } catch (_: Exception) {
                    }
                }
            }
            previousRoute = null
        }
    }
}

@Composable
private fun GeolocationUpdater(
    locationState: Point?, geoLocationImageProvider: ImageProvider
) {
    LaunchedEffect(key1 = locationState, key2 = MapHolder.mapView == null) {
        val mapView = MapHolder.mapView
        if (locationState != null && mapView != null) {
            previousLocationPoint?.let {
                if (it.isValid) {
                    try {
                        mapView.mapWindow.map.mapObjects.remove(it)
                    } catch (_: Exception) {
                    }
                }
            }
            previousLocationPoint = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = locationState
                setIcon(geoLocationImageProvider, iconStyle)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapContent(
    state: MapState.Content,
    locationState: Point?,
    bankImageProvider: ImageProvider,
    clusterListener: ClusterListener,
    locationPermissionsState: MultiplePermissionsState,
    navController: NavController,
    route: RouteInfo?,
    placeMarkTapListener: MapObjectTapListener,
    resetRouteInfo: () -> Unit,
    startCameraPosition: CameraPosition,
    onRecommendationsClick: () -> Unit
) {

    MapView(
        startCameraPosition
    )

    if (route != null) {
        NavigationOverlay(
            address = route.destinationAddress, timeMinutes = route.routeTimeMinutes
        ) {
            resetRouteInfo()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(PaddingMedium),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                FloatingActionButton(onClick = {
                    if (!locationPermissionsState.allPermissionsGranted) {
                        locationPermissionsState.launchMultiplePermissionRequest()
                    } else {
                        val mapView = MapHolder.mapView
                        if (locationState != null && mapView != null) {
                            mapView.mapWindow.map.move(
                                CameraPosition(
                                    locationState, 13f, 150f, 0f
                                )
                            )
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.navigation_icon),
                        contentDescription = null
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(PaddingSmall)) {
                    Button(onClick = { navController.navigate(MoreTechDestinations.CHAT) }) {
                        Text(text = stringResource(id = R.string.chat))
                    }
                    Button(onClick = { onRecommendationsClick() }) {
                        Text(text = stringResource(id = R.string.recommendations))
                    }
                    Button(onClick = { navController.navigate(MoreTechDestinations.SEARCH_HISTORY) }) {
                        Text(text = stringResource(id = R.string.history))
                    }
                }
            }
        }
    }

    BankMarksUpdater(state, clusterListener, bankImageProvider, placeMarkTapListener)
}

@Composable
private fun BankMarksUpdater(
    state: MapState.Content,
    clusterListener: ClusterListener,
    bankImageProvider: ImageProvider,
    placeMarkTapListener: MapObjectTapListener
) {
    val points = state.bankCoordinates
    LaunchedEffect(key1 = points, key2 = MapHolder.mapView == null) {
        val mapView = MapHolder.mapView
        if (mapView != null) {
            clusterizeMapPoints(
                mapView,
                clusterListener,
                points,
                bankImageProvider,
                iconStyle,
                placeMarkTapListener,
                clusterRadius = 60.0,
                minZoom = 13
            )
        }
    }
}

fun clusterizeMapPoints(
    mapView: MapView,
    clusterListener: ClusterListener,
    placeMarks: List<BankCoordinates>,
    placeMarkImageProvider: ImageProvider,
    iconStyle: IconStyle,
    placeMarkTapListener: MapObjectTapListener,
    clusterRadius: Double,
    minZoom: Int
) {
    val clusterizedCollection =
        mapView.mapWindow.map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)

    placeMarks.forEach { point ->
        val placeMark = clusterizedCollection.addPlacemark().apply {
            geometry = Point(point.latitude, point.longitude)
            setIcon(placeMarkImageProvider, iconStyle)
            userData = point.bankId
        }
        placeMark.addTapListener(placeMarkTapListener)
    }

    clusterizedCollection.clusterPlacemarks(clusterRadius, minZoom)
}