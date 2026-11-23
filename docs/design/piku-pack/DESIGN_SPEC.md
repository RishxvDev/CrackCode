# Piku chat design (implemented)

The chat chrome was rebuilt in Steps 1–4; this file records what shipped. Sprite/animation rules live in
`ANIMATION_SPEC.md`. The chat's word-by-word streaming was never changed.

## Header

- Height: ~8% of screen height, clamped to 58–84 dp; bottom corners 28 dp.
- Left: hamburger (or back arrow when a conversation is on the back stack).
- Title: `selected conversation title.uppercase()`, Bebas Neue, 30 sp, single line, ellipsized.
- Right: dark `#424242` scalloped 50 dp badge with a white `+` (new chat). IDE/Run shortcuts and the
  floating-window / dev actions sit in small slots before it.
- Source: `ui/piku/PikuChatChrome.kt` (`PikuChatHeader`, `PikuStarBadge`), used by `ui/chat/ChatScreen.kt`.

## Composer

- Outer margin 18 dp; grey gradient card `#6F6F6F → #939393`, 28 dp radius.
- Placeholder: "Reply to Piku..."; input text white.
- Pale `+` attachment button, mint model chip with chevron, 50 dp green-gradient send button.
- Source: `ui/components/ChatInputBar.kt`.

## Palettes

Settings → Themes → **Piku palette** (persisted in `piku_prefs`, instant Compose state). These only affect the
chat header, composer and screen background; the app's light/dark/auto mode and custom themes are unchanged.

| Palette | Header | Screen | Accent | Accent end | Input | Plus / chip | Ink |
|---|---|---|---|---|---|---|---|
| Mint (default) | `#B5FFA8` | `#ECFFE9` | `#A5FF52` | `#81EE77` | `#6F6F6F → #939393` | `#E8FFE5` / `#EBFFDA` | `#000000` |
| Blush | `#FFD3E2` | `#FFF3F7` | `#FF5C8A` | `#FF8FB1` | `#6F5F66 → #937F88` | `#FFE7EF` / `#FFE0EC` | `#1C1C1C` |
| Night | `#254733` | `#122019` | `#81EE77` | `#B5FFA8` | `#2E2E2E → #454545` | `#2B4A37` / `#23402F` | `#ECFFE9` |

Source: `ui/piku/PikuPalette.kt`, `PikuUi` in `ui/piku/PikuChatChrome.kt`, `MainActivity.kt` init.

## Hidden (not deleted) in Step 2

- Files, terminal, browser and notes entries (chat header shortcuts, switcher, settings).
- Floating app switcher widget and its Settings card.
- Jarvis UI/overlay/shortcuts and voice call / mic / live dictation UI.
- Manifest: `RECORD_AUDIO`, `FOREGROUND_SERVICE_MICROPHONE`, voice/Jarvis services, tiles and shortcut activities.

Toggles live in `UiToggles.kt` (`FLOATING_SWITCHER`, `JARVIS`, `VOICE`); set one to `true` to bring its UI back
(manifest entries would need restoring too). Chat streaming is unaffected.
