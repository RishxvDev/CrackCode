package com.crackcode.mobile.ui.piku

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Step 4: the three Piku looks. Only the chat chrome (header, composer, screen background) uses these;
 * the app's existing light/dark/auto theme mode and colour themes stay untouched.
 */
enum class PikuPalette(
    val label: String,
    val header: Color,
    val screen: Color,
    val accent: Color,
    val accentEnd: Color,
    val inputTop: Color,
    val inputBottom: Color,
    val plusBg: Color,
    val chipBg: Color,
    val badge: Color,
    val ink: Color,
    val onAccent: Color,
) {
    MINT(
        "Mint",
        Color(0xFFB5FFA8), Color(0xFFECFFE9),
        Color(0xFFA5FF52), Color(0xFF81EE77),
        Color(0xFF6F6F6F), Color(0xFF939393),
        Color(0xFFE8FFE5), Color(0xFFEBFFDA),
        Color(0xFF424242), Color(0xFF000000), Color(0xFFFFFFFF),
    ),
    BLUSH(
        "Blush",
        Color(0xFFFFD3E2), Color(0xFFFFF3F7),
        Color(0xFFFF5C8A), Color(0xFFFF8FB1),
        Color(0xFF6F5F66), Color(0xFF937F88),
        Color(0xFFFFE7EF), Color(0xFFFFE0EC),
        Color(0xFF424242), Color(0xFF1C1C1C), Color(0xFFFFFFFF),
    ),
    NIGHT(
        "Night",
        Color(0xFF254733), Color(0xFF122019),
        Color(0xFF81EE77), Color(0xFFB5FFA8),
        Color(0xFF2E2E2E), Color(0xFF454545),
        Color(0xFF2B4A37), Color(0xFF23402F),
        Color(0xFF0E0E0E), Color(0xFFECFFE9), Color(0xFF122019),
    );

    companion object {
        fun fromId(id: String?): PikuPalette = values().firstOrNull { it.name == id } ?: MINT
    }
}

/** Tiny preference holder so the palette survives restarts; compose state makes the switch instant. */
object PikuPaletteManager {
    private const val PREFS = "piku_prefs"
    private const val KEY = "palette"

    private var prefs: android.content.SharedPreferences? = null
    var current: PikuPalette by mutableStateOf(PikuPalette.MINT)
        private set

    fun init(context: Context) {
        if (prefs != null) return
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        current = PikuPalette.fromId(prefs?.getString(KEY, null))
    }

    fun select(palette: PikuPalette) {
        current = palette
        prefs?.edit()?.putString(KEY, palette.name)?.apply()
    }
}
