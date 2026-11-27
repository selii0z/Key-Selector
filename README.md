# Key Selector

Client-side Minecraft 1.21.1 / NeoForge mod to resolve shared keybindings with a lightweight radial menu.

## Status

Work in progress: source scaffold, detection and radial selection are being implemented. Not a tested release.

## Goal

When multiple standard `KeyMapping` actions use the same keyboard key, show a selection wheel rather than triggering them all. Short press opens a clickable wheel, long press selects on release. User preferences will allow priority, reordering and hiding actions.

## Known limitations

Some mods bypass Minecraft's KeyMapping system and handle GLFW input directly. Those are not automatically compatible. Injecting into input dispatch also needs verification on a real NeoForge client.

## Build

Java 21, Minecraft 1.21.1, NeoForge 21.1.x. Build with `./gradlew build` (on Windows `gradlew.bat build`), once the Gradle wrapper is present.
