# Demo for presenting errors if you use encapsulated in java9 jdk classes

**compile with java8 to see java warning**

**compile with java9 to see java error**

> `javac Main.java`

**run compiled with java8 class file using java9**

> `java -cp . Main`

**use jdeps tool to verify your dependencies (jdeps is present starting from java8, but it's better to use the version published with java9)**

> `jdeps -jdkinternals .\Main.class`

**if we cannot change the source code, we can compile and run using this commands**

> `javac --add-exports java.base/sun.security.x509=ALL-UNNAMED Main.java`

> `java --add-exports java.base/sun.security.x509=ALL-UNNAMED Main`
