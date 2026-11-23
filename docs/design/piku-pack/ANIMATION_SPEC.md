# Piku animation spec
Sprites: piku_idle, piku_blink, piku_dizzy, piku_love, piku_sleep, piku_happy (VectorDrawable). Render crisp, no filtering/blur.
1. Idle: piku_idle with small up-down bob (about 3dp, 1.2s loop). Every 3-5 s a blink: piku_blink 120ms.
2. Draggable anywhere; on release a small settle bounce.
3. Edge dock: released within 24dp of left/right edge snaps there, half hidden, with a small chevron handle; tap handle to bring back.
4. Tap/long-press menu pill: Dance, Nap, Hide.
   Dance: piku_happy and piku_love alternate every 300ms, tilt left/right 8 degrees, bubble "Let's dance!" 2s.
   Nap: piku_sleep, slow bob, rising fading "Zzz"; tap to wake: piku_idle + bubble "Hey there!".
   Hide: dock to nearest edge.
5. Shake or fast drag: piku_dizzy, wobble, bubble "Woah.. dizzy..." 2s, then idle.
6. Bubble: white rounded pill, 1dp dark outline, small tail, plain text, auto-hide 2s.
7. Reactions: agent working = faster bob; task done = piku_happy; error = piku_dizzy; idle 60s = piku_sleep.
8. If system animation scale is 0: static piku_idle only.
9. Setting "Show mascot" (default on). Never cover the Stop button or the input box.
Rule: existing physics/drag/fling/menu code stays; only replace art and add what is missing.
