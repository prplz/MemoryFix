MemoryFix is a mod for Forge 1.8.9 that aims to fix known memory issues.

Which issues does it fix?
------
- **Optifine cape leak**: This is the main memory leak, anyone who has played minigames for a few hours would have experienced it. More information: https://github.com/sp614x/optifine/issues/526.
- **Oversized resource pack icons**: Some users download hundreds of resource packs and some resource pack makers (looking at you Apexay) put huge icons in their resource packs. This adds up and can use hundreds of megabytes of memory for some users.
- **System.gc() on world change**: Causes world changes to take a few seconds. The same effect can also be achieved by adding the JVM argument `-XX:-DisableExplicitGC`.

Special thanks
------
![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/)
and [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/),
innovative and intelligent tools for profiling Java and .NET applications.

Without the use of YourKit, this project would not have been possible.
