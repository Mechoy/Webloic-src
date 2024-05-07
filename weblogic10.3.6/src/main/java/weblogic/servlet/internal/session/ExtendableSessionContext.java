package weblogic.servlet.internal.session;

import java.util.ArrayList;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.WebAppServletContext;

public abstract class ExtendableSessionContext extends SessionContext {
   public ExtendableSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   protected void postCreateSession(ServletRequestImpl var1, SessionData var2) {
      SessionData.checkSpecial(var1, var2);
      var2.setMonitoringId();
   }

   public void incrementSessionCount(SessionData var1) {
      var1.incrementActiveRequestCount();
      this.incrementOpenSessionsCount();
   }

   public void decrementSessionCount(SessionData var1) {
      var1.decrementActiveRequestCount();
      this.decrementOpenSessionsCount();
   }

   protected void syncSession(SessionData var1) {
   }

   public boolean invalidateSessionFromContext(SessionData var1, boolean var2) {
      this.servletContext.getServer().getSessionLogin().unregister(var1.id, var1.getContextPath());
      var1.unregisterRuntimeMBean();
      SessionData.invalidateProcessedSession(var1);
      return true;
   }

   boolean invalidateSession(SessionData var1, boolean var2, boolean var3) {
      return false;
   }

   protected void initializeInvalidator() {
   }

   void unregisterExpiredSessions(ArrayList var1) {
   }
}
