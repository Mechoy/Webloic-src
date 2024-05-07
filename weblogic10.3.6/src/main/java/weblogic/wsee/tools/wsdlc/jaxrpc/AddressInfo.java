package weblogic.wsee.tools.wsdlc.jaxrpc;

import java.util.Map;
import weblogic.wsee.util.JmsUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class AddressInfo {
   private String protocol = "";
   private String contextPath = "";
   private String serviceUri = "";
   private String portName = "";
   private String queue = "";
   private String factory = "";

   AddressInfo(WsdlPort var1) {
      if (var1 != null) {
         this.portName = var1.getName().getLocalPart();
         WsdlBinding var2 = var1.getBinding();
         this.protocol = var2.getTransportProtocol();
         SoapAddress var3 = WsdlUtils.getSoapAddress(var1);
         if (var3 != null) {
            String var4 = var3.getLocation();
            boolean var5 = true;
            int var10;
            if ((var10 = var4.indexOf("://")) > -1) {
               var4 = var4.substring(var10 + 3, var4.length());
               var4 = var4.substring(var4.indexOf(47) + 1, var4.length());
               int var7 = var4.lastIndexOf(63);
               int var6;
               if (var7 > -1) {
                  var6 = var4.substring(0, var7).lastIndexOf("/");
               } else {
                  var6 = var4.lastIndexOf("/");
               }

               if (var6 > -1) {
                  this.contextPath = var4.substring(0, var6);
                  if (var7 > -1) {
                     this.serviceUri = var4.substring(var6 + 1, var7);
                  } else {
                     this.serviceUri = var4.substring(var6 + 1, var4.length());
                  }
               } else {
                  this.contextPath = "/";
                  this.serviceUri = var4;
               }

               if (this.protocol.equalsIgnoreCase("jms")) {
                  String var8 = var4.substring(var7 + 1);
                  Map var9 = JmsUtil.asMap(var8);
                  this.queue = (String)var9.get("URI");
                  if (this.queue == null) {
                     this.queue = "weblogic.wsee.DefaultQueue";
                  }

                  this.factory = (String)var9.get("FACTORY");
                  if (this.factory == null) {
                     this.factory = "weblogic.jms.XAConnectionFactory";
                  }
               }
            }
         }
      }

   }

   public String getProtocol() {
      return this.protocol;
   }

   public String getContextPath() {
      return this.contextPath;
   }

   public String getServiceUri() {
      return this.serviceUri;
   }

   public String getPortName() {
      return this.portName;
   }

   public String getQueue() {
      return this.queue;
   }

   public String getConnectionFactory() {
      return this.factory;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.getProtocol().equals("http")) {
         var1.append("@WLHttpTransport(");
      } else if (this.getProtocol().equals("https")) {
         var1.append("@WLHttpsTransport(");
      } else {
         if (!this.getProtocol().equals("jms")) {
            return "";
         }

         var1.append("@WLJmsTransport(");
      }

      if (!StringUtil.isEmpty(this.getContextPath())) {
         var1.append("contextPath=\"" + this.getContextPath() + "\",");
      }

      var1.append("serviceUri=\"" + this.getServiceUri() + "\",");
      if (!StringUtil.isEmpty(this.getPortName())) {
         var1.append("portName=\"" + this.getPortName() + "\"");
      }

      if (!StringUtil.isEmpty(this.getQueue())) {
         var1.append(",queue=\"" + this.getQueue() + "\"");
      }

      if (!StringUtil.isEmpty(this.getConnectionFactory())) {
         var1.append(",connectionFactory=\"" + this.getConnectionFactory() + "\"");
      }

      var1.append(")");
      return var1.toString();
   }
}
