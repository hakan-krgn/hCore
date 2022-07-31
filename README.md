# hCore

## What is hCore?

A simplified and multi-functional tool for spigot developers. There are dozens of features you can use in it, and it is
completely open source code. hCore supports all versions from 1.8.x to 1.19.x. Also, you can find all these APIs usages
from [here](https://github.com/hakan-krgn/hCore/wiki).

## JavaDocs

* https://hakan-krgn.github.io/hCore/api/
* https://hakan-krgn.github.io/hCore/proxy-client/
* https://hakan-krgn.github.io/hCore/proxy-server/

## Developers

### Owner:

* [@hakan-krgn](https://github.com/hakan-krgn) Discord: Hakan#2584

### Wiki:

* [@rin-17](https://github.com/rin-17)

### Contributors:

* [@furkanbalci0](https://github.com/furkanbalci0)
* [@osmanfurkan115](https://github.com/osmanfurkan115)
* [@imBuzz](https://github.com/imBuzz)
* [@Alpho320](https://github.com/Alpho320)
* [@rin-17](https://github.com/rin-17)
* [@bilektugrul](https://github.com/bilektugrul)
* [@hamza-cskn](https://github.com/hamza-cskn)

## Features

- [`Annotation Command`](https://github.com/hakan-krgn/hCore/wiki/AnnotationCommand) - Command system with annotation for registering commands without plugin.yml.
- [`NPC`](https://github.com/hakan-krgn/hCore/wiki/NPC) - NPC system for creating and managing client-side NPCs.
- [`Database`](https://github.com/hakan-krgn/hCore/wiki/Database) - Database implementation system for multi-database support.
- [`Hologram`](https://github.com/hakan-krgn/hCore/wiki/Hologram) - Hologram system for creating and managing client-side holograms.
- [`Scoreboard`](https://github.com/hakan-krgn/hCore/wiki/Scoreboard) - Scoreboard system for creating non-flicker scoreboards.
- [`Message`](https://github.com/hakan-krgn/hCore/wiki/Message) - Message system to send title, action-bar or boss-bar to player.
- [`Packet`](https://github.com/hakan-krgn/hCore/wiki/Packet) - Packet system to listen to the packet and send it to the player.
- [`Event Subscriber`](https://github.com/hakan-krgn/hCore/wiki/EventSubscriber) - Event subscribe system to register listeners without any class.
- [`Particle`](https://github.com/hakan-krgn/hCore/wiki/Particle) - Particle system to play particle effects client-side for any player.
- [`Renderer`](https://github.com/hakan-krgn/hCore/wiki/Renderer) - Renderer system for rendering and sending the package to the closest players.
- [`Scheduler`](https://github.com/hakan-krgn/hCore/wiki/Scheduler) - Scheduler system to easily create new scheduler.
- [`Inventory`](https://github.com/hakan-krgn/hCore/wiki/Inventory) - Inventory system for creating and opening special inventories for players.
- [`Anvil`](https://github.com/hakan-krgn/hCore/wiki/Anvil) - Anvil system for receiving input from the player.
- [`Sign`](https://github.com/hakan-krgn/hCore/wiki/Sign) - Sign system for receiving input from the player.
- [`WorldBorder`](https://github.com/hakan-krgn/hCore/wiki/WorldBorder) - WorldBorder system to display WorldBorder client-side.
- [`ItemBuilder`](https://github.com/hakan-krgn/hCore/wiki/ItemBuilder) - HItemBuilder class for creating item stacks and manage stacks easily.
- [`Yaml`](https://github.com/hakan-krgn/hCore/wiki/Yaml) - Basic yaml system for creating and manage YAMLs easily.
- [`Spam`](https://github.com/hakan-krgn/hCore/wiki/Spam) - Spam system to check if the given ID is spamming.

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
    <version>0.5.7</version>
    <scope>provided</scope>
</dependency>


<!--If you want to use this api without libraries, you can use below dependency -->
<dependency>
    <groupId>com.github.hakan-krgn.hCore</groupId>
    <artifactId>bukkit-primary</artifactId>
    <version>0.5.7</version>
    <scope>compile</scope>
</dependency>
```

### Gradle

``` xml
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.hakan-krgn.hCore:bukkit:0.5.7'
}
```
