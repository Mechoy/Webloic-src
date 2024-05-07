package weblogic.servlet.internal.session;

import java.io.PrintStream;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServletSessionRuntimeMBean;

public final class ServletSessionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ServletSessionRuntimeMBean {
   private static final long serialVersionUID = -1298228652729291451L;
   private SessionData session;
   public static final String TYPE = "ServletSessionRuntime";

   public ServletSessionRuntimeMBeanImpl(SessionData var1) throws ManagementException {
      super(ServletSessionRuntimeManager.getSessionKey(var1), var1.getWebAppServletContext().getRuntimeMBean());
      this.session = var1;
   }

   public void invalidate() {
      this.session.invalidate();
   }

   public long getTimeLastAccessed() {
      return this.session.getLAT();
   }

   public long getMaxInactiveInterval() {
      return (long)this.session.getMaxInactiveInterval();
   }

   public String getMainAttribute() {
      return this.session.getMainAttributeValue();
   }

   public static void dumpSession(PrintStream var0, ServletSessionRuntimeMBean var1) {
      println(var0, "    SESSION NAME: " + var1.getName());
      println(var0, "        TimeLastAccessed: " + var1.getTimeLastAccessed());
      println(var0, "        MainAttribute: " + var1.getMainAttribute());
   }

   private static void println(PrintStream var0, String var1) {
      var0.println(var1 + "<br>");
   }
}
