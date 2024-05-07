package weblogic.diagnostics.harvester;

public interface WLDFHarvesterLauncher {
   WLDFHarvester getHarvesterSingleton();

   void prepare();

   void activate();

   void deactivate();

   void unprepare();
}
