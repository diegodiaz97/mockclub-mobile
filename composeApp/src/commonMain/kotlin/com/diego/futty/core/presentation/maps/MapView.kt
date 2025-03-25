package com.diego.futty.core.presentation.maps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.DayColorScheme
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.core.OrnamentSettings
import io.github.dellisd.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds

@Composable
fun MapView() {
    val camera =
        rememberCameraState(
            firstPosition =
                CameraPosition(
                    target = Position(latitude = -51.6965427, longitude = -57.9225379),
                    zoom = 16.1
                )
        )

    LaunchedEffect(Unit) {
        camera.animateTo(
            finalPosition =
                camera.position.copy(
                    target = Position( // Tandil
                        latitude = -37.3286385,
                        longitude = -59.1373829
                    )
                ),
            duration = 3.seconds,
        )
    }

    val style = if (MaterialTheme.colorScheme == DayColorScheme) {
        "https://tiles.basemaps.cartocdn.com/gl/positron-gl-style/style.json"
    } else {
        "https://tiles.basemaps.cartocdn.com/gl/dark-matter-gl-style/style.json"
    }

    MaplibreMap(
        modifier = Modifier.fillMaxWidth().height(400.dp).clip(RoundedCornerShape(12.dp)),
        styleUri = style,
        zoomRange = 1f..20f,
        pitchRange = 8f..20f,
        ornamentSettings = OrnamentSettings.AllDisabled,
        cameraState = camera
    ) { }
}
