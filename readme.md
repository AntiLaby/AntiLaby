# Welcome to AntiLaby Rework!
AntiLaby is a free open source [Minecraft](https://minecraft.net/) [Bukkit](https://bukkit.org/) plug-in by [NathanNr](https://github.com/NathanNr/) and [heisluft](https://github.com/heisluft) to disable functions of the [LabyMod](https://www.labymod.net/).

## We now have a maven repo!!!
### In Maven:
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
### In Gradle:
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

Useful links:
* [Getting started](https://github.com/AntiLaby/AntiLaby/wiki/Getting-started)
* [Find the plugin on SpigotMC](https://www.spigotmc.org/resources/antilaby-1-8-1-12-2-disable-labymod-functions-api.21347/)
* [About AntiLaby](https://github.com/AntiLaby/AntiLaby/wiki/About)
* [Wiki](https://github.com/AntiLaby/AntiLaby/wiki)
* [Ask questions, send suggestions and report bugs](https://github.com/AntiLaby/AntiLaby/wiki/Support)
* [Source code](https://github.com/AntiLaby/AntiLaby)