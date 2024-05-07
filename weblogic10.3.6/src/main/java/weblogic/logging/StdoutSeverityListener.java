package weblogic.logging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.logging.Level;
import weblogic.i18n.logging.LoggingTextFormatter;
import weblogic.management.configuration.KernelMBean;

public class StdoutSeverityListener implements PropertyChangeListener {
   private static StdoutSeverityListener singleton = null;
   private final LoggingTextFormatter formatter = new LoggingTextFormatter();
   private final KernelMBean config;
   private final ConsoleHandler console;
   private int stdoutSeverityLevel;

   public static StdoutSeverityListener getStdoutSeverityListener(KernelMBean var0) {
      if (singleton == null) {
         singleton = new StdoutSeverityListener(var0);
      }

      return singleton;
   }

   private StdoutSeverityListener(KernelMBean var1) {
      this.config = var1;
      this.console = new ConsoleHandler(var1);
      this.initializeSeverityLevel();
   }

   int getStdoutSeverityLevel() {
      return this.stdoutSeverityLevel;
   }

   public void propertyChange(PropertyChangeEvent var1) {
      this.initializeSeverityLevel();
      if ("StdoutSeverity".equals(var1.getPropertyName())) {
         this.informUsersOfSeverityLevel();
      }

   }

   private void initializeSeverityLevel() {
      this.stdoutSeverityLevel = Severities.severityStringToNum(this.config.getLog().getStdoutSeverity());
   }

   private void informUsersOfSeverityLevel() {
      if (!this.config.isStdoutEnabled()) {
         String var4 = this.formatter.noConsoleSeverity2Log();
         MessageLogger.log((Level)WLLevel.INFO, (String)"Logging", var4);
      } else {
         int var1 = this.config.getStdoutSeverityLevel();
         String var2 = null;
         if (var1 >= 64) {
            var2 = this.formatter.everyConsoleSeverity2Log();
         } else {
            String var3 = SeverityI18N.severityNumToString(var1, Locale.getDefault());
            var2 = this.formatter.someConsoleSeverity2Log(var3);
         }

         MessageLogger.log((Level)WLLevel.INFO, (String)"Logging", var2);
      }
   }
}
