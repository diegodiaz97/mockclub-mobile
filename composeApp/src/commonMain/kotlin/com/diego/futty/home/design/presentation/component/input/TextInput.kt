package com.diego.futty.home.design.presentation.component.input

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diego.futty.core.presentation.theme.colorGrey0
import com.diego.futty.core.presentation.theme.colorGrey200
import com.diego.futty.core.presentation.theme.colorGrey700
import com.diego.futty.core.presentation.theme.colorGrey900
import compose.icons.TablerIcons
import compose.icons.tablericons.Eye
import compose.icons.tablericons.EyeOff
import compose.icons.tablericons.X

sealed interface TextInput {
    @Composable
    fun Draw()

    class Input(
        val input: String,
        val label: String,
        val placeholder: String = "",
        val onTextChangeAction: (String) -> Unit,
        val onFocusChanged: () -> Unit,
    ) : TextInput {
        @Composable
        override fun Draw() {
            Column {
                Text(
                    text = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = TextAlign.Start,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                TextField(
                    placeholder = {
                        Text(
                            text = placeholder,
                            textAlign = TextAlign.Start,
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Normal,
                            color = colorGrey700()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = colorGrey200(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .onFocusChanged {
                            if (it.hasFocus) onFocusChanged()
                        },
                    value = input,
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = colorGrey900(),
                        disabledTextColor = colorGrey900(),
                        focusedTextColor = colorGrey900(),
                        unfocusedContainerColor = colorGrey0(),
                        focusedContainerColor = colorGrey0(),
                        cursorColor = colorGrey900(),
                        disabledLabelColor = colorGrey900(),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = { onTextChangeAction(it) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    trailingIcon = {
                        if (input.isNotEmpty()) {
                            IconButton(onClick = { onTextChangeAction("") }) {
                                Icon(
                                    imageVector = TablerIcons.X,
                                    tint = colorGrey200(),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    class MailInput(
        val input: String,
        val onTextChangeAction: (String) -> Unit,
        val onFocusChanged: () -> Unit,
    ) : TextInput {
        @Composable
        override fun Draw() {
            Column {
                Text(
                    text = "Email",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = TextAlign.Start,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = colorGrey200(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .onFocusChanged {
                            if (it.hasFocus) onFocusChanged()
                        },
                    value = input,
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = colorGrey900(),
                        disabledTextColor = colorGrey900(),
                        focusedTextColor = colorGrey900(),
                        unfocusedContainerColor = colorGrey0(),
                        focusedContainerColor = colorGrey0(),
                        cursorColor = colorGrey900(),
                        disabledLabelColor = colorGrey900(),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = { onTextChangeAction(it) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    trailingIcon = {
                        if (input.isNotEmpty()) {
                            IconButton(onClick = { onTextChangeAction("") }) {
                                Icon(
                                    imageVector = TablerIcons.X,
                                    tint = colorGrey200(),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    class PasswordInput(
        val input: String,
        val onTextChangeAction: (String) -> Unit,
        val onFocusChanged: () -> Unit,
    ) : TextInput {
        @Composable
        override fun Draw() {
            val maxLength = 16
            val (visible, setVisible) = remember { mutableStateOf(false) }

            Column {
                Text(
                    text = "Contraseña",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = TextAlign.Start,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorGrey900()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = colorGrey200(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .onFocusChanged {
                            if (it.hasFocus) onFocusChanged()
                        },
                    value = input,
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = colorGrey900(),
                        disabledTextColor = colorGrey900(),
                        focusedTextColor = colorGrey900(),
                        unfocusedContainerColor = colorGrey0(),
                        focusedContainerColor = colorGrey0(),
                        cursorColor = colorGrey900(),
                        disabledLabelColor = colorGrey900(),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onTextChangeAction(it)
                        }
                    },
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { setVisible(visible.not()) }) {
                            Icon(
                                imageVector = if (visible) TablerIcons.Eye else TablerIcons.EyeOff,
                                tint = colorGrey200(),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }
}
