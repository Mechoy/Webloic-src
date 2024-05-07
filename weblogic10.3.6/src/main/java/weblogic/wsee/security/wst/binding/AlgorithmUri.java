package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.wsee.util.Verbose;
import weblogic.xml.dom.marshal.MarshalException;

public abstract class AlgorithmUri extends TrustDOMStructure {
   private static final boolean verbose = Verbose.isVerbose(AlgorithmUri.class);
   private static final boolean debug = false;
   protected String uri;

   public void setUri(String var1) {
      this.uri = var1;
   }

   public String getUri() {
      return this.uri;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.uri == null) {
         throw new MarshalException("uri of the Algorithm can not be null");
      } else {
         addTextContent(var1, this.uri);
      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.uri = getTextContent(var1);
   }

   public abstract String getName();
}
