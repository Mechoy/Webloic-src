package weblogic.logging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Handler;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.LogMBean;

public class SeverityChangeListener implements PropertyChangeListener {
   public static final String STDOUT_ATTR = "StdoutSeverity";
   public static final String FILE_ATTR = "LogFileSeverity";
   public static final String MEMORY_BUFFER_ATTR = "MemoryBufferSeverity";
   public static final String DOMAIN_LOG_BROADCAST_ATTR = "DomainLogBroadcastSeverity";
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugLoggingConfiguration");
   private String severityAttrName;
   protected Object logDest;

   public SeverityChangeListener(LogMBean var1, String var2, Object var3) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Adding listener on " + var1.toString());
      }

      var1.addPropertyChangeListener(this);
      this.severityAttrName = var2;
      this.logDest = var3;
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (var1.getPropertyName().equals(this.severityAttrName)) {
         String var2 = (String)var1.getNewValue();
         this.setLevel(var2);
      }

   }

   public void setLevel(String var1) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Setting severity = " + var1 + " on the " + this.logDest.getClass().getName() + " Handler");
      }

      int var2 = Severities.severityStringToNum(var1);
      ((Handler)this.logDest).setLevel(WLLevel.getLevel(var2));
   }
}
