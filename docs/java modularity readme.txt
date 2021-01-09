link al corso https://app.pluralsight.com/library/courses/java-9-modularity-first-look

Introduzione
	sistema modulare consente organizzare meglio le relazioni tra vari moduli (packages)
	al posto di class-path viene usato il termine module-path
	sitema modulare introdotta in java9 ha comportato alla sudiviosne del vecchio module jr.jar in piu' moduli (es. java.base, java.sql, java.logging etc.)
	sistema modulare si basa su tre pilastri
		- incapsulamento 
		- definizione di una interfaccia chiara
		- esplicita dichiarazione di moduli da quali dipendiamo e i package che esportiamo
	NOTA: il modulo viene dichiarato nel file module-info.java, definendo due sezioni
		1 - defiinizione packages che vogliamo rendere visibili all'esterno (interfaccia del nostro modulo)
		2 - definizione moduli dai quali dipendiamo 
	demo
		esempio creazione un semplice modulo
		compilazione del modulo: javac --module-source-path src -d out .\src\easytext\javamodularity\easytext\Main.java
		esecuzione del main: c:\dev\tools\jdk-12.0.1\bin\java --module-path out -m easytext/javamodularity.easytext.Main
Sviluppare piu' moduli
	demo, definizione due moduli, dove uno esporta fuori il proprio package, e l'altro importa il modulo dipendente
	nel momento di avvio del'app viene controllato se tutti i moduli dalle quali dipendiamo sono presenti
	risoluzione dei moduli
		nel momento di avvio dell'app viene analizzato il module-info.class (descrittore del modulo) per verificare se abbiamo tutte le dipendenze per runtime
		(analisi a compile time)
		NOTA: il modulo java.base e' referenziato da tutti i moduli in modo implicito!
	leggibilita', come accediamo ai moduli esterni
		il modulo che importa un'altro modulo ha accesso solo ai tipi public esportati
		NOTA: i tipi public non esportate esplicitamente non possano essere acceduti da altri moduli
		prima di usare le classi di un'altro modulo dobbiamo porsi tre domande:
			1. stiamo importando il modulo di interesse?
			2. Il modulo di interesse esporta i package richiesti?
			3. I tipi nei packages esportati hanno la visibilita' public?
		NOTA: i tipi private e protected NON sono visibili al di fuori del modulo
		i moduli non supportano la visibilita' transitiva, cioe', se il modulo x legge il modulo y che a sua volta legge il modulo z, il modulo x NON puo' accedere al modulo z,
			senza una importazione esplicita a livello di module-info.java
	leggibilita' transitiva
		in certi casi viene comodo avere la leggibilita' transitiva, per es. java.sql dipende da java.logging, e un metodo public di java.sql ritorna il tipo di java.logging, 
		se la nostra app usa tale metodo dovrebbe in teoria definire il require anche del modulo java.logging. In alternativa, se modulo java.sql esegue l'import di 
		java.logging come "require transitive", anche la nostra app che esegue "require java.sql" ha accesso diretto a tutti i tipi public di java.logging
		possiamo creare anche i cosi detti moduli AGGREGATORI, dove viene creato un modulo senza codice con solo il module-info.java contenente "require transitive"
		di moduli concreti. Questo semplifica organizzazione delle nostre librerie e esposizione all'esterno delle funzionalita'.
	nuovo modulo per la nostra demo app - modulo della GUI
	platform modules (90+ moduli portati fuori con Java 9)
		Core Java SE: java.base, java.desktop, java.sql, java.se
		Enterprise: java.corba, java.xml.ws, java.transaction
		Java FX: javafx.base, javafx.web, javafx.controls (e' nuovo modo per fare la UI)
		JDK: jdk.compiler, jdk.httpserver, jdk.jartool
		NOTA: comando "java --list-modules" puo' essere usato per stampare eleno di moduli presenti in JDK, agiungendo il nome del modulo al comando stampiamo i dettagli
		del module-descriptor
		"exports <module name> to <dedicated module name", esportazione qualificata, solo il modulo predente in "to" puo' eseguire il require di questo modulo
			questo comando puo' essere usato per esempio nei casi dove il nostro codice viene analizzato da altri moduli usando la reflection
Esporre e consumare i servizi
	il concetto di servizi aiuta a esporre i servizi e non singoli package, e' un ragruppamento in modo da non esporre fuori tanti package..
	i servizi aiutano a rendere il nostro modulo piu' facilmente estendibile
	concetto di servizi nel sistema modulare di java
		viene introdotto un layer in piu' tra il service consumer e service provider - service catalog/registry
		service provider registra l'interfaccia e implementazione nel registry
		service consumer richiede al service registry l'implementazione di una specifica interfaccia 
		NOTA: e' il service registry che crea una nuova istanza del servizio richiesto
	a livello di module descriptor (module-info.java) abbiamo tre moduli "in gioco"
		- modulo che contiene l'interfaccia del servizio
			interfaccia esportata nel descrittore
		- modulo che contiene il provider, chi implementa l'interfaccia
			importa l'interfaccia, implementa interfaccia
			esporta l'implementazione usando la sintassi "provides Impl with Interface" - NOTA: implementazione e' visibile solo al service registry, nessun altro modulo
			puo' richiedere direttamente l'implementazione
		- modulo che contiene il consumer, chi usa l'implementazione dell'interfaccia
			richiede l'interfaccia 
			richiede imlpementazione dell'interfaccia al service registry usando il comando "uses <interface package>"
			il codice del consumatore richiede al service registry l'istanza del servizio, es.
				Iterable<MyService> services = ServiceLoader.load(MyService.class)
	il concetto di servizio disacoppia il consumatore e produttore
	NOTA: avendo la dipendenza dall'interfaccia, il consumer non va in errore se per qualche motivo non trova l'implementazione (es. il provider non ha eseguito la registrazione
		nel service registry)
	recap: il provider incapsula l'implementazione concreta, il consumer viene disacoppiato dal provider.
Linking modules
	nuova fase di sviluppo - linking, uso del tool jlink
	linking e' un modulo facoltativo intermedio tra il compilatore e runtime, creato per i moduli in modo da eseguire i collegamenti tra i moduli in modo opportuno
	prima di eseguire la nostra app a runtime.
	linking serve a 
		- creare custom runtime image (viene eseguita l'analisi di tutti file module-info determinando tutti i moduli che servono all'app per essere eseguita in modalita'
			stand alone), e' l'immagine con tutti i moduli necessari (sia custom che della piattaforma java). 
		- l'immagine e' ridotta confronto precedente app+jdk, migliora le performance
		- ottimizzazione dell'intero programma, eliminando per esempio "il codice morto" non usato da nessuna parte del programma (quindi non viene caricato a runtime)
	e' stato creato il tool JLINK usato per creare immagini di runtime custom
		e' un tool plugin based, e' possibile estenderlo implementando nuove ottimizzazioni
		es. eseguendo jlink passando il nostro modulo easytext.cli, jlink scansiona il suo descrittore, individua il modulo easytext.analysis + il modulo referenziato 
			implicitamente, java.base, e crea l'immagine di runtime che contiene solo questi tre moduli - ottimizzazione spazio disco e tempo di startup dell'app 
			(jvm deve caricare solo questi tre moduli e non tutto il JDK come prima!)
	demo, utilizzo di jlink
		jlink --module-path jars/ --add-modules easytext.cli --strip-debug --compress=2 --output image
			per linkare piu' moduli
		image/bin/java --list-modules
			per stampare la lista di moduli dell'immagine
Preparare il progetto per Java9
	il concetto di classpath rimane
	il concetto di modulo e' facoltativo, possiamo non creare module-info NOTA: pero consideriamo i benefici che otteniamo usandoli
	possiamo compilare in questo modo
		javac -cp $CLASSPATH ...
		java -cp $CLASSPATH ...
	indipendentemente dal fatto se usiamo i moduli o no, java 9 prevede il concetto di modulo 
		per poter compilare con versioni nuove di java, senza usare i moduli, lanciamo i comandi necessari
		java crea per la nostra app un modulo di default "unnamed" - modulo contenente tutto il classpath
		modulo 'unnamed' esporta tutto e puo' leggere tutti i moduli di jdk
	nella migrazione a java9 dobbiamo verificare se non stiamo utilizzando i tipi che sono stati incapsulati e non sono piu' accessibili dall'esterno
	NOTA: se non usiamo i moduli, con java9 cmq JDK e' stato diviso in moduli e li stiamo usando in modo trasparente per nostro codice
		praticamente tutto il nostro classpath finisce nel modulo 'unnamed'
	per verificare incongruenze del nostro codice con nuove versioni java possiamo usare il tool jdeps introdotto con java8
	se non siamo in grado di cambiare il codice sorgente (es. usiamo librerie di terzi che non hanno nuove versioni per java9), c'e' il workaround:
		nella fase di compilazione scriviamo
			javac --add-exports java.base/sun.security.x509=ALL-UNNAMED Main.java
		nella fase di esecuzione scriviamo
			java --add-exports java.base/sun.security.x509=ALL-UNNAMED Main
	se usiamo i tipi non di default del modulo JavaSE (es. i tipi che con java9 non sono piu' disponibili di default, essendoci migrati nei moduli nuovi):
		sono i tipi che sono fuori al modulo java.se (es. i tipi spostati nel modulo java.xml.ws, e altri moduli che sono piu' enterprise edition)
		la solution e' usare il flag --add-modules, es.
		javac --add-modules java.xml.bind Main.java
		idem per esecuzione
		java --add-modules java.xml.bind Main
		NOTA: e' sempre meglio usare jdeps per verificare questo tipi di errori
	modularizzare il nostro codice
		molto prababilmente ci troveremo di fronte alla situazione quando non abbiamo le versioni modulari di tutte nostre dipendenze
		per es. se creiamo il modulo per la nostra app che nel codice referenzia i tipi di una libreria presente nel classpath, questo NON e' permesso, 
			il modulo accede solo ai tipi di altri moduli, altrimenti ritorniamo al problema di prima, dove tutti tipi nel classpath vedono tutto.
		se libreria che usiamo non e' piu' manutenuta, e non esiste la versione modulare, dobbiamo rivolgersi al concetto di AUTOMATIC MODULES di java9
			consente di aggiungere il jar non modulare al module path
			in questo modo il jar diventa un modulo - il nome del modulo deriva dal nome del jar, e noi referenziamo questo modulo nel nostro module 
			descriptor (module-info.java)
			automatic module esporta tutti i suoi packages! quindi NON incapsula niente!
			
		