package weblogic.diagnostics.harvester;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.internal.WLDFHarvesterImpl;
import weblogic.diagnostics.utils.SecurityHelper;

public class WLDFHarvesterManager implements WLDFHarvesterLauncher {
   private static final DebugLogger DBG = DebugLogger.getDebugLogger("DebugDiagnosticsHarvester");
   private static WLDFHarvesterImpl wldfHarvesterSingleton;
   private static WLDFHarvesterManager mgr = new WLDFHarvesterManager();
   LifecycleState state;

   private WLDFHarvesterManager() {
      this.state = WLDFHarvesterManager.LifecycleState.UNPREPARED;
   }

   public static WLDFHarvesterLauncher getInstance() {
      SecurityHelper.checkForAdminRole();
      return mgr;
   }

   public WLDFHarvester getHarvesterSingleton() {
      SecurityHelper.checkForAdminRole();
      synchronized(this) {
         if (wldfHarvesterSingleton == null) {
            wldfHarvesterSingleton = WLDFHarvesterImpl.getInstance();
            wldfHarvesterSingleton.prepare();
            wldfHarvesterSingleton.activate();
         }
      }

      return wldfHarvesterSingleton;
   }

   public void prepare() {
      if (DBG.isDebugEnabled()) {
         DBG.debug("In WLDFHarvesterManager.prepare");
      }

      this.state = WLDFHarvesterManager.LifecycleState.PREPARED;
   }

   public void activate() {
      if (this.state.compareTo(WLDFHarvesterManager.LifecycleState.ACTIVATED) < 0) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("In WLDFHarvesterManager.activate()");
         }

         this.state = WLDFHarvesterManager.LifecycleState.ACTIVATED;
      }

   }

   public void deactivate() {
      if (this.state.compareTo(WLDFHarvesterManager.LifecycleState.PREPARED) > 0) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("In WLDFHarvesterManager.deactivate()");
         }

         this.state = WLDFHarvesterManager.LifecycleState.PREPARED;
      }

   }

   public void unprepare() {
      if (this.state.compareTo(WLDFHarvesterManager.LifecycleState.UNPREPARED) > 0) {
         if (DBG.isDebugEnabled()) {
            DBG.debug("In WLDFHarvesterManager.unprepare");
         }

         this.state = WLDFHarvesterManager.LifecycleState.UNPREPARED;
      }

   }

   static enum LifecycleState {
      UNPREPARED,
      PREPARED,
      ACTIVATED;
   }
}
