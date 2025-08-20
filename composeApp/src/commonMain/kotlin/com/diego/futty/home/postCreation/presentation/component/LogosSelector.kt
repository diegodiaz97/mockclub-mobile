package com.diego.futty.home.postCreation.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.bottomsheet.BottomSheet
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.input.TextInput
import com.diego.futty.home.design.presentation.component.post.PostLogosSelection
import com.skydoves.flexible.core.InternalFlexibleApi
import com.skydoves.flexible.core.log
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.launch

@OptIn(InternalFlexibleApi::class)
@Composable
fun LogosSelector(
    teamName: String,
    brandName: String,
    teamLogo: ByteArray?,
    brandLogo: ByteArray?,
    designerLogo: ByteArray?,
    onDismiss: () -> Unit,
    onConfirm: (IdentityRequest) -> Unit,
) = BottomSheet(
    draggable = true,
    onDismiss = { onDismiss() },
) {
    var tempTeamName by remember { mutableStateOf(teamName) }
    var tempBrandName by remember { mutableStateOf(brandName) }
    var tempTeamLogo by remember { mutableStateOf(teamLogo) }
    var tempBrandLogo by remember { mutableStateOf(brandLogo) }
    var tempDesignerLogo by remember { mutableStateOf(designerLogo) }
    var showGallery by remember { mutableStateOf(false) }
    var selectionType by remember { mutableStateOf("") }

    fun isButtonEnabled(): Boolean =
        tempTeamName.isNotEmpty() && tempBrandName.isNotEmpty() && tempTeamLogo != null && tempBrandLogo != null

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp),
            text = "Identidad",
            style = typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = colorGrey900()
        )

        PostLogosSelection(
            modifier = Modifier.padding(horizontal = 16.dp),
            teamLogo = tempTeamLogo,
            brandLogo = tempBrandLogo,
            designerLogo = tempDesignerLogo,
            onLogoClicked = { type ->
                selectionType = type
                showGallery = true
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextInput.Input(
                modifier = Modifier.weight(1f),
                input = tempTeamName,
                placeholder = "Introduce el equipo",
                background = colorGrey0(),
                onTextChangeAction = {
                    if (it.length <= 12) {
                        tempTeamName = it.lowercase()
                            .replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
                    }
                }
            ).Draw()
            TextInput.Input(
                modifier = Modifier.weight(1f),
                input = tempBrandName,
                placeholder = "Introduce la marca",
                background = colorGrey0(),
                onTextChangeAction = {
                    if (it.length <= 12) {
                        tempBrandName = it.lowercase()
                            .replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
                    }
                }
            ).Draw()
        }

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            title = "Listo",
            isEnabled = isButtonEnabled(),
            onClick = {
                onConfirm(
                    IdentityRequest(
                        teamName = tempTeamName,
                        brandName = tempBrandName,
                        teamLogo = tempTeamLogo,
                        brandLogo = tempBrandLogo,
                        designerLogo = tempDesignerLogo
                    )
                )
            }
        )

        // Gallery
        val coroutineScope = rememberCoroutineScope()

        val launcher = rememberFilePickerLauncher(
            type = FileKitType.Image,
            mode = FileKitMode.Single,
        ) { image ->
            coroutineScope.launch {
                try {
                    if (image == null) return@launch
                    val imageBitmap = image.toImageBitmap()
                    val imageByteArray = imageBitmap.encodeToByteArray(
                        format = ImageFormat.PNG, // JPEG or PNG
                        quality = 60 // Compression quality (0-100)
                    )
                    when (selectionType) {
                        "team" -> tempTeamLogo = imageByteArray
                        "brand" -> tempBrandLogo = imageByteArray
                        else -> tempDesignerLogo = imageByteArray
                    }
                    selectionType = ""
                } catch (e: Exception) {
                    log(e.message ?: "Error al subir $selectionType logo de post")
                }
            }
            showGallery = false
        }
        if (showGallery) {
            launcher.launch()
        }
    }
}

data class IdentityRequest(
    val teamName: String,
    val brandName: String,
    val teamLogo: ByteArray?,
    val brandLogo: ByteArray?,
    val designerLogo: ByteArray?,
)
