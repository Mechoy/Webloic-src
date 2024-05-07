package weblogic.xml.crypto.dsig;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.MarshalException;

public interface WLXMLStructure {
   void write(XMLStreamWriter var1) throws MarshalException;

   void read(XMLStreamReader var1) throws MarshalException;
}
