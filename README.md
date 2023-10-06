# Plugineer
`Currently working in progress....`        
Plugineer aims to simplify the process of creating Spigot plugins by eliminating boilerplate code and adding practical methods.  

## Who is this for?
Whether you are **a skilled Spigot plugin programmer** or **a java novice**, Plugineer can always help you in some way.

## What does it do?
Features:
1. [x] Easier commands
2. [x] Miscellaneous functionalities...

TODO:
1. [ ] Easier configuration
2. [ ] Easier reflection
3. [ ] Easier I18N

##  How do you add it?
Maven:
```xml
<repositories>
    <repository>
        <id>sonatype</id>
        <url>https://oss.sonatype.org/content/groups/public/</url>
    </repository>
</repositories>
    
<dependencies>
    <dependency>
        <groupId>top.shjibi</groupId>
        <artifactId>plugineer</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```groovy
repositories {
    repositories {
        jcenter()
        maven {
            name 'sonatype'
            url 'https://oss.sonatype.org/content/groups/public/'
        }
    }}

dependencies {
    implementation: 'top.shjibi:plugineer:2.0.0-SNAPSHOT'
}
```
Also, you might want to put this dependency into your final build, this is different for Maven and Gradle, you can search it up yourself.

## Special thanks
[JetBrains](https://www.jetbrains.com): For creating amazing IDEs like Intellij IDEA and supporting the open source community. <br><img src="https://www.jetbrains.com/company/brand/img/jetbrains_logo.png" alt="JetBrains" style="width:100px;"/>

[SpigotMC](https://www.spigotmc.org/): For contributing such a wonderful project for the Minecraft community :3  <br><img src="https://static.spigotmc.org/img/spigot.png" alt="SpigotMC" style="width:100px;"/>
