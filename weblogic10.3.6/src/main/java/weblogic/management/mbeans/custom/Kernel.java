package weblogic.management.mbeans.custom;

import weblogic.common.internal.VersionInfo;
import weblogic.logging.LoggingConfigurationProcessor;
import weblogic.logging.Severities;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.KernelDebugMBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class Kernel extends ConfigurationMBeanCustomizer {
   private static final boolean DEBUG = false;
   private static final String STDOUT_SEVERITY = "StdoutSeverity";
   private int stdoutSeverityLevel = 32;
   private boolean stdoutDebugEnabled = false;
   private boolean stdoutEnabled = true;
   private String stdoutFormat = "standard";
   private boolean stdoutLogStack = true;
   static final VersionInfo diabloVersion = new VersionInfo("9.0.0.0");

   public Kernel(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public int getStdoutSeverityLevel() {
      return this.isDelegateModeEnabled() ? Severities.severityStringToNum(this.getLogMBean().getStdoutSeverity()) : this.stdoutSeverityLevel;
   }

   public void setStdoutSeverityLevel(int var1) {
      this.stdoutSeverityLevel = var1;
      if (this.isDelegateModeEnabled()) {
         this.updateLogMBeanStdoutSeverity();
      }

   }

   public boolean isStdoutDebugEnabled() {
      if (this.isDelegateModeEnabled()) {
         return Severities.severityStringToNum(this.getLogMBean().getStdoutSeverity()) >= 128;
      } else {
         return this.stdoutDebugEnabled;
      }
   }

   public void setStdoutDebugEnabled(boolean var1) {
      this.stdoutDebugEnabled = var1;
      if (this.isDelegateModeEnabled()) {
         this.updateLogMBeanStdoutSeverity();
      }

   }

   public boolean isStdoutEnabled() {
      if (this.isDelegateModeEnabled()) {
         return Severities.severityStringToNum(this.getLogMBean().getStdoutSeverity()) > 0;
      } else {
         return this.stdoutEnabled;
      }
   }

   public void setStdoutEnabled(boolean var1) {
      this.stdoutEnabled = var1;
      if (this.isDelegateModeEnabled()) {
         this.updateLogMBeanStdoutSeverity();
      }

   }

   private void updateLogMBeanStdoutSeverity() {
      LogMBean var1 = ((KernelMBean)this.getMbean()).getLog();
      String var2 = LoggingConfigurationProcessor.getNormalizedStdoutSeverity(this.stdoutEnabled, this.stdoutSeverityLevel, this.stdoutDebugEnabled);
      var1.setStdoutSeverity(var2);
      if (var2.equals("Notice")) {
         var1.unSet("StdoutSeverity");
      }

   }

   public String getStdoutFormat() {
      return this.isDelegateModeEnabled() ? this.getLogMBean().getStdoutFormat() : this.stdoutFormat;
   }

   public void setStdoutFormat(String var1) {
      this.stdoutFormat = var1;
      if (this.isDelegateModeEnabled()) {
         LogMBean var2 = ((KernelMBean)this.getMbean()).getLog();
         var2.setStdoutFormat(var1);
         if (var1.equals("standard")) {
            var2.unSet("StdoutFormat");
         }
      }

   }

   public boolean isStdoutLogStack() {
      return this.isDelegateModeEnabled() ? this.getLogMBean().isStdoutLogStack() : this.stdoutLogStack;
   }

   public void setStdoutLogStack(boolean var1) {
      this.stdoutLogStack = var1;
      if (this.isDelegateModeEnabled()) {
         LogMBean var2 = ((KernelMBean)this.getMbean()).getLog();
         var2.setStdoutLogStack(var1);
         if (var1) {
            var2.unSet("StdoutLogStack");
         }
      }

   }

   private LogMBean getLogMBean() {
      return ((KernelMBean)this.getMbean()).getLog();
   }

   public KernelDebugMBean getKernelDebug() {
      return ((ServerMBean)this.getMbean()).getServerDebug();
   }

   protected boolean isDelegateModeEnabled() {
      DomainMBean var1 = (DomainMBean)this.getMbean().getDescriptor().getRootBean();
      String var2 = var1.getConfigurationVersion();
      if (var2 == null) {
         return false;
      } else {
         VersionInfo var3 = new VersionInfo(var2);
         return !var3.earlierThan(diabloVersion);
      }
   }
}
