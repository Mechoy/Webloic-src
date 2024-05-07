package weblogic.servlet.internal.session;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.Cookie;
import weblogic.servlet.internal.AttributeWrapper;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;

public final class CookieSessionData extends SessionData {
   private static final long serialVersionUID = 1423350728235186420L;
   private ServletResponseImpl response = null;
   private Cookie wlcookie = null;
   private static final String EQL = "/";
   static final String DELIMITER = "|";

   public CookieSessionData(String var1, SessionContext var2, ServletRequestImpl var3, ServletResponseImpl var4, Cookie var5) {
      super(var1, var2, false);
      this.wlcookie = var5;
      this.response = var4;
      this.initSessionPropsFromCookie(this.wlcookie.getValue());
      if (!var2.getConfigMgr().isSessionSharingEnabled()) {
         this.wlcookie.setPath(this.response.processProxyPathHeaders(this.getContextPath()));
      }

      this.response.addCookie(this.wlcookie);
      this.setNew(false);
   }

   public CookieSessionData(String var1, SessionContext var2, ServletRequestImpl var3, ServletResponseImpl var4) {
      super(var1, var2, true);
      this.response = var4;
      this.wlcookie = new Cookie(((CookieSessionContext)var2).getWLCookieName(), this.getCookieValue());
      if (!var2.getConfigMgr().isSessionSharingEnabled()) {
         this.wlcookie.setPath(this.response.processProxyPathHeaders(this.getContextPath()));
      }

      this.response.addCookie(this.wlcookie);
      this.getWebAppServletContext().getEventsManager().notifySessionLifetimeEvent(this, true);
   }

   protected void initRuntime() {
   }

   public synchronized void setAttribute(String var1, Object var2, boolean var3) throws IllegalStateException, IllegalArgumentException {
      if (!(var2 instanceof String)) {
         throw new IllegalArgumentException("Cookie based sessions support attributes of type \"String\" only; could not set attribute: " + var1);
      } else {
         super.setAttribute(var1, var2, var3);
         this.wlcookie.setValue(this.getCookieValue());
      }
   }

   protected void removeAttribute(String var1, boolean var2) throws IllegalStateException {
      super.removeAttribute(var1, var2);
      this.wlcookie.setValue(this.getCookieValue());
   }

   public synchronized void setInternalAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException {
      if (var2 instanceof String) {
         super.setInternalAttribute(var1, var2);
         this.wlcookie.setValue(this.getCookieValue());
      }

   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      super.removeInternalAttribute(var1);
      this.wlcookie.setValue(this.getCookieValue());
   }

   public void setLastAccessedTime(long var1) {
      super.setLastAccessedTime(var1);
      if (this.wlcookie != null) {
         this.wlcookie.setValue(this.getCookieValue());
      }

   }

   public void setMaxInactiveInterval(int var1) {
      super.setMaxInactiveInterval(var1);
      this.wlcookie.setValue(this.getCookieValue());
   }

   public void invalidate(boolean var1) throws IllegalStateException {
      if (!this.isValid) {
         throw new IllegalStateException("Session already invalidated");
      } else {
         this.getContext().invalidateSession(this, false, var1);
         this.wlcookie.setValue("");
         this.wlcookie.setMaxAge(0);
         this.setValid(false);
      }
   }

   private String getCookieValue() {
      String var1 = "" + this.creationTime + "|" + this.accessTime + "|" + this.maxInactiveInterval;
      if (this.attributes != null) {
         Iterator var2 = this.attributes.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (!"weblogic.authuser".equals(var3)) {
               String var4 = this.getAttribute(var3).toString();
               if (var4 == null) {
                  var4 = "";
               }

               var1 = var1 + "|" + var3 + "/" + var4;
            }
         }
      }

      return var1;
   }

   private void initSessionPropsFromCookie(String var1) {
      try {
         if (var1 != null) {
            StringTokenizer var2 = new StringTokenizer(var1, "|");
            if (var2.hasMoreTokens()) {
               String var3 = var2.nextToken();
               this.creationTime = new Long(var3);
               if (var2.hasMoreTokens()) {
                  var3 = var2.nextToken();
                  this.accessTime = new Long(var3);
                  if (var2.hasMoreTokens()) {
                     var3 = var2.nextToken();
                     this.maxInactiveInterval = new Integer(var3);
                     this.attributes = new ConcurrentHashMap();

                     while(var2.hasMoreTokens()) {
                        String var4 = var2.nextToken();
                        int var5 = var4.indexOf("/");
                        String var6 = var4.substring(0, var5);
                        String var7 = var4.substring(var5 + 1);
                        this.attributes.put(var6, new AttributeWrapper(var7));
                     }

                  }
               }
            }
         }
      } catch (Exception var8) {
         HTTPSessionLogger.logMalformedWLCookie(var1, var8);
      }
   }

   protected void logTransientAttributeError(String var1) {
   }
}
