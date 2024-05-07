package weblogic.wsee.saaj;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;

public class SOAPConnectionFactoryImpl extends SOAPConnectionFactory {
   public SOAPConnection createConnection() throws SOAPException {
      return new SOAPConnectionImpl();
   }
}
