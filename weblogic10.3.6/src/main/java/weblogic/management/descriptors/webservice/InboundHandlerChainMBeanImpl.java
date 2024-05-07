package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class InboundHandlerChainMBeanImpl extends XMLElementMBeanDelegate implements InboundHandlerChainMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_handlerChains = false;
   private HandlerChainsMBean handlerChains;

   public HandlerChainsMBean getHandlerChains() {
      return this.handlerChains;
   }

   public void setHandlerChains(HandlerChainsMBean var1) {
      HandlerChainsMBean var2 = this.handlerChains;
      this.handlerChains = var1;
      this.isSet_handlerChains = var1 != null;
      this.checkChange("handlerChains", var2, this.handlerChains);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<inbound-handler-chain");
      var2.append(">\n");
      if (null != this.getHandlerChains()) {
         var2.append(this.getHandlerChains().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</inbound-handler-chain>\n");
      return var2.toString();
   }
}
