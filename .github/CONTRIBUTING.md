# How to Contribute
Issues are used for bug reporting and feature requests and ONLY for these two things, where as Pull requests are expected to be very specific.
We, the authors try to help and fix bugs as fast as we can, but we need to understand the things you want to tell us.

## As such, a bug report should always contain:
### A brief description of the bug in the issue title
Example: AntiLaby not updating
### Steps to reproduce the issue
Example:
1. Set option foo.bar.geeHelp: true
2. Wait for a LabyPlayer to join the server
3. ...
### If the Console prints an error, A picture or paste of the whole error message
Example:
java.io.IOException: Could not find file bar.yml
at com.github.antilaby.antilaby.main.AntiLaby.getBarConfig(line: 1782)...
### AntiLaby version, MC server type (CraftBukkit, Spigot & PaperSpigot are supported), CraftBukkit and Minecraft version
Example:
AntiLaby v.pre-2.0, Spigot version git-Spigot-549c1fa-8c37e2e, MC 1.12.2, CraftBukkit 1.12.2-R0.1-SNAPSHOT
## A Feature/API Request should always contain:
### A brief description of the feature request in the issue title
Example:
Add Support for plugin Bar
### A detailed description of what you want to achieve and why this is necessary 
Example:
I want my plugin to hook in your ... system. This currently is not possible without reflection...
### Your AntiLaby version
Example:
AntiLaby v.pre-2.0

## A pull request looks like a feature request with two things in mind: 
1. All code is described with javadoc, and, where helpful, with comments
2. The codes aim is stated in a proper way ("Added a hook" is not enough!)
3. The same applies to commit messages

**If we ask you to change your code, you *do so*!**