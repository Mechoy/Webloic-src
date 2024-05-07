package weblogic.xml.security.transforms;

import java.util.Map;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class WriterTransform extends NodeTransform {
   private final OctetTransform dest;

   public WriterTransform(OctetTransform var1) {
      this.dest = var1;
   }

   public void setNamespaces(Map var1) {
      this.dest.setNamespaces(var1);
   }

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      return xfactory.newOutputStream(this.dest.getOutputStream());
   }

   public String getURI() {
      return this.dest.getURI();
   }

   public void setDest(NodeTransform var1) throws IncompatibleTransformException {
      throw new IncompatibleTransformException("Internal Transform error");
   }
}
