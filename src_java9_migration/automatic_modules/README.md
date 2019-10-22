# Demo on how we can define automatic modules for not modularized jars

**compile using automatic modules**

> `javac --module-path lib --module-source-path src -d out .\src\application\javamodularity\automaticmods\Main.java`

**run using automatica modules**

> `java --module-path out -m application/javamodularity.automaticmods.Main`
