package weblogic.wsee.util;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import weblogic.xml.saaj.SAAJMetaFactoryImpl;

public class WLSOAPFactory {
   private static WLSOAPFactory wlSOAPFactory = null;
   private static SAAJMetaFactoryImpl metaFactory = new SAAJMetaFactoryImpl();

   private WLSOAPFactory() {
   }

   public static WLSOAPFactory getInstance() {
      if (wlSOAPFactory == null) {
         wlSOAPFactory = new WLSOAPFactory();
      }

      return wlSOAPFactory;
   }

   /** @deprecated */
   @Deprecated
   public static SOAPFactory createSOAPFactory() {
      return createSOAPFactory("SOAP 1.1 Protocol");
   }

   public static SOAPFactory createSOAPFactory(String var0) {
      try {
         return metaFactory.newSOAPFactory(var0);
      } catch (SOAPException var2) {
         throw new ExceptionInInitializerError(var2);
      }
   }
}
