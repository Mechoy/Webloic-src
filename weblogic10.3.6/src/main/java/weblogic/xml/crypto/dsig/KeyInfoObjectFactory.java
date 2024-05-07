package weblogic.xml.crypto.dsig;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.api.MarshalException;

public interface KeyInfoObjectFactory {
   QName getQName();

   Object newKeyInfoObject(XMLStreamReader var1) throws MarshalException;
}
