module easytext.gui {
   // JavaFX needs to instantiate our Main Application class
   exports javamodularity.easytext.gui to javafx.graphics;

   requires javafx.graphics;
   requires javafx.controls;
   requires easytext.analysis.api;

   uses javamodularity.easytext.analysis.api.Analyzer;
}
