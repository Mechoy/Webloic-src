package weblogic.wsee.security.wssc.base.sct;

import java.io.Serializable;
import java.security.Key;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.security.wssc.SecurityTokenBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wst.framework.TrustCredential;
import weblogic.wsee.security.wst.framework.TrustToken;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.Util;
import weblogic.xml.dom.marshal.MarshalException;

public abstract class SCTokenBase extends SecurityTokenBase implements TrustToken, Serializable {
   private SCCredential credential;
   private static final boolean verbose = Verbose.isVerbose(SCTokenBase.class);
   private String netCV;
   private QName netCNS;

   public SCTokenBase() {
   }

   public SCTokenBase(SCCredential var1) {
      this.credential = var1;
      this.setId(var1.getTokenId());
      if (var1.getScNamespace() == null) {
         var1.setScNamespace(this.getXMLNS_WSC());
      } else if (var1.getScNamespace().length() <= 0) {
         var1.setScNamespace(this.getXMLNS_WSC());
      } else {
         assert var1.getScNamespace().equals(this.getXMLNS_WSC()) : "Error ! input SCCredential.getSCNamespace()='" + var1.getScNamespace() + "', but the SCToken is being created in the namespace '" + this.getXMLNS_WSC() + "'.  The 2 SC version namespaces are supposed to be equal !";
      }

   }

   protected abstract QName getSCT_IDENTIFIER_QNAME();

   protected abstract QName getSCT_QNAME();

   protected abstract String getSCT_VALUE_TYPE();

   protected abstract String getXMLNS_WSC();

   public Element marshalInternal(Element var1, Node var2, Map var3) throws MarshalException {
      Element var4 = DOMUtils.createElement(var1, this.getSCT_QNAME(), "wsc");
      String var5 = this.credential.getSecurityContextTokenIDAttribute();
      if (var5 != null) {
         var4.setAttributeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu:Id", var5);
      }

      weblogic.xml.dom.DOMUtils.addNamespaceDeclaration(var4, "wsc", this.getXMLNS_WSC());
      Element var6 = DOMUtils.createAndAddElement(var4, this.getSCT_IDENTIFIER_QNAME(), "wsc");
      DOMUtils.addText(var6, this.credential.getIdentifier());
      String var7 = this.credential.getNETCookieValue();
      if (var7 != null && var7.length() > 0) {
         Element var8 = DOMUtils.createAndAddElement(var4, this.credential.getNETCookieQName(), "wlsNetCookie");
         DOMUtils.addText(var8, var7);
         if (verbose) {
            Verbose.log((Object)("detected and marshalled .NET SCT Cookie.  XML value: \n" + Util.printNode(var8) + "\n"));
         }
      }

      if (var2 != null) {
         var1.insertBefore(var4, var2);
      } else {
         var1.appendChild(var4);
      }

      return var4;
   }

   public Element unmarshalInternal(Node var1) throws MarshalException {
      Element var2 = (Element)var1;
      String var3 = null;
      QName var4 = null;
      String var5 = null;
      String var6 = null;

      try {
         Element var7 = weblogic.xml.dom.DOMUtils.getElementByTagNameNS(var2, this.getXMLNS_WSC(), "Identifier");
         var3 = weblogic.xml.dom.DOMUtils.getTextContent(var7, true);
         Element var8 = null;

         try {
            var8 = weblogic.xml.dom.DOMUtils.getOptionalElementByLocalName(var2, "Cookie");
         } catch (DOMProcessingException var10) {
            if (verbose) {
               Verbose.log((Object)" ignoring exception obtained while looking for optional .NET SCT Cookie 'Cookie'");
            }
         }

         if (var8 != null) {
            var5 = weblogic.xml.dom.DOMUtils.getTextContent(var8, false);
            var4 = new QName(var8.getNamespaceURI(), var8.getLocalName());
            if (verbose) {
               Verbose.log((Object)("\n SCT unmarshalled NET SCT Cookie. Name='" + var4.toString() + "', Value='" + var5 + "'\n"));
            }

            Attr var9 = var2.getAttributeNodeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id");
            if (var9 != null) {
               var6 = var9.getValue();
            }
         }
      } catch (DOMProcessingException var11) {
         throw new MarshalException("Token '" + this.getValueType() + "' missing element - " + "Identifier");
      }

      this.credential = new SCCredential();
      this.credential.setIdentifier(var3);
      this.credential.setScNamespace(this.getXMLNS_WSC());
      this.credential.setTokenId(DOMUtils.getAttributeValue(var2, WSSConstants.WSU_ID_QNAME));
      this.credential.setNETCookieValue(var5);
      this.credential.setNETCookieQName(var4);
      this.credential.setSecurityContextTokenIDAttribute(var6);
      return var2;
   }

   public String getValueType() {
      return this.getSCT_VALUE_TYPE();
   }

   public Key getSecretKey() {
      return this.credential.getSecret();
   }

   public SCCredential getCredential() {
      return this.credential;
   }

   void setCredential(SCCredential var1) {
      this.credential = var1;
   }

   public TrustCredential getTrustCredential() {
      return this.credential;
   }

   public int hashCode() {
      return this.credential != null ? this.credential.getIdentifier().hashCode() : super.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SCTokenBase) {
         SCTokenBase var2 = (SCTokenBase)var1;
         if (this.getCredential() != null && var2.getCredential() != null) {
            return this.getCredential().equals(var2.getCredential());
         }
      }

      return false;
   }
}
