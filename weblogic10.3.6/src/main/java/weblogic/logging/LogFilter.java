package weblogic.logging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.logging.LogVariablesImpl;
import weblogic.diagnostics.query.Query;
import weblogic.diagnostics.query.QueryException;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.LogMBean;

public class LogFilter implements Filter, PropertyChangeListener {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugLoggingConfiguration");
   public static final String FILE_FILTER_ATTR = "LogFileFilter";
   public static final String STDOUT_FILTER_ATTR = "StdoutFilter";
   public static final String MEMORY_FILTER_ATTR = "MemoryBufferFilter";
   public static final String DOMAIN_FILTER_ATTR = "DomainLogBroadcastFilter";
   private static final String FILTER_ATTR = "FilterExpression";
   private Query filterQuery = null;
   private String filterAttrName;
   private LogFilterMBean filterConfig = null;

   public LogFilter(LogMBean var1, String var2, LogFilterMBean var3) {
      this.filterConfig = var3;
      if (this.filterConfig != null) {
         this.filterConfig.addPropertyChangeListener(this);
      }

      this.filterAttrName = var2;
      var1.addPropertyChangeListener(this);
      this.initialize();
   }

   public boolean isLoggable(LogRecord var1) {
      WLLogRecord var2 = WLLogger.normalizeLogRecord(var1);
      return this.filterLogEntry(var2);
   }

   public boolean filterLogEntry(LogEntry var1) {
      if (this.filterQuery != null) {
         try {
            return this.filterQuery.executeQuery(LogVariablesImpl.getInstance().getLogVariablesResolver(var1));
         } catch (QueryException var3) {
            return true;
         }
      } else {
         return true;
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      Object var3 = var1.getOldValue();
      Object var4 = var1.getNewValue();
      if (var2.equals("FilterExpression")) {
         this.initialize();
      } else if (var2.equals(this.filterAttrName)) {
         if (this.filterConfig != null) {
            this.filterConfig.removePropertyChangeListener(this);
         }

         this.filterConfig = (LogFilterMBean)var4;
         if (this.filterConfig != null) {
            this.filterConfig.addPropertyChangeListener(this);
         }

         this.initialize();
      }

   }

   private void initialize() {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Initializing filter for " + this.filterAttrName);
      }

      if (this.filterConfig != null) {
         this.filterQuery = this.filterConfig.getQuery();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Filter expression is " + this.filterConfig.getFilterExpression());
         }
      } else {
         this.filterQuery = null;
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Filter expression is null");
         }
      }

   }

   public String toString() {
      String var1 = this.filterConfig == null ? "" : this.filterConfig.getFilterExpression();
      StringBuilder var2 = new StringBuilder();
      var2.append("The filter is for attribute = " + this.filterAttrName + " with expression " + var1);
      return var2.toString();
   }
}
