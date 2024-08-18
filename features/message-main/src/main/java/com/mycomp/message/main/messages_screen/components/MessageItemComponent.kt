package com.mycomp.message.main.messages_screen.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mycomp.domain.models.MessageModel
import com.mycomp.message.main.R
import com.mycomp.message.main.messages_screen.Constants.DATE_FORMAT_HOURS_MINUTES
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MessageItem(
    message: MessageModel,
    isLongPressed: Boolean,
    onDeleteMessage: (Int) -> Unit,
    onEditMessage: (MessageModel) -> Unit,
    onLongPressMessage: (Int) -> Unit
) {
    val timeFormat = SimpleDateFormat(DATE_FORMAT_HOURS_MINUTES, Locale.getDefault())
    val formattedTime = timeFormat.format(message.date)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
            .combinedClickable(
                onClick = { /* Regular click action, if any */ },
                onLongClick = {
                    onLongPressMessage(message.id)
                }
            )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(min = 72.dp)
        ) {
            Column(modifier = Modifier.padding(2.dp)) {
                val hasMedia = message.photoUri.isNotEmpty() || message.videoUri.isNotEmpty()

                if (message.text.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                end = 10.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            )
                            .wrapContentWidth()
                    ) {
                        Text(
                            text = message.text,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(end = 60.dp)
                        )
                        if (!hasMedia) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                            ) {
                                Text(
                                    text = formattedTime,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_seen_16),
                                    contentDescription = "seen",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (message.photoUri.isNotEmpty()) {
                    Box(modifier = Modifier.wrapContentWidth()) {
                        val mediaModifier = Modifier
                            .width(260.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))

                        Image(
                            painter = rememberAsyncImagePainter(model = message.photoUri),
                            contentDescription = null,
                            modifier = mediaModifier,
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp),
                            fontSize = 12.sp
                        )
                    }
                }

                if (message.videoUri.isNotEmpty()) {
                    Box(modifier = Modifier.wrapContentWidth()) {
                        val mediaModifier = Modifier
                            .width(260.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))

                        val uri = Uri.parse(message.videoUri)
                        VideoPlayer(
                            uri = uri,
                            modifier = mediaModifier
                        )
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp),
                            fontSize = 12.sp
                        )
                    }
                }

                if (isLongPressed) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                    ) {
                        IconButton(
                            onClick = { onDeleteMessage(message.id) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete_24),
                                contentDescription = stringResource(R.string.delete_content_description),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (message.text.isNotEmpty()) {
                            IconButton(
                                onClick = { onEditMessage(message) }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_edit_24),
                                    contentDescription = stringResource(R.string.edit_content_description),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}