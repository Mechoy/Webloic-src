package weblogic.webservice.encoding;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.xml.schema.binding.DeserializationContext;
import weblogic.xml.schema.binding.SerializationContext;
import weblogic.xml.stream.XMLName;

/** @deprecated */
public class MimeMultipartCodec extends AttachmentCodec {
   protected String getContentType() {
      throw new Error("should not be called");
   }

   protected Object serializeContent(Object var1) {
      throw new Error("should not be called");
   }

   protected Object deserializeContent(Object var1) {
      throw new Error("should not be called");
   }

   protected Object deserialize(XMLName var1, SOAPMessage var2, DeserializationContext var3) throws JAXRPCException {
      AttachmentPart var4 = this.getAttachmentPart(var1, var2, var3);
      if (var4 == null) {
         return null;
      } else {
         try {
            MimeMultipart var5 = (MimeMultipart)var4.getContent();
            return var5;
         } catch (SOAPException var6) {
            throw new JAXRPCException("unable to deserialize", var6);
         }
      }
   }

   protected void serialize(Object var1, XMLName var2, SOAPMessage var3, SerializationContext var4) throws SOAPFaultException {
      try {
         this.addBodyElement(var2, var3);
      } catch (SOAPException var11) {
         throw new JAXRPCException("failed to serialize the attachment " + var2, var11);
      }

      if (var1 != null) {
         MimeMultipart var5 = (MimeMultipart)var1;
         AttachmentPart var6 = var3.createAttachmentPart();
         var6.setContent(var5, var5.getContentType());
         var6.setContentId("<" + var2.getLocalName() + ">");

         try {
            int var7 = var5.getCount();

            for(int var8 = 0; var8 < var7; ++var8) {
               MimeBodyPart var9 = (MimeBodyPart)((MimeBodyPart)var5.getBodyPart(var8));
               String var10 = var9.getDataHandler().getContentType();
               var9.setHeader("Content-Type", var10);
            }
         } catch (MessagingException var12) {
            throw new JAXRPCException("failed to set the contentType to the attachment part", var12);
         }

         var3.addAttachmentPart(var6);
      }
   }
}
