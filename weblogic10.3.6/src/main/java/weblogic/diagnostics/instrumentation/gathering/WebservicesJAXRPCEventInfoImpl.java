package weblogic.diagnostics.instrumentation.gathering;

import java.util.ArrayList;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCEventInfo;

public class WebservicesJAXRPCEventInfoImpl implements WebservicesJAXRPCEventInfo {
   private Object smc = null;
   private boolean sending = false;
   private String action = null;
   private String currentParty = null;
   private WSSoapMessageContextBaseRenderer renderer = null;
   private ArrayList<WebservicesJAXRPCEventInfo> deferredArguments = null;

   public WebservicesJAXRPCEventInfoImpl(WSSoapMessageContextBaseRenderer var1, Object var2, boolean var3) {
      this.renderer = var1;
      this.smc = var2;
      this.sending = var3;
   }

   public boolean isSending() {
      return this.sending;
   }

   public String getAction() {
      if (this.action != null) {
         return this.action;
      } else {
         if (this.smc != null) {
            this.action = this.renderer.getAction(this.smc);
         }

         return this.action;
      }
   }

   public void setAction(String var1) {
      this.action = var1;
   }

   public String getCurrentParty() {
      if (this.currentParty != null) {
         return this.currentParty;
      } else {
         if (this.smc != null) {
            this.currentParty = this.renderer.getCurrentParty(this.smc, this.sending);
         }

         return this.currentParty;
      }
   }

   public void setCurrentParty(String var1) {
      this.currentParty = var1;
   }

   public ArrayList<WebservicesJAXRPCEventInfo> getDeferredArguments() {
      return this.deferredArguments;
   }

   public void setDeferredArguments(ArrayList<WebservicesJAXRPCEventInfo> var1) {
      this.deferredArguments = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("action=");
      var1.append(this.getAction());
      var1.append(",");
      var1.append("currentParty=");
      var1.append(this.getCurrentParty());
      return var1.toString();
   }
}
