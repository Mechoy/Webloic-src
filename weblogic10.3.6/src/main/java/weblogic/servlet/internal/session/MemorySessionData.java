package weblogic.servlet.internal.session;

public final class MemorySessionData extends SessionData {
   public MemorySessionData() {
   }

   public MemorySessionData(String var1, SessionContext var2, boolean var3) {
      super(var1, var2, var3);
      if (var3) {
         this.getWebAppServletContext().getEventsManager().notifySessionLifetimeEvent(this, true);
      }

   }

   protected void logTransientAttributeError(String var1) {
      if (this.getContext().getConfigMgr().isSaveSessionsOnRedeployEnabled()) {
         HTTPSessionLogger.logTransientMemoryAttributeError(this.getWebAppServletContext().getLogContext(), var1, this.getId());
      }

   }
}
