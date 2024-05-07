package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ServerHandlerChainMBeanImpl extends XMLElementMBeanDelegate implements ServerHandlerChainMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_inboundHandlerChain = false;
   private InboundHandlerChainMBean inboundHandlerChain;
   private boolean isSet_outboundHandlerChain = false;
   private OutboundHandlerChainMBean outboundHandlerChain;

   public InboundHandlerChainMBean getInboundHandlerChain() {
      return this.inboundHandlerChain;
   }

   public void setInboundHandlerChain(InboundHandlerChainMBean var1) {
      InboundHandlerChainMBean var2 = this.inboundHandlerChain;
      this.inboundHandlerChain = var1;
      this.isSet_inboundHandlerChain = var1 != null;
      this.checkChange("inboundHandlerChain", var2, this.inboundHandlerChain);
   }

   public OutboundHandlerChainMBean getOutboundHandlerChain() {
      return this.outboundHandlerChain;
   }

   public void setOutboundHandlerChain(OutboundHandlerChainMBean var1) {
      OutboundHandlerChainMBean var2 = this.outboundHandlerChain;
      this.outboundHandlerChain = var1;
      this.isSet_outboundHandlerChain = var1 != null;
      this.checkChange("outboundHandlerChain", var2, this.outboundHandlerChain);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<server-handler-chain");
      var2.append(">\n");
      if (null != this.getInboundHandlerChain()) {
         var2.append(this.getInboundHandlerChain().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getOutboundHandlerChain()) {
         var2.append(this.getOutboundHandlerChain().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</server-handler-chain>\n");
      return var2.toString();
   }
}
