package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.marshal.MarshalException;

public class RequestSecurityTokenResponse extends RSTBase {
   public static final String NAME = "RequestSecurityTokenResponse";
   private RequestedSecurityToken rst;
   private RequestedAttachedReference rar;
   private RequestedUnattachedReference rur;
   private RequestedProofToken rpt;
   private RequestedTokenCancelled rtc;

   public RequestSecurityTokenResponse() {
   }

   public RequestSecurityTokenResponse(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setRequestedSecurityToken(RequestedSecurityToken var1) {
      this.rst = var1;
   }

   public RequestedSecurityToken getRequestedSecurityToken() {
      return this.rst;
   }

   public void setRequestedAttachedReference(RequestedAttachedReference var1) {
      this.rar = var1;
   }

   public RequestedAttachedReference getRequestedAttachedReference() {
      return this.rar;
   }

   public void setRequestedUnattachedReference(RequestedUnattachedReference var1) {
      this.rur = var1;
   }

   public RequestedUnattachedReference getRequestedUnattachedReference() {
      return this.rur;
   }

   public void setRequestedProofToken(RequestedProofToken var1) {
      this.rpt = var1;
   }

   public RequestedProofToken getRequestedProofToken() {
      return this.rpt;
   }

   public void setRequestedTokenCancelled(RequestedTokenCancelled var1) {
      this.rtc = var1;
   }

   public RequestedTokenCancelled getRequestedTokenCancelled() {
      return this.rtc;
   }

   public String getName() {
      return "RequestSecurityTokenResponse";
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      this.marshalRSTContents(var1, var2);
      if (this.requestType != null) {
         this.requestType.marshal(var1, (Node)null, var2);
      }

      if (this.rst != null) {
         this.rst.marshal(var1, (Node)null, var2);
      }

      if (this.rar != null) {
         this.rar.marshal(var1, (Node)null, var2);
      }

      if (this.rur != null) {
         this.rur.marshal(var1, (Node)null, var2);
      }

      if (this.rpt != null) {
         this.rpt.marshal(var1, (Node)null, var2);
      }

      if (this.rtc != null) {
         this.rtc.marshal(var1, (Node)null, var2);
      }

   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.unmarshalRSTContents(var1);
      Element var2 = getElementByTagName(var1, "RequestType", true);
      if (var2 != null) {
         this.requestType = new RequestType(var2.getNamespaceURI());
         this.requestType.unmarshal(var2);
      }

      Element var3 = getElementByTagName(var1, "RequestedSecurityToken", true);
      if (var3 != null) {
         this.rst = new RequestedSecurityToken(var3.getNamespaceURI());
         this.rst.setTokenHandler(this.tokenHandler);
         if (null != this.getTokenType()) {
            this.rst.setTokenType(this.getTokenType().getTokenType());
         }

         this.rst.unmarshal(var3);
      }

      Element var4 = getElementByTagName(var1, "RequestedAttachedReference", true);
      if (var4 != null) {
         this.rar = new RequestedAttachedReference(var4.getNamespaceURI());
         this.rar.setTokenHandler(this.tokenHandler);
         this.rar.unmarshal(var4);
      }

      Element var5 = getElementByTagName(var1, "RequestedUnattachedReference", true);
      if (var5 != null) {
         this.rur = new RequestedUnattachedReference(var5.getNamespaceURI());
         this.rur.setTokenHandler(this.tokenHandler);
         this.rur.unmarshal(var5);
      }

      Element var6 = getElementByTagName(var1, "RequestedProofToken", true);
      if (var6 != null) {
         this.rpt = new RequestedProofToken(var6.getNamespaceURI());
         this.rpt.unmarshal(var6);
      }

      Element var7 = getElementByTagName(var1, "RequestedTokenCancelled", true);
      if (var7 != null) {
         this.rtc = new RequestedTokenCancelled(var7.getNamespaceURI());
      }

   }
}
