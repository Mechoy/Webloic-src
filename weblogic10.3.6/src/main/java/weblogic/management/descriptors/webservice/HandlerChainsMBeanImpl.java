package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class HandlerChainsMBeanImpl extends XMLElementMBeanDelegate implements HandlerChainsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_handlerChains = false;
   private List handlerChains;

   public HandlerChainMBean[] getHandlerChains() {
      if (this.handlerChains == null) {
         return new HandlerChainMBean[0];
      } else {
         HandlerChainMBean[] var1 = new HandlerChainMBean[this.handlerChains.size()];
         var1 = (HandlerChainMBean[])((HandlerChainMBean[])this.handlerChains.toArray(var1));
         return var1;
      }
   }

   public void setHandlerChains(HandlerChainMBean[] var1) {
      this.isSet_handlerChains = true;
      if (this.handlerChains == null) {
         this.handlerChains = Collections.synchronizedList(new ArrayList());
      } else {
         this.handlerChains.clear();
      }

      if (null != var1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.handlerChains.add(var1[var2]);
         }
      }

   }

   public void addHandlerChain(HandlerChainMBean var1) {
      this.isSet_handlerChains = true;
      if (this.handlerChains == null) {
         this.handlerChains = Collections.synchronizedList(new ArrayList());
      }

      this.handlerChains.add(var1);
   }

   public void removeHandlerChain(HandlerChainMBean var1) {
      if (this.handlerChains != null) {
         this.handlerChains.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      if (null != this.getHandlerChains()) {
         if (this.getHandlerChains().length > 0) {
            var2.append(ToXML.indent(var1)).append("<handler-chains");
            var2.append(">\n");
         }

         for(int var3 = 0; var3 < this.getHandlerChains().length; ++var3) {
            var2.append(this.getHandlerChains()[var3].toXML(var1 + 2));
         }

         if (this.getHandlerChains().length > 0) {
            var2.append(ToXML.indent(var1)).append("</handler-chains>\n");
         }
      }

      return var2.toString();
   }
}
