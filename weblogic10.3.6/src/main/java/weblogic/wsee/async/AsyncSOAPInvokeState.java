package weblogic.wsee.async;

import java.io.Externalizable;
import java.util.Map;
import javax.xml.soap.SOAPMessage;

public interface AsyncSOAPInvokeState extends Externalizable {
   SOAPMessage getSOAPMessage();

   SOAPMessage getClonedSOAPMessage();

   boolean isSoap12();

   Map getMessageContextProperties();

   String getServiceURI();
}
