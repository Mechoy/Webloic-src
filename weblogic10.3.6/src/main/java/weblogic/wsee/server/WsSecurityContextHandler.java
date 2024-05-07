package weblogic.wsee.server;

import javax.xml.rpc.handler.MessageContext;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;

public class WsSecurityContextHandler implements ContextHandler {
   private int SIZE = 1;
   private ContextElement messageContext;

   public WsSecurityContextHandler(MessageContext var1) {
      this.messageContext = new ContextElement("com.bea.contextelement.wsee.SOAPMessage", var1);
   }

   public int size() {
      return this.SIZE;
   }

   public String[] getNames() {
      return new String[]{"com.bea.contextelement.wsee.SOAPMessage"};
   }

   public Object getValue(String var1) {
      return "com.bea.contextelement.wsee.SOAPMessage".equals(var1) ? this.messageContext.getValue() : null;
   }

   public ContextElement[] getValues(String[] var1) {
      if (var1 != null && var1.length != 0) {
         return "com.bea.contextelement.wsee.SOAPMessage".equals(var1[0]) ? new ContextElement[]{this.messageContext} : new ContextElement[0];
      } else {
         return null;
      }
   }
}
