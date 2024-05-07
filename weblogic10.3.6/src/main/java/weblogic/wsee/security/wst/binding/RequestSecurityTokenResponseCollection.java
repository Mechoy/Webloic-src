package weblogic.wsee.security.wst.binding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.marshal.MarshalException;

public class RequestSecurityTokenResponseCollection extends RSTBase {
   public static final String NAME = "RequestSecurityTokenResponseCollection";
   private List<RequestSecurityTokenResponse> requestSecurityTokenResponseList;

   public RequestSecurityTokenResponseCollection() {
   }

   public RequestSecurityTokenResponseCollection(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void addRequestSecurityTokenResponse(RequestSecurityTokenResponse var1) {
      if (this.requestSecurityTokenResponseList == null) {
         this.requestSecurityTokenResponseList = new ArrayList();
      }

      this.requestSecurityTokenResponseList.add(var1);
   }

   public List<RequestSecurityTokenResponse> getRequestSecurityTokenResponseCollection() {
      if (this.requestSecurityTokenResponseList == null) {
         this.requestSecurityTokenResponseList = new ArrayList();
      }

      return this.requestSecurityTokenResponseList;
   }

   public String getName() {
      return "RequestSecurityTokenResponseCollection";
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.requestSecurityTokenResponseList != null) {
         if (this.requestSecurityTokenResponseList.size() > 0) {
            Iterator var3 = this.requestSecurityTokenResponseList.iterator();

            while(var3.hasNext()) {
               RequestSecurityTokenResponse var4 = (RequestSecurityTokenResponse)var3.next();
               var4.marshal(var1, (Node)null, var2);
            }

         }
      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var3 = getFirstElement(var1);
      if (var3 != null) {
         RequestSecurityTokenResponse var2 = new RequestSecurityTokenResponse(var1.getNamespaceURI());
         var2.setTokenHandler(this.tokenHandler);
         this.addRequestSecurityTokenResponse(var2);
         var2.unmarshal(var3);

         for(var3 = getNextSiblingElement(var3); var3 != null; var3 = getNextSiblingElement(var3)) {
            var2 = new RequestSecurityTokenResponse(var1.getNamespaceURI());
            var2.setTokenHandler(this.tokenHandler);
            this.addRequestSecurityTokenResponse(var2);
            var2.unmarshal(var3);
         }
      }

   }
}
