# Demo for presenting errors if you use not standard java modules (ee modules, eg. java.xml.bind)

**it's better check your code using jdeps tool**

> `jdeps Main.class`

**if we use not standard types (eg. types in modules such as java.xml.bind, java.transaction, etc.), we can compile and run using this commands**

> `javac --add-modules java.xml.bind Main.java`

> `java --add-modules java.xml.bind Main`
