package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import weblogic.wsee.bind.SchemaConstants;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public abstract class AttachmentBase implements TypeMapper {
   private static final boolean verbose = Verbose.isVerbose(AttachmentBase.class);
   private static final QName XOP_INCLUDE_QNAME = new QName("http://www.w3.org/2004/08/xop/include", "Include");

   protected SOAPElement addChildElement(SOAPElement var1, QName var2) throws XmlException {
      try {
         SOAPElement var3 = var1.addChildElement(var2.getLocalPart(), var2.getPrefix(), var2.getNamespaceURI());
         return var3;
      } catch (SOAPException var5) {
         throw new XmlException("Failed to serialize source array", var5);
      }
   }

   public void serializeType(SOAPElement var1, Object var2, Class var3, QName var4, QName var5, SerializerContext var6, boolean var7, String var8) throws XmlException {
      SOAPElement var9 = this.addChildElement(var1, var5);
      SOAPMessage var10 = var6.getMessage();
      if (var2 != null) {
         String var11 = var4.getLocalPart();
         String var12 = null;
         var12 = var5.getLocalPart();
         this.writeHref(var10, var9, var12);
         AttachmentPart var13 = this.createAttachmentPart(this.addAngleBrackets(var12), var10, var2, var8);
         var10.addAttachmentPart(var13);
      }

   }

   protected void writeHref(SOAPMessage var1, SOAPElement var2, String var3) throws XmlException {
      try {
         Name var4 = var1.getSOAPPart().getEnvelope().createName("href");
         var2.addAttribute(var4, "cid:" + var3);
      } catch (SOAPException var5) {
         throw new XmlException("Failed to add href attribute", var5);
      }
   }

   abstract AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException;

   public Object deserializeType(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException {
      String var6;
      if (verbose) {
         var6 = DOMUtils.toXMLString(var1);
         Verbose.log((Object)(" deserializeType called with SOAPElement " + var6 + "\n"));
      }

      var6 = this.getCid(var1);
      if (verbose) {
         Verbose.log((Object)("CID = " + var6));
      }

      if (var6 == null) {
         return null;
      } else {
         SOAPMessage var7 = var4.getMessage();
         Iterator var8 = var7.getAttachments();

         AttachmentPart var9;
         do {
            if (!var8.hasNext()) {
               throw new XmlException("Unable to find attachment part for href = " + var6);
            }

            var9 = (AttachmentPart)var8.next();
            if (verbose) {
               this.dumpHeader(var9);
            }

            if (('<' + var6 + '>').equals(var9.getContentId())) {
               return this.deserializeAttachmentPart(var9);
            }
         } while(!var6.equals(var9.getContentId()));

         return this.deserializeAttachmentPart(var9);
      }
   }

   public void serializeElement(SOAPElement var1, Object var2, Class var3, QName var4, SerializerContext var5, boolean var6, String var7) throws XMLStreamException, XmlException {
      this.serializeType(var1, var2, var3, SchemaConstants.any, var4, var5, var6, var7);
   }

   public Object deserializeElement(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException, XMLStreamException {
      return this.deserializeType(var1, var2, SchemaConstants.any, var4, var5);
   }

   private void dumpHeader(AttachmentPart var1) {
      Iterator var2 = var1.getAllMimeHeaders();

      while(var2.hasNext()) {
         MimeHeader var3 = (MimeHeader)var2.next();
         Verbose.log((Object)("Mime Header: " + var3.getName() + " = " + var3.getValue()));
      }

   }

   abstract Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException;

   abstract Object deserializeBase64Binary(String var1) throws XmlException;

   private String getCid(SOAPElement var1) {
      String var2 = var1.getAttribute("href");
      if (var2 != null && !"".equals(var2)) {
         return var2.startsWith("cid:") ? var2.substring("cid:".length(), var2.length()) : var2;
      } else {
         return null;
      }
   }

   private String addAngleBrackets(String var1) {
      String var2 = var1;
      if (var1 != null && var1.length() > 0 && (var1.charAt(0) != '<' || var1.charAt(var1.length() - 1) != '>')) {
         var2 = '<' + var1 + '>';
      }

      return var2;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
