package com.crackcode.mobile

/**
 * Step 2: parts of the app hidden from the UI without deleting their code.
 * Flip a value back to true to show the feature again.
 */
object UiToggles {
    /** The floating quick-switch pill across the whole app (and its settings card). */
    const val FLOATING_SWITCHER = false

    /** Jarvis: orb, drawer chip, command strip, settings row, tile and shortcut. */
    const val JARVIS = false

    /** Voice calls, mic voice notes and live dictation ("Stream words in real-time"). */
    const val VOICE = false

    /** STEP 1: first-run install / agent-pick / terminal-icon onboarding. Code kept, UI hidden. */
    const val FIRST_RUN_INSTALLER = false
    const val AGENT_CHOICE_ONBOARDING = true
    const val TERMINAL_LAUNCHER_ONBOARDING = false

    /** STEP 1: UI customisation hidden; themes stay System / Light / Dark. */
    const val THEME_GALLERY = false
    const val PIKU_PALETTE_PICKER = false
    const val CHAT_TEXT_SCALING = false
    const val CHAT_HEADER_ICONS = false

    /** STEP 2: extra full-screen nav (Terminal / Notes / Browser) hidden; floating switcher already off. */
    const val EXTRA_NAV_SCREENS = false

    /** STEP 5: in-app updater hidden (it points at the upstream repo, not CrackCode). */
    const val APP_UPDATER = false
}
