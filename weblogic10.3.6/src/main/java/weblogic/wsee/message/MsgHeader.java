package weblogic.wsee.message;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMUtils;
import weblogic.xml.domimpl.DocumentImpl;

public abstract class MsgHeader implements Serializable {
   private static final long serialVersionUID = -7520245755108809744L;
   private boolean mustUnderstand = false;
   private String role;
   private boolean refParam = false;
   private boolean hasDuplicated = false;
   private transient String currentSoapEnvelopeNamespace;

   public abstract MsgHeaderType getType();

   public abstract QName getName();

   public abstract void read(Element var1) throws MsgHeaderException;

   public abstract void write(Element var1) throws MsgHeaderException;

   public boolean isMultiple() {
      return false;
   }

   protected String getCurrentSoapEnvelopeNamespace() {
      return this.currentSoapEnvelopeNamespace;
   }

   public Element writeToParent(Element var1) throws MsgHeaderException {
      String var2 = var1.getLocalName();
      if (null == var2) {
         var2 = var1.getNodeName();
         if (null != var2) {
            int var3 = var2.indexOf(58);
            if (var3 >= 0) {
               var2 = var2.substring(var3 + 1);
            }
         }
      }

      if ("Header".equals(var2)) {
         this.currentSoapEnvelopeNamespace = this.getSoapNamespace(var1);
      }

      QName var6 = this.getName();
      Object var4;
      if (var6.getPrefix() != null && var6.getPrefix().length() > 0) {
         var4 = var1.getOwnerDocument().createElementNS(var6.getNamespaceURI(), var6.getPrefix() + ":" + var6.getLocalPart());
         DOMUtils.addNamespaceDeclaration(var1, var6.getPrefix(), var6.getNamespaceURI());
      } else {
         Document var5 = var1.getOwnerDocument();
         if (var5 instanceof DocumentImpl) {
            var4 = ((DocumentImpl)var5).createElementNS(var6.getNamespaceURI(), var6.getLocalPart(), "");
         } else {
            var4 = var5.createElementNS(var6.getNamespaceURI(), var6.getLocalPart());
         }
      }

      if (this.refParam) {
         DOMUtils.addNamespaceDeclaration((Element)var4, "wsa", "http://www.w3.org/2005/08/addressing");
         ((Element)var4).setAttributeNS("http://www.w3.org/2005/08/addressing", "wsa:IsReferenceParameter", "true");
      }

      this.addStandardNamespaces((Element)var4);
      this.write((Element)var4);
      var1.appendChild((Node)var4);
      return (Element)var4;
   }

   protected void addStandardNamespaces(Element var1) {
      DOMUtils.addNamespaceDeclaration(var1, "wsu", "http://schemas.xmlsoap.org/ws/2002/07/utility");
      if (this.isMustUnderstand()) {
         DOMUtils.addNamespaceDeclaration(var1, "env", this.getCurrentSoapEnvelopeNamespace());
      }

   }

   public void setMustUnderstand(boolean var1) {
      this.mustUnderstand = var1;
   }

   public boolean isMustUnderstand() {
      return this.mustUnderstand;
   }

   public void setRefParam(boolean var1) {
      this.refParam = var1;
   }

   public boolean isRefParam() {
      return this.refParam;
   }

   public void setDuplicated(boolean var1) {
      this.hasDuplicated = var1;
   }

   public boolean hasDuplicated() {
      return this.hasDuplicated;
   }

   public String getRole() {
      return this.role;
   }

   public void setRole(String var1) {
      this.role = var1;
   }

   public void toString(ToStringWriter var1) {
      var1.writeField("name", this.getName());
      var1.writeField("role", this.role);
      var1.writeField("mustUnderstand", new Boolean(this.mustUnderstand));
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   private String getSoapNamespace(Element var1) {
      assert var1 != null;

      String var2 = var1.getNamespaceURI();
      return !"http://schemas.xmlsoap.org/soap/envelope/".equals(var2) && !"http://www.w3.org/2003/05/soap-envelope".equals(var2) ? null : var2;
   }
}
