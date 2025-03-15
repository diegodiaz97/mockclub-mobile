package com.diego.futty.design.presentation.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.Grey100
import com.diego.futty.core.presentation.Grey900
import com.diego.futty.design.presentation.component.avatar.Avatar

sealed class TopBarActionType {
    class Button(
        private val text: String,
        private val onClick: () -> Unit
    ) : TopBarActionType() {
        @Composable
        override fun Draw() {
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClick() }
                    .background(Grey100)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = Grey900,
                style = typography.labelLarge,
                text = text
            )
        }
    }

    class Icon(
        private val icon: ImageVector,
        private val onClick: () -> Unit
    ) : TopBarActionType() {
        @Composable
        override fun Draw() {
            TopBarIcon(icon, onClick)
        }
    }

    class Profile(
        private val image: Painter? = null,
        private val icon: ImageVector? = null,
        private val initials: String? = null,
        private val tint: Color = Grey900,
        private val background: Color = Grey100,
        private val onClick: () -> Unit
    ) : TopBarActionType() {
        @Composable
        override fun Draw() {
            when {
                image != null -> {
                    Avatar.FullImageAvatar(
                        image = image,
                        onClick = onClick
                    ).Draw()
                }

                initials != null -> {
                    Avatar.InitialsAvatar(
                        initials = initials,
                        tint = tint,
                        background = background,
                        onClick = onClick
                    ).Draw()
                }

                icon != null -> {
                    Avatar.IconAvatar(
                        icon = icon,
                        tint = tint,
                        background = background,
                        onClick = onClick
                    ).Draw()
                }

                else -> {
                    /* Do nothing */
                }
            }
        }
    }

    class BothAction(
        private val button: Button,
        private val icon: Icon,
    ) : TopBarActionType() {
        @Composable
        override fun Draw() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                button.Draw()
                icon.Draw()
            }
        }
    }

    object None : TopBarActionType() {
        @Composable
        override fun Draw() {
            // Do nothing
        }
    }

    @Composable
    abstract fun Draw()
}
