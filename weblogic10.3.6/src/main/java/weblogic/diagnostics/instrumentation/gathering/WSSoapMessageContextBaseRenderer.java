package weblogic.diagnostics.instrumentation.gathering;

import java.lang.reflect.Method;
import weblogic.diagnostics.instrumentation.ValueRenderer;

public class WSSoapMessageContextBaseRenderer implements ValueRenderer {
   private boolean enabled = false;
   private boolean sending = false;
   private static final String getActionName = "getAction";
   private static final String getCurrentPartyName = "getCurrentParty";
   private static final String wlSoapConnection = "weblogic.wsee.connection.soap.SoapConnection";
   private static final String wlSoapMessageContext = "weblogic.wsee.message.soap.SoapMessageContext";
   private Class wlSoapMessageContextClass;
   private Method getAction;
   private Method getCurrentParty;

   public WSSoapMessageContextBaseRenderer(boolean var1) {
      this.sending = var1;

      try {
         Class var2 = Class.forName("weblogic.wsee.connection.soap.SoapConnection");
         this.wlSoapMessageContextClass = Class.forName("weblogic.wsee.message.soap.SoapMessageContext");
         Class[] var3 = new Class[]{this.wlSoapMessageContextClass};
         this.getAction = var2.getMethod("getAction", var3);
         var3 = new Class[]{this.wlSoapMessageContextClass, Boolean.TYPE};
         this.getCurrentParty = var2.getMethod("getCurrentParty", var3);
         this.enabled = true;
      } catch (Exception var4) {
      }

   }

   public Object render(Object var1) {
      return this.enabled && var1 != null && this.wlSoapMessageContextClass.isInstance(var1) ? new WebservicesJAXRPCEventInfoImpl(this, var1, this.sending) : null;
   }

   String getCurrentParty(Object var1, boolean var2) {
      try {
         Object[] var3 = new Object[]{var1, new Boolean(var2)};
         return (String)this.getCurrentParty.invoke((Object)null, var3);
      } catch (Exception var4) {
         return null;
      }
   }

   String getAction(Object var1) {
      try {
         Object[] var2 = new Object[]{var1};
         return (String)this.getAction.invoke((Object)null, var2);
      } catch (Exception var3) {
         return null;
      }
   }
}
