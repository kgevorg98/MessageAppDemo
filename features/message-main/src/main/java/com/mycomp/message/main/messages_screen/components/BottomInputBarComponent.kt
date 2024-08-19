package com.mycomp.message.main.messages_screen.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mycomp.domain.models.MessageModel
import com.mycomp.message.main.R
import com.mycomp.message.main.messages_screen.Constants.EMPTY_STRING
import com.mycomp.message.main.messages_screen.utils.isPhoto
import com.mycomp.message.main.messages_screen.utils.isVideo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomInputBar(
    onSendMessage: (message: MessageModel) -> Unit,
    editingMessage: MessageModel? = null,
    onEditingDone: () -> Unit
) {
    var inputText by remember {
        mutableStateOf(
            TextFieldValue(
                editingMessage?.text ?: EMPTY_STRING
            )
        )
    }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var isPhoto by remember { mutableStateOf(false) }
    var isVideo by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(editingMessage) {
        val text = editingMessage?.text ?: EMPTY_STRING
        inputText = TextFieldValue(text = text, selection = TextRange(text.length))
        if (editingMessage != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val context = LocalContext.current
    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedUri = uri
            isPhoto = uri.isPhoto(context)
            isVideo = uri.isVideo(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
    ) {
        if (selectedUri != null) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(130.dp)
                    .padding(8.dp)
            ) {
                if (isPhoto) {
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedUri),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else if (isVideo) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(130.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.video_content_description),
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = {
                        selectedUri = null
                        isPhoto = false
                        isVideo = false
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(50)
                        )
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.remove_content_description),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(text = stringResource(id = R.string.type_a_message_placeholder)) },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    pickMediaLauncher.launch(
                                        PickVisualMediaRequest(
                                            PickVisualMedia.ImageAndVideo
                                        )
                                    )
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_attach_24),
                                    contentDescription = stringResource(R.string.attach_content_description),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputText.text.isNotEmpty() || selectedUri != null) {
                        if (editingMessage != null) {
                            onSendMessage(
                                editingMessage.copy(
                                    text = inputText.text
                                )
                            )
                            onEditingDone()
                        } else {
                            onSendMessage(
                                MessageModel(
                                    text = inputText.text,
                                    photoUri = if (isPhoto) selectedUri?.toString()
                                        ?: EMPTY_STRING else EMPTY_STRING,
                                    videoUri = if (isVideo) selectedUri?.toString()
                                        ?: EMPTY_STRING else EMPTY_STRING
                                )
                            )
                        }
                        inputText = TextFieldValue(EMPTY_STRING)
                        selectedUri = null
                        isPhoto = false
                        isVideo = false
                    } else {
                      //  TODO("Add voice message logic")
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (inputText.text.isNotEmpty() || selectedUri != null) {
                            R.drawable.ic_send_24
                        } else {
                            R.drawable.ic_voice_24
                        }
                    ),
                    contentDescription = stringResource(R.string.send_content_description),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

