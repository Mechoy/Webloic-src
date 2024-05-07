package weblogic.auddi.soap;

import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;

class SOAPTransportFactory {
   private static final String SOAP_TRANSPORT_TYPE = "acumenat.soap.transport.type";
   private static final String WLS_SAAJ_MESSAGEFACTORY_CLASS = "weblogic.webservice.core.soap.MessageFactoryImpl.class";
   private static final String WLS_SAAJ_CONNECTIONFACTORY_CLASS = "weblogic.webservice.core.soap.ConnectionFactoryImpl.class";
   private static final String WLS_SAAJ_SOAPFACTORY_CLASS = "weblogic.webservice.core.soap.SOAPFactoryImpl.class";
   private static final String ACUMEN_SAAJ_MESSAGEFACTORY_CLASS = "acumenat.saaj.MessageFactory.class";
   private static final String ACUMEN_SAAJ_CONNECTIONFACTORY_CLASS = "acumenat.saaj.ConnectionFactory.class";
   private static final String ACUMEN_SAAJ_SOAPFACTORY_CLASS = "acumenat.saaj.SOAPFactory.class";

   private SOAPTransportFactory() {
   }

   public static SOAPTransport getSOAPTransport() {
      Logger.trace("+weblogic.auddi.soap.SOAPTransportFactory.getSOAPTransport()");
      String var0 = PropertyManager.getRuntimeProperty("acumenat.soap.transport.type");
      Logger.trace("transportType : " + var0);
      if (var0 != null && var0.length() != 0 && !var0.equalsIgnoreCase("SAAJ")) {
         try {
            Class var6 = Class.forName(var0);
            SOAPTransport var7 = (SOAPTransport)var6.newInstance();
            Logger.trace("-weblogic.auddi.soap.SOAPTransportFactory.getSOAPTransport() returning " + var0);
            return var7;
         } catch (Exception var5) {
            Logger.error((Throwable)var5);
            throw new RuntimeException(UDDIMessages.get("error.class.instantiation", var0));
         }
      } else {
         String var1 = PropertyManager.getRuntimeProperty("weblogic.webservice.core.soap.MessageFactoryImpl.class");
         String var2 = PropertyManager.getRuntimeProperty("weblogic.webservice.core.soap.ConnectionFactoryImpl.class");
         String var3 = PropertyManager.getRuntimeProperty("weblogic.webservice.core.soap.SOAPFactoryImpl.class");
         if (var1 == null) {
            var1 = PropertyManager.getRuntimeProperty("acumenat.saaj.MessageFactory.class");
            var2 = PropertyManager.getRuntimeProperty("acumenat.saaj.ConnectionFactory.class");
            var3 = PropertyManager.getRuntimeProperty("acumenat.saaj.SOAPFactory.class");
         }

         SAAJTransport var4 = new SAAJTransport(var1, var2, var3);
         Logger.trace("-weblogic.auddi.soap.SOAPTransportFactory.getSOAPTransport() returning SAAJTransport");
         return var4;
      }
   }
}
