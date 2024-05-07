package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.runtime.NodeFromStreamReader;
import com.bea.xml.XmlException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.dom.DOMStreamReader;

public class WLSDomNodeFromStreamReader implements NodeFromStreamReader {
   private static NodeFromStreamReader INSTANCE = new WLSDomNodeFromStreamReader();

   public static NodeFromStreamReader getInstance() {
      return INSTANCE;
   }

   public Node getCurrentNode(XMLStreamReader var1) throws XmlException {
      if (var1 instanceof DOMStreamReader) {
         DOMStreamReader var3 = (DOMStreamReader)var1;
         return var3.current();
      } else {
         String var2 = "expected reader of type " + DOMStreamReader.class.getName() + " not " + var1.getClass().getName();
         throw new IllegalStateException(var2);
      }
   }
}
