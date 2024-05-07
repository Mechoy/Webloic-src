package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.ServletStaleResourceEventInfo;

public class ServletStaleResourceEventInfoImpl implements ServletStaleResourceEventInfo {
   private String resource = null;

   public ServletStaleResourceEventInfoImpl(String var1) {
      this.resource = var1;
   }

   public String getResource() {
      return this.resource;
   }

   public void setResource(String var1) {
      this.resource = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("resource=");
      var1.append(this.resource);
      return var1.toString();
   }

   public boolean getThrottled() {
      return false;
   }

   public void setThrottled(boolean var1) {
   }
}
