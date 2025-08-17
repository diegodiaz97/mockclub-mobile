package com.diego.futty.home.design.presentation.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.X
import com.diego.futty.core.presentation.theme.colorError
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey800
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.avatar.Avatar
import com.diego.futty.home.design.presentation.component.button.PrimaryButton
import com.diego.futty.home.design.presentation.component.button.SecondaryButton
import com.diego.futty.home.design.presentation.component.image.AsyncImage

sealed interface Modal {
    @Composable
    fun Draw()

    class GenericModal(
        val image: String,
        val title: String,
        val subtitle: String,
        val primaryButton: String,
        val secondaryButton: String? = null,
        val onPrimaryAction: () -> Unit,
        val onSecondaryAction: () -> Unit = {},
        val onDismiss: () -> Unit = {}
    ) : Modal {
        @Composable
        override fun Draw() {
            BottomSheet(
                draggable = true,
                onDismiss = { onDismiss() }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Avatar.IconAvatar(
                        icon = PhosphorIcons.Bold.X,
                        background = colorGrey100(),
                        onClick = { onDismiss() }
                    ).Draw()

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(100.dp),
                        shimmerModifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorGrey100()),
                        contentScale = ContentScale.Fit,
                        contentDescription = "modal image",
                        image = image
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center,
                        style = typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorGrey900(),
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = subtitle,
                        textAlign = TextAlign.Center,
                        style = typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = colorGrey800(),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            title = primaryButton,
                            onClick = { onPrimaryAction() }
                        )
                        if (secondaryButton != null) {
                            SecondaryButton(
                                modifier = Modifier.weight(1f),
                                title = secondaryButton,
                                onClick = { onSecondaryAction() }
                            )
                        }
                    }
                }
            }
        }
    }

    class GenericErrorModal(
        val onPrimaryAction: () -> Unit,
        val onDismiss: () -> Unit,
    ) : Modal {
        @Composable
        override fun Draw() {
            GenericModal(
                image = "https://cdn3d.iconscout.com/3d/free/thumb/free-warning-3d-illustration-download-in-png-blend-fbx-gltf-file-formats--alert-error-danger-sign-user-interface-pack-illustrations-4715732.png",
                title = "Algo salió mal",
                subtitle = "Intenta de nuevo más tarde.",
                primaryButton = "Entendido",
                secondaryButton = null,
                onPrimaryAction = { onPrimaryAction() },
                onSecondaryAction = { },
                onDismiss = { onDismiss() },
            ).Draw()
        }
    }

    class OptionsModal(
        val options: List<OptionItem>,
        val onDismiss: () -> Unit,
    ) : Modal {
        @Composable
        override fun Draw() {
            BottomSheet(
                draggable = true,
                onDismiss = { onDismiss() }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    options.forEach { option ->
                        SecondaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            title = option.text,
                            color = if (option.type == OptionType.Basic) {
                                colorGrey900()
                            } else {
                                colorError()
                            },
                            onClick = {
                                option.action()
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}
