package com.crackcode.mobile.ui.piku

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crackcode.mobile.R

/**
 * The chat chrome's current Piku palette (Settings → Themes → Piku palette).
 * Everything the header, composer and screen background draw comes from here.
 */
object PikuUi {
    val Header: Color get() = PikuPaletteManager.current.header
    val Screen: Color get() = PikuPaletteManager.current.screen
    val Accent: Color get() = PikuPaletteManager.current.accent
    val AccentEnd: Color get() = PikuPaletteManager.current.accentEnd
    val InputTop: Color get() = PikuPaletteManager.current.inputTop
    val InputBottom: Color get() = PikuPaletteManager.current.inputBottom
    val PlusBg: Color get() = PikuPaletteManager.current.plusBg
    val ChipBg: Color get() = PikuPaletteManager.current.chipBg
    val Badge: Color get() = PikuPaletteManager.current.badge
    val Ink: Color get() = PikuPaletteManager.current.ink
    val OnAccent: Color get() = PikuPaletteManager.current.onAccent
}

/** Bebas Neue (OFL), bundled from google/fonts. */
val PikuDisplay = FontFamily(Font(R.font.bebas_neue_regular))

/**
 * The chat's header: 8% of the screen tall, rounded bottom corners, hamburger on the left, the chat title in
 * condensed ALL-CAPS in the middle and the dark scalloped "new chat" badge on the right.
 */
@Composable
fun PikuChatHeader(
    title: String,
    onMenu: () -> Unit,
    onNewChat: () -> Unit,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    trailing: @Composable RowScope.() -> Unit = {},
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val headerHeight = (screenHeight * 0.08f).dp.coerceIn(58.dp, 84.dp)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = PikuUi.Header,
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(headerHeight)
                .padding(start = 8.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { if (showBack) onBack() else onMenu() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (showBack) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Menu,
                    contentDescription = if (showBack) "Back" else "Open sessions",
                    tint = PikuUi.Ink,
                    modifier = Modifier.size(26.dp),
                )
            }
            Text(
                text = title.uppercase(),
                color = PikuUi.Ink,
                fontFamily = PikuDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                letterSpacing = 0.6.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp),
            )
            trailing()
            PikuStarBadge(onClick = onNewChat)
        }
    }
}

/** The dark scalloped badge with a white "+": new chat. */
@Composable
fun PikuStarBadge(onClick: () -> Unit, badgeSize: Dp = 50.dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(badgeSize)
            .clip(CircleShape)
            .clickable(onClick = onClick),
    ) {
        Canvas(modifier = Modifier.size(badgeSize)) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val outer = minOf(cx, cy) * 0.98f
            val inner = outer * 0.86f
            val path = Path()
            // 24-point scallop: 12 soft bumps around the circle.
            val points = 24
            for (i in 0 until points * 2) {
                val angle = Math.toRadians((i * 360.0 / (points * 2)) - 90.0)
                val r = if (i % 2 == 0) outer else inner
                val x = cx + kotlin.math.cos(angle).toFloat() * r
                val y = cy + kotlin.math.sin(angle).toFloat() * r
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = PikuUi.Badge)
        }
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "New chat",
            tint = Color.White,
            modifier = Modifier.size(24.dp),
        )
    }
}
