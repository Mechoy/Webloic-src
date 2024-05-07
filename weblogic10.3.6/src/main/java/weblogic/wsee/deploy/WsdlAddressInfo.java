package weblogic.wsee.deploy;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.wsdl.WsdlFilter;

public class WsdlAddressInfo {
   private String host;
   private String port;
   private String importPrefix;
   private QName serviceName;
   private Map wsdlPorts = new HashMap();
   private String policyURL;
   private WsdlFilter wsdlFilter;

   public String getHost() {
      return this.host;
   }

   public void setHost(String var1) {
      this.host = var1;
   }

   public String getPort() {
      return this.port;
   }

   public void setPort(String var1) {
      this.port = var1;
   }

   public String getImportPrefix() {
      return this.importPrefix;
   }

   public void setImportPrefix(String var1) {
      this.importPrefix = var1;
   }

   public QName getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(QName var1) {
      this.serviceName = var1;
   }

   public void setPolicyURL(String var1) {
      this.policyURL = var1;
   }

   public String getPolicyURL() {
      return this.policyURL;
   }

   public WsdlFilter getWsdlFilter() {
      return this.wsdlFilter;
   }

   public void setWsdlFilter(WsdlFilter var1) {
      this.wsdlFilter = var1;
   }

   public PortAddress addWsdlPort(QName var1) {
      PortAddress var2 = new PortAddress();
      this.wsdlPorts.put(var1, var2);
      return var2;
   }

   public void addWsdlPort(QName var1, PortAddress var2) {
      this.wsdlPorts.put(var1, var2);
   }

   public PortAddress removeWsdlPort(QName var1) {
      return (PortAddress)this.wsdlPorts.remove(var1);
   }

   public PortAddress getPortAddress(QName var1) {
      return (PortAddress)this.wsdlPorts.get(var1);
   }

   public String getServiceUrl(QName var1) {
      return this.getServiceUrl(var1, (String)null);
   }

   public String getServiceUrl(QName var1, String var2) {
      PortAddress var3 = this.getPortAddress(var1);
      if (var3 == null) {
         return null;
      } else {
         String var4 = var3.getHost();
         if (var4 == null) {
            var4 = this.host;
         }

         String var5 = var3.getListenPort();
         if (var5 == null) {
            var5 = this.port;
         }

         return var2 != null && this.wsdlFilter != null ? this.wsdlFilter.rewritePortUrl(var1, var3.getProtocol(), var4, var5, var3.getServiceuri(), var2) : var3.getProtocol() + "://" + var4 + ":" + var5 + var3.getServiceuri();
      }
   }

   public static class PortAddress {
      private String protocol;
      private String listenPort;
      private String host;
      private String serviceuri;

      private PortAddress() {
         this.protocol = "http";
         this.listenPort = null;
         this.host = null;
      }

      public String getProtocol() {
         return this.protocol;
      }

      public void setProtocol(String var1) {
         this.protocol = var1;
      }

      public String getServiceuri() {
         return this.serviceuri;
      }

      public void setServiceuri(String var1) {
         this.serviceuri = var1;
      }

      public String getListenPort() {
         return this.listenPort;
      }

      public void setListenPort(String var1) {
         this.listenPort = var1;
      }

      public String getHost() {
         return this.host;
      }

      public void setHost(String var1) {
         this.host = var1;
      }

      // $FF: synthetic method
      PortAddress(Object var1) {
         this();
      }
   }
}
