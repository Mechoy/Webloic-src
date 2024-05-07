package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.WebApplicationEventInfo;

public class WebApplicationEventInfoImpl implements WebApplicationEventInfo {
   private String moduleName = null;

   public WebApplicationEventInfoImpl(String var1) {
      this.moduleName = var1;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("moduleName=");
      var1.append(this.moduleName);
      return var1.toString();
   }
}
