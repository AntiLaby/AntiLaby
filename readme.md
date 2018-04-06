# Welcome to AntiLaby!
AntiLaby is a free open source [Minecraft](https://minecraft.net/) [Bukkit](https://bukkit.org/) plug-in by [NathanNr](https://github.com/NathanNr/) and [heisluft](https://github.com/heisluft) to disable functions of the Minecraft modification [LabyMod](https://www.labymod.net/). [Read more ...](https://github.com/AntiLaby/AntiLaby/wiki/Home)

Useful links:
* [Getting started](https://github.com/AntiLaby/AntiLaby/wiki/Getting-started)
* [Find the plugin on SpigotMC](https://www.spigotmc.org/resources/21347/)
* [About AntiLaby](https://github.com/AntiLaby/AntiLaby/wiki/Home)
* [Ask questions, send suggestions and report bugs](https://github.com/AntiLaby/AntiLaby/wiki/Support)
* [Source code](https://github.com/AntiLaby/AntiLaby)

## AntiLaby in
### Maven:
````xml
...
<repositories>
  ...
  <repository>
    <id>heisluft-repo</id>
    <url>http://heisluft.bplaced.net/maven/</url>
  </repository>
  ...
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>com.gihub.antilaby</groupId>
    <artifactId>AntiLaby</artifactId>
    <version>2.0-pre1</version>
    <scope>compile</scope>
  </dependency>
  ...
</dependencies>
...
````
### Gradle:
````gradle
...
repositories {
  ...
  maven {
    url 'http://heisluft.bplaced.net/maven/'
  }
  ...
}
...
dependencies {
  ...
  compile group: 'com.github.antilaby", name: "AntiLaby", version: "2.0-pre1'
  ...
}
...
````
