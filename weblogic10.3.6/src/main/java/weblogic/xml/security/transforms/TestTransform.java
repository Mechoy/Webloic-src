package weblogic.xml.security.transforms;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class TestTransform extends NodeTransform {
   private final ByteArrayOutputStream result = new ByteArrayOutputStream();

   public void setNamespaces(Map namespaces) {
   }

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      return xfactory.newOutputStream(this.result);
   }

   public String getURI() {
      return "TEST";
   }

   public byte[] getResult() {
      return this.result.toByteArray();
   }

   public void setDest(NodeTransform dest) throws IncompatibleTransformException {
      throw new IncompatibleTransformException("Internal Transform error");
   }
}
