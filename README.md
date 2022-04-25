# hCore

A simplified and multi-functional tool for spigot developers. There are dozens of features you can use in it, and it is
completely open source code. hCore supports all versions from 1.8.x to 1.18.2. Also you can find all these APIs usages
from [here](https://github.com/hakan-krgn/hCore/wiki).

## Developers

- [@hakan-krgn](https://github.com/hakan-krgn) Discord: Hakan#2584
- [@furkanbalci0](https://github.com/furkanbalci0) Discord: caandalek#4917
- [@rin-17](https://github.com/rin-17) Discord: RIN#8198
- [@osmanfurkan115](https://github.com/osmanfurkan115)

## Features

- [`Command`](https://github.com/hakan-krgn/hCore/wiki/Command) - Basic command system for registering commands without
  plugin.yml.
- [`Database`](https://github.com/hakan-krgn/hCore/wiki/Database) - Database implementation system for multi-database
  support.
- [`Hologram`](https://github.com/hakan-krgn/hCore/wiki/Hologram) - Hologram system for creating and managing
  client-side holograms.
- [`Message`](https://github.com/hakan-krgn/hCore/wiki/Message) - Message system to send title, action-bar or boss-bar
  to player.
- [`Packet`](https://github.com/hakan-krgn/hCore/wiki/Packet) - Packet system to listen to the packet and send it to the
  player.
- [`Particle`](https://github.com/hakan-krgn/hCore/wiki/Particle) - Particle system to play particle effects client-side
  for any player.
- [`Renderer`](https://github.com/hakan-krgn/hCore/wiki/Renderer) - Renderer system for rendering and sending the
  package to the closest players.
- [`Scheduler`](https://github.com/hakan-krgn/hCore/wiki/Scheduler) - Scheduler system to easily create new scheduler.
- [`Inventory`](https://github.com/hakan-krgn/hCore/wiki/Inventory) - Inventory system for creating and opening special
  inventories for players.
- [`Sign`](https://github.com/hakan-krgn/hCore/wiki/Sign) - Sign system for receiving input from the player.
- [`WorldBorder`](https://github.com/hakan-krgn/hCore/wiki/WorldBorder) - WorldBorder system to display WorldBorder
  client-side.
- [`HItemBuilder`](https://github.com/hakan-krgn/hCore/wiki/HItemBuilder) - HItemBuilder class for creating item stacks
  and
  manage stacks easily.
- [`HYaml`](https://github.com/hakan-krgn/hCore/wiki/HYaml) - Basic yaml system for creating and manage yamls easily.
- [`HSpam`](https://github.com/hakan-krgn/hCore/wiki/HSpam) - Spam system to check if the given ID is spamming.

## How to add it to Maven or Gradle?

### Maven

``` xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.hakan-krgn.hCore</groupId>
    <artifactId>bukkit</artifactId>
    <version>0.2.3</version>
</dependency>


<!--If you want to use this api without libraries, you can use below dependency -->
<dependency>
    <groupId>com.github.hakan-krgn.hCore</groupId>
    <artifactId>bukkit-primary</artifactId>
    <version>0.2.3</version>
</dependency>
```

### Gradle

``` xml
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.hakan-krgn.hCore:bukkit:0.2.3'
}
```
