package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.EJBEventInfo;

public class EJBEventInfoImpl implements EJBEventInfo {
   private static final String delimiter = ":";
   private String applicationName = null;
   private String componentName = null;
   private String ejbName = null;
   private String ejbMethodName = null;

   public EJBEventInfoImpl(String var1, String var2, String var3, String var4) {
      this.applicationName = var1;
      this.componentName = var2;
      this.ejbName = var3;
      this.ejbMethodName = var4;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public void setApplicationName(String var1) {
      this.applicationName = var1;
   }

   public String getComponentName() {
      return this.componentName;
   }

   public void setComponentName(String var1) {
      this.componentName = var1;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public void setEjbName(String var1) {
      this.ejbName = var1;
   }

   public String getEjbMethodName() {
      return this.ejbMethodName;
   }

   public void setEjbMethodName(String var1) {
      this.ejbMethodName = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.applicationName);
      var1.append(":");
      var1.append(this.componentName);
      var1.append(":");
      var1.append(this.ejbName);
      var1.append(":");
      var1.append(this.ejbMethodName);
      return var1.toString();
   }
}
