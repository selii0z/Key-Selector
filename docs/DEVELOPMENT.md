# Implementation status

This branch is **initial source code**, not a compiled or tested release.

## What exists
- NeoForge 1.21.1 ModDevGradle source project.
- KeyMapping conflict discovery across assigned keys.
- Early key/mouse event interception with Mixins.
- Basic transparent radial selection UI; mouse click / 300ms hold.
- Initial click dispatch.

## Known issues before first playable build
- Build not run; mapping/Java method signatures may need fixes.
- Current selection dispatch calls KeyMapping.click(key), which queues a click by physical key and may trigger *all* mappings on that key. Must replace with per-mapping dispatch before release.
- Priority/reordering/hiding, settings entry points, persistence, radial sectors, and compatibility tests are not yet implemented.
- Vanilla continuous actions and direct GLFW mod handlers are not replayed safely.
- For prolonged hold, the vanilla key release may occur while a GUI is open and requires real-game test.
- The Gradle wrapper JAR is binary and is not included in this branch; generate it with a Gradle 9.2.1 installation or import official MDK wrapper before using gradlew.

Do **not** distribute this as 1.0.0 until replay isolation and testing pass.
