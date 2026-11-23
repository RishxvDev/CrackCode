package com.crackcode.mobile.ui.piku

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/** Palette from the reference design: mint screen, neon accents, charcoal text, matte grey composer. */
object PikuColors {
    val ScreenBg = Color(0xFFE8FCE8)
    val Neon = Color(0xFF7CFC00)
    val NeonSoft = Color(0xFF98FB98)
    val InputGrey = Color(0xFF707070)
    val Charcoal = Color(0xFF1C1C1C)
    val MintChip = Color(0xFFB9FBC0)
    val BubbleIn = Color(0xFFD6FBD6)
    val White = Color(0xFFFFFFFF)
}

data class PikuMessage(val text: String, val isUser: Boolean)

/**
 * Minimalist single-column chat UI with a retro pixel mascot.
 * Drop-in: wire [onSend], [onNewChat] and [onSessionClick] to the app's chat controller.
 */
@Composable
fun PikuChatScreen(
    modifier: Modifier = Modifier,
    sessionName: String = "PROJECT NAME",
    modelName: String = "CLAUDE",
    messages: List<PikuMessage> = emptyList(),
    sessions: List<String> = listOf("Project Name", "Old session", "Reverse engineering"),
    onSend: (String) -> Unit = {},
    onNewChat: () -> Unit = {},
    onSessionClick: (String) -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = PikuColors.ScreenBg,
                modifier = Modifier.width(280.dp),
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "SESSIONS",
                    color = PikuColors.Charcoal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(12.dp))
                sessions.forEach { name ->
                    Text(
                        text = name,
                        color = PikuColors.Charcoal,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSessionClick(name)
                                scope.launch { drawerState.close() }
                            }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                    )
                }
            }
        },
        modifier = modifier,
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = PikuColors.ScreenBg) {
            Column(modifier = Modifier.fillMaxSize()) {
                PikuTopBar(
                    title = sessionName,
                    onMenu = { scope.launch { drawerState.open() } },
                    onNewChat = onNewChat,
                )

                Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                    if (messages.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            PikuMascot(modifier = Modifier.padding(end = 28.dp))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(messages) { msg -> PikuBubble(msg) }
                        }
                    }
                }

                PikuComposer(
                    modelName = modelName,
                    onSend = onSend,
                )
            }
        }
    }
}

@Composable
private fun PikuTopBar(
    title: String,
    onMenu: () -> Unit,
    onNewChat: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(PikuColors.Neon),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Open sessions",
            tint = PikuColors.Charcoal,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(28.dp)
                .clickable(onClick = onMenu),
        )
        Text(
            text = title,
            color = PikuColors.Charcoal,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        StarBadge(onClick = onNewChat)
        Spacer(Modifier.width(14.dp))
    }
}

@Composable
private fun StarBadge(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(46.dp)
            .clickable(onClick = onClick),
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val outer = minOf(cx, cy) * 0.95f
            val inner = outer * 0.46f
            val path = androidx.compose.ui.graphics.Path()
            for (i in 0 until 10) {
                val angle = Math.toRadians((i * 36 - 90).toDouble())
                val r = if (i % 2 == 0) outer else inner
                val x = cx + kotlin.math.cos(angle).toFloat() * r
                val y = cy + kotlin.math.sin(angle).toFloat() * r
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = PikuColors.NeonSoft)
        }
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "New chat",
            tint = PikuColors.Charcoal,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun PikuBubble(msg: PikuMessage) {
    val align = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = align) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (msg.isUser) PikuColors.InputGrey else PikuColors.BubbleIn,
        ) {
            Text(
                text = msg.text,
                color = if (msg.isUser) PikuColors.White else PikuColors.Charcoal,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            )
        }
    }
}

@Composable
private fun PikuComposer(
    modelName: String,
    onSend: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        shape = RoundedCornerShape(28.dp),
        color = PikuColors.InputGrey,
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp, max = 140.dp)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                textStyle = TextStyle(color = PikuColors.White, fontSize = 16.sp),
                decorationBox = { inner ->
                    if (text.isEmpty()) {
                        Text(
                            text = "Reply to Piku...",
                            color = PikuColors.White.copy(alpha = 0.72f),
                            fontSize = 16.sp,
                        )
                    }
                    inner()
                },
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PikuColors.White.copy(alpha = 0.16f))
                        .clickable { /* attach */ },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Attach", tint = PikuColors.White)
                }

                Spacer(Modifier.width(10.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = PikuColors.MintChip,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = PikuColors.Charcoal,
                            modifier = Modifier.size(14.dp),
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = modelName,
                            color = PikuColors.Charcoal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(PikuColors.Neon)
                        .clickable {
                            val value = text.trim()
                            if (value.isNotEmpty()) {
                                onSend(value)
                                text = ""
                            }
                        },
                ) {
                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = "Send",
                        tint = PikuColors.White,
                        modifier = Modifier.size(26.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun PikuMascot(modifier: Modifier = Modifier, pixel: Dp = 6.dp) {
    // 12x8 retro pixel cat/alien, drawn without any asset file.
    val art = listOf(
        "....GGGG....",
        "...GWWWWG...",
        "..GWWWWWWG..",
        "..GWKWWKWG..",
        "..GWWWWWWG..",
        "..GWKWKWG...",
        "...GWWWWG...",
        "....GWWG....",
        "...GG..GG...",
    )
    androidx.compose.foundation.Canvas(
        modifier = modifier.size(pixel * 12, pixel * art.size),
    ) {
        val step = pixel.toPx()
        art.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                val color = when (ch) {
                    'G' -> PikuColors.Neon
                    'W' -> PikuColors.MintChip
                    'K' -> PikuColors.Charcoal
                    else -> return@forEachIndexed
                }
                drawRect(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(x * step, y * step),
                    size = androidx.compose.ui.geometry.Size(step, step),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PikuChatPreview() {
    PikuChatScreen(sessionName = "PROJECT NAME")
}
