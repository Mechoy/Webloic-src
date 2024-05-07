package weblogic.wsee.util;

import javax.xml.soap.SOAPConnectionFactory;
import weblogic.wsee.saaj.SOAPConnectionFactoryImpl;

public class WLSOAPConnectionFactory {
   public static SOAPConnectionFactory createSOAPConnectionFactory() {
      return new SOAPConnectionFactoryImpl();
   }
}
