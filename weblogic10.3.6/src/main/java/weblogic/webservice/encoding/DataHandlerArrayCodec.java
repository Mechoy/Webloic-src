package weblogic.webservice.encoding;

import java.util.ArrayList;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
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
public class DataHandlerArrayCodec extends AttachmentCodec {
   protected String getContentType() {
      throw new Error("should not be called");
   }

   protected Object serializeContent(Object var1) {
      throw new Error("should not be called");
   }

   protected Object deserializeContent(Object var1) {
      throw new Error("should not be called");
   }

   protected Object deserialize(XMLName var1, SOAPMessage var2, DeserializationContext var3) {
      AttachmentPart var4 = this.getAttachmentPart(var1, var2, var3);
      if (var4 == null) {
         return null;
      } else {
         try {
            MimeMultipart var5 = (MimeMultipart)var4.getContent();
            ArrayList var6 = new ArrayList();

            for(int var7 = 0; var7 < var5.getCount(); ++var7) {
               BodyPart var8 = var5.getBodyPart(var7);
               DataHandler var9 = var8.getDataHandler();
               var6.add(var9);
            }

            return var6.toArray(new DataHandler[var6.size()]);
         } catch (SOAPException var10) {
            throw new JAXRPCException("failed to deserialize:" + var4, var10);
         } catch (MessagingException var11) {
            throw new JAXRPCException("failed to deserialize mime multipart", var11);
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
         DataHandler[] var5 = (DataHandler[])((DataHandler[])var1);
         MimeMultipart var6 = new MimeMultipart();

         for(int var7 = 0; var7 < var5.length; ++var7) {
            MimeBodyPart var8 = new MimeBodyPart();

            try {
               var8.setDataHandler(var5[var7]);
               var6.addBodyPart(var8);
            } catch (MessagingException var10) {
               throw new JAXRPCException("failed to serialize", var10);
            }
         }

         AttachmentPart var12 = var3.createAttachmentPart();
         var12.setContent(var6, var6.getContentType());
         var12.setContentId("<" + var2.getLocalName() + ">");
         var12.setMimeHeader("Content-Type", var6.getContentType());
         var3.addAttachmentPart(var12);
      }
   }
}
