package weblogic.webservice.encoding;

import java.io.IOException;
import java.util.ArrayList;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.rpc.JAXRPCException;
import javax.xml.transform.Source;
import weblogic.webservice.WebServiceLogger;

/** @deprecated */
public class XMLSourceArrayCodec extends AttachmentCodec {
   private XMLSourceCodec codec = new XMLSourceCodec();

   protected Object deserializeContent(Object var1) {
      MimeMultipart var2 = (MimeMultipart)var1;

      String var4;
      try {
         int var3 = var2.getCount();
         ArrayList var11 = new ArrayList();

         for(int var5 = 0; var5 < var3; ++var5) {
            BodyPart var6 = var2.getBodyPart(var5);
            Object var7 = var6.getContent();
            Source var8 = (Source)this.codec.deserializeContent(var7);
            var11.add(var8);
         }

         return var11.toArray(new Source[var11.size()]);
      } catch (MessagingException var9) {
         var4 = WebServiceLogger.logXMLSourceEncodingMessageException();
         WebServiceLogger.logStackTrace(var4, var9);
         throw new JAXRPCException("failed to deserialize mime multipart", var9);
      } catch (IOException var10) {
         var4 = WebServiceLogger.logXMLSourceEncodingIOException();
         WebServiceLogger.logStackTrace(var4, var10);
         throw new JAXRPCException("failed to deserialize mime multipart", var10);
      }
   }

   protected String getContentType() {
      return "multipart/*";
   }

   protected Object serializeContent(Object var1) {
      if (!(var1 instanceof Source[])) {
         throw new JAXRPCException("input is not Source[] :" + var1);
      } else {
         Source[] var2 = (Source[])((Source[])var1);
         MimeMultipart var3 = new MimeMultipart();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            MimeBodyPart var5 = new MimeBodyPart();
            DataHandler var6 = new DataHandler(this.codec.serializeContent(var2[var4]), "text/xml");

            try {
               var5.setDataHandler(var6);
               var3.addBodyPart(var5, var4);
            } catch (MessagingException var8) {
               throw new JAXRPCException("failed to set data handler " + var8, var8);
            }
         }

         return var3;
      }
   }
}
