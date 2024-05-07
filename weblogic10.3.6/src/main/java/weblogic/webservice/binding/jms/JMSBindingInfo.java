package weblogic.webservice.binding.jms;

import javax.xml.rpc.JAXRPCException;
import weblogic.webservice.binding.BindingInfo;

/** @deprecated */
public class JMSBindingInfo extends BindingInfo {
   private String host;
   private int port;
   private String factoryName;
   private String queueName;
   private String serviceURI;
   private static final boolean verbose = Boolean.getBoolean("weblogic.webservice.verbose");
   public static final String TRANSPORT = "http://www.openuri.org/2002/04/soap/jms/";

   public String getTransport() {
      return "jms";
   }

   public void setAddress(String var1) {
      this.parseAddress(var1);
      super.setAddress(var1);
   }

   public String getAddress() {
      return "jms://" + this.host + ":" + this.port + "/" + this.factoryName + "/" + this.queueName + "?URI=" + this.serviceURI;
   }

   public void setHost(String var1) {
      this.host = var1;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public String getServiceURI() {
      return this.serviceURI;
   }

   public void setServiceURI(String var1) {
      this.serviceURI = var1;
   }

   public String getFactoryName() {
      return this.factoryName;
   }

   public String getQueueName() {
      return this.queueName;
   }

   private void throwParseError(String var1) {
      throw new JAXRPCException("the address [" + var1 + "] is not " + "a JMS valid address. It should be of the from " + "'jms://host:port/factoryName/queueName?URI=serviceURI");
   }

   private void parseAddress(String var1) {
      if (!var1.startsWith("jms://")) {
         this.throwParseError(var1);
      }

      String var2 = var1.substring("jms://".length(), var1.length());
      int var3 = var2.indexOf(":");
      if (var3 == -1) {
         this.throwParseError(var1);
      }

      this.host = var2.substring(0, var3);
      var2 = var2.substring(var3 + 1, var2.length());
      var3 = var2.indexOf("/");
      if (var3 == -1) {
         this.throwParseError(var1);
      }

      try {
         this.port = Integer.parseInt(var2.substring(0, var3));
      } catch (NumberFormatException var5) {
         if (verbose) {
            var5.printStackTrace();
         }

         this.throwParseError(var1);
      }

      var2 = var2.substring(var3 + 1, var2.length());
      var3 = var2.indexOf("/");
      if (var3 == -1) {
         this.throwParseError(var1);
      }

      this.factoryName = var2.substring(0, var3);
      var2 = var2.substring(var3 + 1, var2.length());
      var3 = var2.indexOf("?");
      if (var3 == -1) {
         this.throwParseError(var1);
      }

      this.queueName = var2.substring(0, var3);
      var2 = var2.substring(var3 + 1, var2.length());
      if (!var2.startsWith("URI=")) {
         this.throwParseError(var1);
      }

      this.serviceURI = var2.substring("URI=".length());
   }
}
