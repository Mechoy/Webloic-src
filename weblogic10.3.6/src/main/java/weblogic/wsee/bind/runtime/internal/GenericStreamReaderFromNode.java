package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.runtime.StreamReaderFromNode;
import com.bea.xml.XmlException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;
import weblogic.xml.dom.DOMStreamReaderExt;

public final class GenericStreamReaderFromNode implements StreamReaderFromNode {
   private static final StreamReaderFromNode INSTANCE = new GenericStreamReaderFromNode();

   public static StreamReaderFromNode getInstance() {
      return INSTANCE;
   }

   private GenericStreamReaderFromNode() {
   }

   public XMLStreamReader getStreamReader(Element var1) throws XmlException {
      try {
         return new DOMStreamReaderExt(var1);
      } catch (XMLStreamException var3) {
         throw new XmlException(var3);
      }
   }
}
