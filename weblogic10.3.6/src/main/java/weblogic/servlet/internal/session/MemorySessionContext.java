package weblogic.servlet.internal.session;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;

public final class MemorySessionContext extends SessionContext {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MemorySessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      this.checkSessionCount();
      MemorySessionData var4 = new MemorySessionData(var1, this, true);
      SessionData.checkSpecial(var2, var4);
      var4.incrementActiveRequestCount();
      this.addSession(var4.id, var4);
      this.incrementOpenSessionsCount();
      var4.setMonitoringId();
      return var4;
   }

   public String getPersistentStoreType() {
      return "memory";
   }

   protected void invalidateSecondarySessions() {
   }

   void unregisterExpiredSessions(ArrayList var1) {
   }

   public SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      var1 = RSID.getID(var1);
      MemorySessionData var4 = (MemorySessionData)this.getOpenSession(var1);
      if (var4 == null) {
         return null;
      } else {
         synchronized(var4) {
            if (var2 != null && var3 != null) {
               if (!var4.isValidForceCheck()) {
                  return null;
               }

               var4.incrementActiveRequestCount();
            }

            return var4;
         }
      }
   }

   public int getCurrOpenSessionsCount() {
      return this.getOpenSessions().size();
   }

   boolean invalidateSession(SessionData var1, boolean var2, boolean var3) {
      this.removeSession(var1.id);
      var1.remove(var3);
      this.decrementOpenSessionsCount();
      SessionData.invalidateProcessedSession(var1);
      return true;
   }

   public void destroy(boolean var1) {
      super.destroy(var1);
      if (!this.configMgr.isSaveSessionsOnRedeployEnabled()) {
         String[] var2 = this.getIdsInternal();
         SessionCleanupAction var3 = new SessionCleanupAction(var2);
         Throwable var4 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, SubjectUtils.getAnonymousSubject(), var3);
         if (var4 != null) {
            HTTPSessionLogger.logUnexpectedErrorCleaningUpSessions(this.getServletContext().getLogContext(), var4);
         }

      }
   }

   public void sync(HttpSession var1) {
      MemorySessionData var2 = (MemorySessionData)var1;
      var2.decrementActiveRequestCount();
      var2.syncSession();
   }

   public int getNonPersistedSessionCount() {
      return this.getOpenSessions().size();
   }

   class SessionCleanupAction implements PrivilegedAction {
      private final String[] ids;

      SessionCleanupAction(String[] var2) {
         this.ids = var2;
      }

      public Object run() {
         try {
            for(int var1 = 0; var1 < this.ids.length && this.ids[var1] != null; ++var1) {
               SessionData var2 = MemorySessionContext.this.getSessionInternal(this.ids[var1], (ServletRequestImpl)null, (ServletResponseImpl)null);
               MemorySessionContext.this.invalidateSession(var2, false);
            }

            return null;
         } catch (Throwable var3) {
            return var3;
         }
      }
   }
}
