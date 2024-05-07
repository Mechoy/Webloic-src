package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class HandlerChainMBeanImpl extends XMLElementMBeanDelegate implements HandlerChainMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_handlers = false;
   private List handlers;
   private boolean isSet_handlerChainName = false;
   private String handlerChainName;

   public HandlerMBean[] getHandlers() {
      if (this.handlers == null) {
         return new HandlerMBean[0];
      } else {
         HandlerMBean[] var1 = new HandlerMBean[this.handlers.size()];
         var1 = (HandlerMBean[])((HandlerMBean[])this.handlers.toArray(var1));
         return var1;
      }
   }

   public void setHandlers(HandlerMBean[] var1) {
      HandlerMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getHandlers();
      }

      this.isSet_handlers = true;
      if (this.handlers == null) {
         this.handlers = Collections.synchronizedList(new ArrayList());
      } else {
         this.handlers.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.handlers.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Handlers", var2, this.getHandlers());
      }

   }

   public void addHandler(HandlerMBean var1) {
      this.isSet_handlers = true;
      if (this.handlers == null) {
         this.handlers = Collections.synchronizedList(new ArrayList());
      }

      this.handlers.add(var1);
   }

   public void removeHandler(HandlerMBean var1) {
      if (this.handlers != null) {
         this.handlers.remove(var1);
      }
   }

   public String getHandlerChainName() {
      return this.handlerChainName;
   }

   public void setHandlerChainName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.handlerChainName;
      this.handlerChainName = var1;
      this.isSet_handlerChainName = var1 != null;
      this.checkChange("handlerChainName", var2, this.handlerChainName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<handler-chain");
      if (this.isSet_handlerChainName) {
         var2.append(" name=\"").append(String.valueOf(this.getHandlerChainName())).append("\"");
      }

      var2.append(">\n");
      if (null != this.getHandlers()) {
         for(int var3 = 0; var3 < this.getHandlers().length; ++var3) {
            var2.append(this.getHandlers()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</handler-chain>\n");
      return var2.toString();
   }
}
