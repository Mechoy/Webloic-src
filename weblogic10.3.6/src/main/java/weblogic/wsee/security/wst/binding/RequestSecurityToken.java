package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.marshal.MarshalException;

public class RequestSecurityToken extends RSTBase {
   private static final long serialVersionUID = -6063086753169386981L;
   public static final String NAME = "RequestSecurityToken";
   private CancelTarget ct;
   private RenewTarget rt;
   private AllowPostdating ap;
   private Renewing re;
   private OnBehalfOf obo;

   public RequestSecurityToken() {
   }

   public RequestSecurityToken(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public CancelTarget getCancelTarget() {
      return this.ct;
   }

   public void setCancelTarget(CancelTarget var1) {
      this.ct = var1;
   }

   public RenewTarget getRenewTarget() {
      return this.rt;
   }

   public void setRenewTarget(RenewTarget var1) {
      this.rt = var1;
   }

   public AllowPostdating getAllowPostdating() {
      return this.ap;
   }

   public void setAllowPostdating(AllowPostdating var1) {
      this.ap = var1;
   }

   public void setRenewing(Renewing var1) {
      this.re = var1;
   }

   public Renewing getRenewing() {
      return this.re;
   }

   public void setOnBehalfOf(OnBehalfOf var1) {
      this.obo = var1;
   }

   public OnBehalfOf getOnBehalfOf() {
      return this.obo;
   }

   public String getName() {
      return "RequestSecurityToken";
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      this.marshalRSTContents(var1, var2);
      if (this.requestType == null) {
         throw new MarshalException("RequestType must be specified");
      } else {
         this.requestType.marshal(var1, (Node)null, var2);
         if (this.ct != null) {
            this.ct.marshal(var1, (Node)null, var2);
         }

         if (this.rt != null) {
            this.rt.marshal(var1, (Node)null, var2);
         }

         if (this.ap != null) {
            this.ap.marshal(var1, (Node)null, var2);
         }

         if (this.re != null) {
            this.re.marshal(var1, (Node)null, var2);
         }

         if (this.obo != null) {
            this.obo.marshal(var1, (Node)null, var2);
         }

      }
   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.unmarshalRSTContents(var1);
      Element var2 = getElementByTagName(var1, "RequestType", false);
      this.requestType = new RequestType(var2.getNamespaceURI());
      this.requestType.unmarshal(var2);
      Element var3 = getElementByTagName(var1, "CancelTarget", true);
      if (var3 != null) {
         this.ct = new CancelTarget(var3.getNamespaceURI());
         this.ct.setTokenHandler(this.tokenHandler);
         this.ct.unmarshal(var3);
      }

      Element var4 = getElementByTagName(var1, "RenewTarget", true);
      if (var4 != null) {
         this.rt = new RenewTarget(var4.getNamespaceURI());
         this.rt.setTokenHandler(this.tokenHandler);
         this.rt.unmarshal(var4);
      }

      Element var5 = getElementByTagName(var1, "AllowPostdating", true);
      if (var5 != null) {
         this.ap = new AllowPostdating(var5.getNamespaceURI());
         this.ap.unmarshal(var5);
      }

      Element var6 = getElementByTagName(var1, "Renewing", true);
      if (var6 != null) {
         this.re = new Renewing(var6.getNamespaceURI());
         this.re.unmarshal(var6);
      }

      Element var7 = getElementByTagName(var1, "OnBehalfOf", true);
      if (var7 != null) {
         this.obo = new OnBehalfOf(var7.getNamespaceURI());
         this.obo.unmarshal(var7);
      }

   }
}
