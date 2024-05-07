package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSEventInfo;

public class WebservicesJAXWSEventInfoImpl implements WebservicesJAXWSEventInfo {
   private String uri = null;

   public WebservicesJAXWSEventInfoImpl(String var1) {
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
