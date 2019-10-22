# Demo for presenting java modularity introduced in Java 9

**Command to compile source code**

> `javac --module-source-path src -d out <your *.java>`

**Command to create jars of your modules (using linux terminal)**

> rm -rf jars && mkdir -p jars
> jar --create --file jars/easytext.analysis.api.jar -C out/easytext.analysis.api .
> jar --create --file jars/easytext.analysis.coleman.jar -C out/easytext.analysis.coleman .
> jar --create --file jars/easytext.analysis.kincaid.jar -C out/easytext.analysis.kincaid .
> jar --create --file jars/easytext.cli.jar --main-class=javamodularity.easytext.cli.Main -C out/easytext.cli .

**Command to link java modules using jlink tool (using linux terminal)**

> rm -rf image && \
> jlink --module-path jars/:\$JAVA_HOME/jmods --add-modules easytext.cli --strip-debug --compress=2 --output=image

**Command to execute your module**

> `java --module-path out -m <your module name>/<FQN of module's main class>`
