package weblogic.wsee.jaxws.framework.policy;

import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.SDDocumentFilter;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class WSDLPatchFilter implements SDDocumentFilter {
   private WSEndpoint endpoint;

   public WSDLPatchFilter(WSEndpoint var1) {
      this.endpoint = var1;
   }

   public XMLStreamWriter filter(SDDocument var1, XMLStreamWriter var2) throws XMLStreamException, IOException {
      return new PatchFilter(this.endpoint, var2);
   }
}
