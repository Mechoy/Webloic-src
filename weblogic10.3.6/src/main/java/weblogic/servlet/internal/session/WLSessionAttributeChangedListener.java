package weblogic.servlet.internal.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class WLSessionAttributeChangedListener implements HttpSessionAttributeListener {
   public final void attributeAdded(HttpSessionBindingEvent var1) {
      if (this.debuggable(var1)) {
         this.logAttributeEvent("Add new Attribute", var1);
         if ("wl_debug_session".equals(var1.getName())) {
            this.logStartStopDebugging(true, var1.getSession());
         }

      }
   }

   public final void attributeRemoved(HttpSessionBindingEvent var1) {
      if (this.debuggable(var1)) {
         this.logAttributeEvent("Remove Attribute", var1);
         if ("wl_debug_session".equals(var1.getName())) {
            this.logStartStopDebugging(false, var1.getSession());
         }

      }
   }

   public final void attributeReplaced(HttpSessionBindingEvent var1) {
      if (this.debuggable(var1)) {
         this.logAttributeEvent("Replace Attribute", var1);
      }
   }

   private final boolean debuggable(HttpSessionBindingEvent var1) {
      return var1.getSession() != null && var1.getSession() instanceof SessionData;
   }

   private final void logAttributeEvent(String var1, HttpSessionBindingEvent var2) {
      SessionData var3 = (SessionData)var2.getSession();
      if (var3.isDebuggingSession()) {
         var3.logSessionAttributeChanged(var1, var2.getName(), var2.getValue(), var3.getAttribute(var2.getName()));
      }
   }

   private final void logStartStopDebugging(boolean var1, HttpSession var2) {
      String var3 = var2.getServletContext() == null ? "" : var2.getServletContext().getContextPath();
      String var4 = var1 ? "Start to debug session" : "Stop debugging session";
      HTTPSessionLogger.logDebugSessionEvent(var4, var3, var2.getId());
      SessionData.dumpSessionToLog(var2);
   }
}
