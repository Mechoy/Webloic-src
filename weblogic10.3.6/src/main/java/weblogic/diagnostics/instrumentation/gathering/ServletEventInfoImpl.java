package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.ServletEventInfo;

public class ServletEventInfoImpl implements ServletEventInfo {
   private String uri = null;

   public ServletEventInfoImpl(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      this.uri = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("URI=");
      var1.append(this.uri);
      return var1.toString();
   }
}
