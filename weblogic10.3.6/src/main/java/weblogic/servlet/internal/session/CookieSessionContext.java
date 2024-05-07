package weblogic.servlet.internal.session;

import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import weblogic.logging.Loggable;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;

public final class CookieSessionContext extends SessionContext {
   private final String cookieName;

   public CookieSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
      this.cookieName = this.configMgr.getCookiePersistentStoreCookieName();
   }

   protected void initializeInvalidator() {
   }

   public String getPersistentStoreType() {
      return "cookie";
   }

   String getWLCookieName() {
      return this.cookieName;
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      if (var3 == null) {
         return null;
      } else {
         var3.removeCookie(this.cookieName, "/");
         CookieSessionData var4 = new CookieSessionData(var1, this, var2, var3);
         SessionData.checkSpecial(var2, var4);
         this.incrementOpenSessionsCount();
         return var4;
      }
   }

   public SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      if (var2 != null && var3 != null) {
         var1 = RSID.getID(var1);
         Cookie var4 = this.getCookie(var2);
         if (var4 != null && var4.getValue() != null && var4.getValue().length() >= 4 && var4.getValue().indexOf("|") != -1) {
            try {
               CookieSessionData var5 = new CookieSessionData(var1, this, var2, var3, var4);
               return var2 != null && var3 != null && !var5.isValidForceCheck() ? null : var5;
            } catch (Exception var6) {
               var4.setValue("");
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public String[] getIdsInternal() {
      return new String[0];
   }

   void unregisterExpiredSessions(ArrayList var1) {
   }

   boolean invalidateSession(SessionData var1, boolean var2, boolean var3) {
      var1.remove(var3);
      this.decrementOpenSessionsCount();
      SessionData.invalidateProcessedSession(var1);
      return true;
   }

   public void destroy(boolean var1) {
   }

   public void sync(HttpSession var1) {
   }

   public Cookie getCookie(ServletRequestImpl var1) {
      Cookie[] var2 = var1.getCookies();
      if (var2 == null) {
         return null;
      } else {
         for(int var3 = var2.length - 1; var3 > -1; --var3) {
            if (this.cookieName.equals(var2[var3].getName())) {
               if (this.isDebugEnabled()) {
                  Loggable var4 = HTTPSessionLogger.logFoundWLCookieLoggable(this.cookieName, var2[var3].getValue());
                  DEBUG_SESSIONS.debug(var4.getMessage());
               }

               return var2[var3];
            }
         }

         return null;
      }
   }

   public int getNonPersistedSessionCount() {
      return 0;
   }

   public int getCurrOpenSessionsCount() {
      return 0;
   }
}
