capsule-maven-plugin
====================

maven plugin to build java capsules.

Visit this page for more info on [capsule](https://github.com/puniverse/capsule).

## Building from source
to build the project locally you will need to clone the git repo and install the plugin in your local repository

```
git clone https://github.com/pguedes/capsule-maven-plugin.git
cd capsule-maven-plugin
mvn install
```

after this, the project is installed in your local repository and available to build your projects with

## Using the plugin in your project
to build a capsule for your project, add the following to the build section of your pom.xml

```
<project>
...
  <build>
		...
    <plugins>
      ...
      <plugin>
        <groupId>com.github.pguedes</groupId>
        <artifactId>capsule-maven-plugin</artifactId>
        <version>0.3</version>
        <configuration>
          <mainClass>com.github.pguedes.App</mainClass>
        </configuration>
      </plugin>
      ...
    </plugins>
		...
  </build>
...
</project>
```

you should then be able to build a capsule for your project by running:  
```mvn package capsule:capsule```

you need to run the package phase because the capsule target needs a jar from the main project phase. Alternatively you can run:  
```mvn jar:jar capsule:capsule```


### building attached to a phase
you can also attach the plugin goal to a maven lifecycle phase with the ```<executions>``` part of the pom

```
<project>
...
  <build>
		...
    <plugins>
      ...
      <plugin>
        <groupId>com.github.pguedes</groupId>
        <artifactId>capsule-maven-plugin</artifactId>
        <version>0.3</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>capsule</goal>
            </goals>
            <configuration>
              <mainClass>com.github.pguedes.App</mainClass>
            </configuration>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
		...
  </build>
...
</project>
```

you should then be able to build a capsule for your project by running:  

```mvn package```

## specify a capsule version to use
In case you need to, you can specify which version of the capsule class should be downloaded from a maven repo to be used in your capsule jar.
I required this feature while doing deployments to openBSD machines which was not working in the latest version of capsule - https://github.com/puniverse/capsule/issues/63

```
<project>
...
  <build>
		...
    <plugins>
      ...
      <plugin>
        <groupId>com.github.pguedes</groupId>
        <artifactId>capsule-maven-plugin</artifactId>
        <version>0.3</version>
        <configuration>
          <mainClass>com.github.pguedes.App</mainClass>
          <capsuleVersion>0.10.0</capsuleVersion>
        </configuration>
      </plugin>
      ...
    </plugins>
		...
  </build>
...
</project>
```

if you do not need this, you can skip this configuration and the plugin will find the latest version deployed in maven repositories available and use that.

## License
This project is licensed under the **GNU GPL v3**.  

Read [the license](http://www.gnu.org/licenses/gpl.txt) or the [summarized version](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3)) .
