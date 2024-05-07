package weblogic.xml.security.transforms;

import java.io.OutputStream;
import java.util.Map;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class OctetTransform extends Transform {
   public abstract OutputStream getOutputStream();

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      return xfactory.newOutputStream(this.getOutputStream());
   }

   public void setNamespaces(Map var1) {
   }

   public void setDest(NodeTransform var1) throws IncompatibleTransformException {
      throw new IncompatibleTransformException("Cannot convert octets -> nodes");
   }
}
