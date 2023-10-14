package ru.phrogs.moretechhackathon.presentation.ui.screen.map

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.compose.animation.Crossfade
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
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.phrogs.moretechhackathon.R
import ru.phrogs.moretechhackathon.domain.entity.LoadType
import ru.phrogs.moretechhackathon.presentation.ui.common.lifecycle.observeAsState
import ru.phrogs.moretechhackathon.presentation.ui.common.state.LoadingProgress
import ru.phrogs.moretechhackathon.presentation.ui.navigation.MoreTechDestinations
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.BankOfficeDetails
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.ClusterView
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.MapView
import ru.phrogs.moretechhackathon.presentation.ui.screen.map.components.NavigationOverlay
import ru.phrogs.moretechhackathon.presentation.ui.theme.BANK_ICON_SCALE
import ru.phrogs.moretechhackathon.presentation.ui.theme.PaddingMedium
import ru.phrogs.moretechhackathon.presentation.ui.theme.WhiteBlue
import ru.phrogs.moretechhackathon.presentation.uistate.map.BankInfoState
import ru.phrogs.moretechhackathon.presentation.uistate.map.MapState
import ru.phrogs.moretechhackathon.presentation.uistate.map.RouteInfo
import ru.phrogs.moretechhackathon.presentation.viewmodel.MapViewModel

private val iconStyle = IconStyle().setScale(BANK_ICON_SCALE)

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
    val locationState by remember { mapViewModel.currentLocationState }
    var shouldShowBankInfoBottomSheet by remember { mutableStateOf(false) }
    val bankInfoSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val placeMarkTapListener = MapObjectTapListener { mapObject, _ ->
        Log.e("TAP", "TAP")
        val bankId = mapObject.userData as Int
        mapViewModel.loadBankData(bankId)
        shouldShowBankInfoBottomSheet = true
        true
    }
    val route by remember { mapViewModel.routeState }
    val mapCameraListener =
        CameraListener { _, p1, _, _ -> mapViewModel.lastSavedCameraPosition = p1 }

    var forceMapRedraw by remember { mapViewModel.forceRedraw }

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
                    geoLocationImageProvider,
                    clusterListener,
                    locationPermissionsState,
                    navController,
                    forceMapRedraw,
                    mapViewModel::lastSavedCameraPosition::set,
                    mapViewModel.lastSavedCameraPosition,
                    mapCameraListener,
                    mapViewModel::forceUpdateLocation,
                    route,
                    placeMarkTapListener,
                    mapViewModel::resetRoute
                )
            }

            MapState.Error -> Unit
        }
    }

    if (shouldShowBankInfoBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { shouldShowBankInfoBottomSheet = false },
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
                                mapViewModel.showRoute(state.bankInfo.address, locationState!!, Point(
                                    state.bankInfo.latitude.toDouble(), state.bankInfo.longitude.toDouble()
                                ))
                                scope.launch { bankInfoSheetState.hide() }.invokeOnCompletion { shouldShowBankInfoBottomSheet = false }
                            }
                        })

                    BankInfoState.Error -> Unit
                    BankInfoState.Loading -> LoadingProgress(0.5f)
                }
            }
        }
    }

    val lifeCycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()

    LaunchedEffect(lifeCycleState) {
        when (lifeCycleState) {
            Lifecycle.Event.ON_PAUSE -> {
                mapViewModel.stopLocationTracking()
            }

            Lifecycle.Event.ON_RESUME -> if (locationPermissionsState.allPermissionsGranted) {
                mapViewModel.startLocationTracking(context)
                forceMapRedraw = !forceMapRedraw
            }

            else -> Unit
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapContent(
    state: MapState.Content,
    locationState: Point?,
    bankImageProvider: ImageProvider,
    geoLocationImageProvider: ImageProvider,
    clusterListener: ClusterListener,
    locationPermissionsState: MultiplePermissionsState,
    navController: NavController,
    forceMapRedraw: Boolean,
    lastCameraPosSetter: (CameraPosition) -> Unit,
    lastCameraPosition: CameraPosition?,
    cameraMapListener: CameraListener,
    forceUpdateLocation: () -> Unit,
    route: RouteInfo?,
    placeMarkTapListener: MapObjectTapListener,
    resetRouteInfo: () -> Unit
) {
    val points = state.bankCoordinates

    MapView(
        placeMarks = points,
        locationPoint = locationState,
        placeMarkImageProvider = bankImageProvider,
        geoLocationImageProvider = geoLocationImageProvider,
        iconStyle = iconStyle,
        clusterListener = clusterListener,
        placeMarkTapListener = placeMarkTapListener,
        forceMapRedraw = forceMapRedraw,
        cameraPositionState = lastCameraPosition,
        cameraListener = cameraMapListener,
        route = route
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
                        if (locationState != null) {
                            lastCameraPosSetter(
                                CameraPosition(
                                    locationState, 13f, 150f, 0f
                                )
                            )
                            forceUpdateLocation()
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.navigation_icon),
                        contentDescription = null
                    )
                }
                Row {
                    Button(onClick = { navController.navigate(MoreTechDestinations.CHAT) }) {
                        Text(text = stringResource(id = R.string.chat))
                    }
                }
            }
        }
    }
}