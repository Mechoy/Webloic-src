package weblogic.webservice.encoding;

import java.util.Iterator;
import javax.mail.internet.MimeMultipart;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.xml.schema.binding.DeserializationContext;
import weblogic.xml.schema.binding.DeserializationException;
import weblogic.xml.schema.binding.SerializationContext;
import weblogic.xml.schema.binding.SerializationException;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

/** @deprecated */
public abstract class AttachmentCodec extends AbstractCodec {
   protected Object deserialize(XMLName var1, SOAPMessage var2, DeserializationContext var3) throws JAXRPCException {
      AttachmentPart var4 = this.getAttachmentPart(var1, var2, var3);
      if (var4 == null) {
         return null;
      } else {
         try {
            return this.deserializeContent(var4.getContent());
         } catch (SOAPException var6) {
            throw new JAXRPCException("failed to deserialize:" + var4, var6);
         }
      }
   }

   protected abstract Object deserializeContent(Object var1);

   private SOAPElement getFirstChild(SOAPElement var1) throws SOAPException {
      Iterator var2 = var1.getChildElements();
      return var2.hasNext() ? (SOAPElement)var2.next() : null;
   }

   protected void addBodyElement(XMLName var1, SOAPMessage var2) throws SOAPException {
      SOAPEnvelope var3 = var2.getSOAPPart().getEnvelope();
      String var4 = var1.getPrefix();
      String var5 = var1.getNamespaceUri();
      if (var5 != null) {
         var4 = var4 == null ? "ns" : var4;
      }

      Name var6 = var3.createName(var1.getLocalName(), var4, var5);
      SOAPBody var7 = var3.getBody();
      SOAPElement var8 = this.getFirstChild(var7);
      if (var8 == null) {
         var8 = var7.addChildElement(var6);
      } else {
         var8 = var8.addChildElement(var6);
      }

      Name var9 = var3.createName("href");
      var8.addAttribute(var9, "cid:" + var1.getLocalName());
   }

   protected void serialize(Object var1, XMLName var2, SOAPMessage var3, SerializationContext var4) throws SOAPFaultException {
      try {
         this.addBodyElement(var2, var3);
      } catch (SOAPException var7) {
         throw new JAXRPCException("failed to serialize the attachment " + var2, var7);
      }

      if (var1 != null) {
         Object var5 = this.serializeContent(var1);
         AttachmentPart var6 = var3.createAttachmentPart();
         var6.setContent(var5, this.getContentType());
         var6.setContentId("<" + var2.getLocalName() + ">");
         if (var5 instanceof MimeMultipart) {
            var6.setMimeHeader("Content-Type", ((MimeMultipart)var5).getContentType());
         }

         var3.addAttachmentPart(var6);
      }
   }

   protected abstract Object serializeContent(Object var1);

   protected abstract String getContentType();

   protected AttachmentPart getAttachmentPart(XMLName var1, SOAPMessage var2, DeserializationContext var3) {
      SOAPElement var4 = var3.getSOAPElement();
      String var5 = null;
      if (var4 != null) {
         try {
            SOAPEnvelope var6 = var2.getSOAPPart().getEnvelope();
            Name var7 = var6.createName("href");
            var5 = this.cleanHrefId(var4.getAttributeValue(var7));
         } catch (SOAPException var8) {
            throw new JAXRPCException(var8);
         }
      }

      AttachmentPart var9 = null;
      if (var5 != null) {
         var9 = this.getAttachmentPartFromName(var5, var2);
      }

      if (var9 == null) {
         var9 = this.getAttachmentPartFromName(var1.getLocalName(), var2);
      }

      return var9;
   }

   private String cleanHrefId(String var1) {
      if (var1 != null && var1.startsWith("cid:")) {
         var1 = var1.substring("cid:".length(), var1.length());
      }

      return var1;
   }

   private AttachmentPart getAttachmentPartFromName(String var1, SOAPMessage var2) {
      Iterator var3 = var2.getAttachments();

      AttachmentPart var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (AttachmentPart)var3.next();
         if (var1.equals(var4.getContentId())) {
            return var4;
         }
      } while(!var1.equals(this.cleanId(var4.getContentId())));

      return var4;
   }

   private String cleanId(String var1) {
      if (var1 != null && var1.startsWith("<")) {
         var1 = var1.substring(1, var1.length());
      }

      if (var1 != null && var1.endsWith(">")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      return var1;
   }

   public final Object deserialize(XMLName var1, XMLInputStream var2, DeserializationContext var3) throws DeserializationException {
      SOAPMessage var4 = var3.getSOAPMessage();
      if (var4 == null) {
         throw new DeserializationException("Unable to find message inside the DeserializationContext");
      } else {
         return this.deserialize(var1, var4, var3);
      }
   }

   public final Object deserialize(XMLName var1, Attribute var2, DeserializationContext var3) throws DeserializationException {
      throw new DeserializationException("SOAPElementCodec does not support Attribute deserialization");
   }

   public final void serialize(Object var1, XMLName var2, XMLOutputStream var3, SerializationContext var4) throws SerializationException {
      SOAPMessage var5 = var4.getSOAPMessage();
      if (var5 == null) {
         throw new SerializationException("Unable to find message inside the SerializationContext");
      } else {
         this.serialize(var1, var2, var5, var4);
      }
   }
}
