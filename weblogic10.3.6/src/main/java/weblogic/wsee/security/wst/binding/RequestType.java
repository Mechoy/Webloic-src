package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class RequestType extends TrustDOMStructure {
   public static final String NAME = "RequestType";
   private String requestType;

   public RequestType() {
   }

   public RequestType(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setRequestType(String var1) {
      this.requestType = var1;
   }

   public String getRequestType() {
      return this.requestType;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.requestType == null) {
         throw new MarshalException("requestType must not be null");
      } else {
         addTextContent(var1, this.requestType);
      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.requestType = getTextContent(var1);
   }

   public String getName() {
      return "RequestType";
   }
}
