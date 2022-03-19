# hCore

A simplified and multi-functional tool for spigot developers. There are dozens of features you can use in it, and it is
completely open source code. hCore supports all versions from 1.8.x to 1.18.2.

## Developers

- [@hakan-krgn](https://github.com/hakan-krgn) Discord: Hakan Kargın#7515
- [@furkanbalci0](https://github.com/furkanbalci0) Discord: caandalek#4917
- [@bilektugrul](https://github.com/bilektugrul) Discord: Breakthrough#1006
- [@rin-17](https://github.com/rin-17) Discord: RIN#8198

## Wiki

**[CLICK](https://github.com/hakan-krgn/hCore/wiki)**

## Features

- [`Command`](https://github.com/hakan-krgn/hCore/wiki/command) - Basic command system for registering commands without
  plugin.yml.
- [`Database`](https://github.com/hakan-krgn/hCore/wiki/database) - Database implementation system for multi-database
  support.
- [`Hologram`](https://github.com/hakan-krgn/hCore/wiki/hologram) - hologram system for creating and managing
  client-side holograms.
- [`Message`](https://github.com/hakan-krgn/hCore/wiki/message) - Message system to send title, action-bar or boss-bar
  to player.
- [`Packet`](https://github.com/hakan-krgn/hCore/wiki/packet) - Packet system to listen to the packet and send it to the
  player.
- [`Particle`](https://github.com/hakan-krgn/hCore/wiki/particle) - Particle system to play particle effects client-side
  for any player.
- [`Renderer`](https://github.com/hakan-krgn/hCore/wiki/renderer) - Renderer system for rendering and sending the
  package to the closest players.
- [`Scheduler`](https://github.com/hakan-krgn/hCore/wiki/scheduler) - Scheduler system to easily create new scheduler.
- [`Inventory`](https://github.com/hakan-krgn/hCore/wiki/inventory) - Inventory system for creating and opening special
  inventories for players.
- [`Sign`](https://github.com/hakan-krgn/hCore/wiki/sign) - Sign system for receiving input from the player.
- [`WorldBorder`](https://github.com/hakan-krgn/hCore/wiki/worldborder) - WorldBorder system to display WorldBorder
  client-side.

## How to add it to Maven or Gradle?

[![](https://jitpack.io/v/hakan-krgn/hCore.svg)](https://jitpack.io/#hakan-krgn/hCore)

### Maven

``` xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.hakan-krgn.hCore</groupId>
    <artifactId>bukkit</artifactId>
    <version>0.0.2</version>
</dependency>
```

### Gradle

``` xml
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.hakan-krgn.hCore:bukkit:0.0.2'
}
```
