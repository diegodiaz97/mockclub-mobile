package com.diego.futty.home.design.presentation.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey100
import com.diego.futty.core.presentation.theme.colorGrey900
import com.diego.futty.home.design.presentation.component.avatar.Avatar

sealed class TopBarActionType {
    class Button(
        private val text: String,
        private val textColor: Color? = null,
        private val background: Color? = null,
        private val onClick: () -> Unit
    ) : TopBarActionType() {
        @Composable
        override fun Draw() {
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClick() }
                    .background(background ?: colorGrey100())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = textColor ?: colorGrey900(),
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
        private val imageUrl: String? = null,
        private val initials: String? = null,
        private val tint: Color? = null,
        private val background: Color? = null,
        private val onClick: () -> Unit
    ) : TopBarActionType() {
        @Composable
        override fun Draw() {
            Avatar.ProfileAvatar(
                imageUrl = imageUrl,
                initials = initials,
                tint = tint,
                background = background,
                onClick = onClick
            ).Draw()
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
            Spacer(Modifier.width(36.dp))
        }
    }

    @Composable
    abstract fun Draw()
}
