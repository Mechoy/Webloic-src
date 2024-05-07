package weblogic.diagnostics.instrumentation;

import java.io.Serializable;
import java.util.Properties;
import weblogic.diagnostics.context.DiagnosticContextManager;
import weblogic.diagnostics.instrumentation.rtsupport.InstrumentationSupportImpl;

public abstract class DiagnosticMonitorControl implements DiagnosticMonitor, Serializable, Comparable {
   private InstrumentationScope instrumentationScope;
   private String name;
   private String description;
   private String type;
   private boolean allowServerScope;
   private boolean allowComponentScope;
   public boolean enabled;
   private boolean enableDyeFiltering;
   private long dye_mask;
   private String[] attributeNames;
   private Properties properties;
   private String[] includes;
   private String[] excludes;
   public boolean argumentsCaptureNeeded;
   private boolean serverManaged;
   private String diagnosticVolume;
   private String eventClassName;
   private Class eventClass;

   public DiagnosticMonitorControl(DiagnosticMonitorControl var1) {
      this(var1.getType());
      this.allowServerScope = var1.allowServerScope;
      this.allowComponentScope = var1.allowComponentScope;
      this.attributeNames = var1.attributeNames;
      this.serverManaged = var1.serverManaged;
      this.diagnosticVolume = var1.diagnosticVolume;
      this.eventClassName = var1.eventClassName;
      this.resolveEventClass();
   }

   public DiagnosticMonitorControl(String var1) {
      this("", var1);
   }

   public DiagnosticMonitorControl(String var1, String var2) {
      this.properties = new Properties();
      this.argumentsCaptureNeeded = false;
      this.serverManaged = false;
      this.diagnosticVolume = null;
      this.eventClassName = null;
      this.eventClass = null;
      this.name = var1;
      this.type = var2;
   }

   void setInstrumentationScope(InstrumentationScope var1) {
      this.instrumentationScope = var1;
   }

   public InstrumentationScope getInstrumentationScope() {
      return this.instrumentationScope;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getType() {
      return this.type;
   }

   public boolean isServerScopeAllowed() {
      return this.allowServerScope;
   }

   void setServerScopeAllowed(boolean var1) {
      this.allowServerScope = var1;
   }

   public boolean isComponentScopeAllowed() {
      return this.allowComponentScope;
   }

   void setComponentScopeAllowed(boolean var1) {
      this.allowComponentScope = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("DiagnosticMonitorControl.setEnabled " + this.getType() + " to " + var1);
      }

      this.enabled = var1;
   }

   public boolean isEnabledAndNotDyeFiltered() {
      if (!this.enabled) {
         return false;
      } else if (!this.enableDyeFiltering) {
         return !this.serverManaged || DiagnosticContextManager.isJFRThrottled();
      } else {
         return InstrumentationSupportImpl.dyeMatches(this);
      }
   }

   public boolean isArgumentsCaptureNeeded() {
      return this.argumentsCaptureNeeded;
   }

   public boolean isServerManaged() {
      return this.serverManaged;
   }

   public void setServerManaged(boolean var1) {
      this.serverManaged = var1;
   }

   public String getEventClassName() {
      return this.eventClassName;
   }

   public void setEventClassName(String var1) {
      this.eventClassName = var1;
   }

   public Class getEventClass() {
      return this.eventClass;
   }

   private void resolveEventClass() {
      if (this.eventClassName != null && this.eventClassName.length() != 0) {
         try {
            this.eventClass = Class.forName(this.eventClassName);
            if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
               InstrumentationDebug.DEBUG_CONFIG.debug("DiagnosticMonitorControl.resolveEventClass " + this.eventClassName + " resolved and is not pooled");
            }
         } catch (Exception var2) {
            if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
               InstrumentationDebug.DEBUG_CONFIG.debug("DiagnosticMonitorControl.resolveEventClass exception resolving " + this.eventClassName, var2);
            }
         }

      }
   }

   public String getDiagnosticVolume() {
      return this.diagnosticVolume;
   }

   public void setDiagnosticVolume(String var1) {
      this.diagnosticVolume = var1;
   }

   public String[] getAttributeNames() {
      return this.attributeNames;
   }

   public void setAttributeNames(String[] var1) {
      this.attributeNames = var1;
   }

   public String getAttribute(String var1) {
      String var2 = null;
      if (this.isValidAttributeName(var1)) {
         var2 = this.properties.getProperty(var1);
      }

      return var2;
   }

   public void setAttribute(String var1, String var2) {
      if (this.isValidAttributeName(var1)) {
         if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_CONFIG.debug("DiagnosticMonitorControl.setAttribute " + this.getType() + " " + var1 + "=" + var2);
         }

         if (var2 == null) {
            this.properties.remove(var1);
         } else {
            this.properties.setProperty(var1, var2);
         }
      }

   }

   private boolean isValidAttributeName(String var1) {
      int var2 = this.attributeNames != null ? this.attributeNames.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1.equals(this.attributeNames[var3])) {
            return true;
         }
      }

      return false;
   }

   public boolean isDyeFilteringEnabled() {
      return this.enableDyeFiltering;
   }

   public void setDyeFilteringEnabled(boolean var1) {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("DiagnosticMonitorControl.setDyeFilteringEnabled " + this.getType() + " to " + var1);
      }

      this.enableDyeFiltering = var1;
   }

   public long getDyeMask() {
      return this.dye_mask;
   }

   public void setDyeMask(long var1) {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("DiagnosticMonitorControl.setDyeMask " + this.getType() + " to " + var1);
      }

      this.dye_mask = var1;
   }

   public String[] getIncludes() {
      return this.includes;
   }

   public void setIncludes(String[] var1) {
      this.includes = var1;
   }

   public String[] getExcludes() {
      return this.excludes;
   }

   public void setExcludes(String[] var1) {
      this.excludes = var1;
   }

   protected synchronized boolean merge(DiagnosticMonitorControl var1) {
      boolean var2 = false;
      if (var1 != null && this.type.equals(var1.type)) {
         this.name = var1.name;
         this.description = var1.description;
         this.allowServerScope = var1.allowServerScope;
         this.allowComponentScope = var1.allowComponentScope;
         this.enabled = var1.enabled;
         this.enableDyeFiltering = var1.enableDyeFiltering;
         this.dye_mask = var1.dye_mask;
         this.attributeNames = var1.attributeNames;
         this.properties = var1.properties;
         this.includes = var1.includes;
         this.excludes = var1.excludes;
         this.argumentsCaptureNeeded = var1.argumentsCaptureNeeded;
         this.serverManaged = var1.serverManaged;
         this.diagnosticVolume = var1.diagnosticVolume;
         var2 = true;
      }

      return var2;
   }

   public int compareTo(Object var1) {
      DiagnosticMonitorControl var2 = (DiagnosticMonitorControl)var1;
      return this.type.compareTo(var2.type);
   }
}
