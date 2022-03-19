
# hCore

A simplified and multi-functional tool for spigot developers. There are dozens of features you can use in 
it, and it is completely open source code. hCore supports all versions from 1.8.x to 1.18.2. 

## Developers

- [@hakan-krgn](https://github.com/hakan-krgn) Discord: Hakan Kargın#7515
- [@furkanbalci0](https://github.com/furkanbalci0) Discord: caandalek#4917
- [@bilektugrul](https://github.com/bilektugrul) Discord: Breakthrough#1006
- [@rin-17](https://github.com/rin-17) Discord: RIN#8198

##Wiki
**[CLICK](https://github.com/hakan-krgn/hCore/wiki)**

##Features
- [`Command`](https://github.com/hakan-krgn/hCore/wiki/command:-Command) - Basic command system to register commands without plugin.yml

## How to add it to Maven or Gradle?
[![](https://jitpack.io/v/hakan-krgn/hCore.svg)](https://jitpack.io/#hakan-krgn/hCore)

###Maven
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

###Gradle
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
