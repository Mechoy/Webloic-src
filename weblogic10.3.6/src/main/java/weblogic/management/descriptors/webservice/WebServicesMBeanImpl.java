package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WebServicesMBeanImpl extends XMLElementMBeanDelegate implements WebServicesMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_webServices = false;
   private List webServices;
   private boolean isSet_handlerChains = false;
   private HandlerChainsMBean handlerChains;

   public WebServiceMBean[] getWebServices() {
      if (this.webServices == null) {
         return new WebServiceMBean[0];
      } else {
         WebServiceMBean[] var1 = new WebServiceMBean[this.webServices.size()];
         var1 = (WebServiceMBean[])((WebServiceMBean[])this.webServices.toArray(var1));
         return var1;
      }
   }

   public HandlerChainsMBean getHandlerChains() {
      if (this.handlerChains == null) {
         this.handlerChains = new HandlerChainsMBeanImpl();
      }

      return this.handlerChains;
   }

   public void setWebServices(WebServiceMBean[] var1) {
      this.isSet_webServices = true;
      if (this.webServices == null) {
         this.webServices = Collections.synchronizedList(new ArrayList());
      } else {
         this.webServices.clear();
      }

      if (null != var1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.webServices.add(var1[var2]);
         }
      }

   }

   public void setHandlerChains(HandlerChainsMBean var1) {
      this.handlerChains = var1;
      this.isSet_handlerChains = true;
   }

   public void addWebService(WebServiceMBean var1) {
      this.isSet_webServices = true;
      if (this.webServices == null) {
         this.webServices = Collections.synchronizedList(new ArrayList());
      }

      this.webServices.add(var1);
   }

   public void removeWebService(WebServiceMBean var1) {
      if (this.webServices != null) {
         this.webServices.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<web-services");
      var2.append(">\n");
      if (null != this.getHandlerChains()) {
         var2.append(this.getHandlerChains().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getWebServices()) {
         for(int var3 = 0; var3 < this.getWebServices().length; ++var3) {
            var2.append(this.getWebServices()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</web-services>\n");
      return var2.toString();
   }
}
