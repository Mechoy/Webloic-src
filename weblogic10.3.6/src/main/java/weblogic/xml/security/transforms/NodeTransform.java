package weblogic.xml.security.transforms;

import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class NodeTransform extends Transform {
   public abstract XMLOutputStream getXMLOutputStream() throws XMLStreamException;

   public void setDest(OctetTransform var1) throws IncompatibleTransformException {
      this.setDest(new WriterTransform(var1));
   }
}
