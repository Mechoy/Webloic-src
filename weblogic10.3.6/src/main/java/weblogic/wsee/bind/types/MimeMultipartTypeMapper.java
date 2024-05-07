package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import javax.mail.internet.MimeMultipart;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class MimeMultipartTypeMapper extends AttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(MimeMultipartTypeMapper.class);

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      MimeMultipart var5 = (MimeMultipart)var3;
      if (StringUtil.isEmpty(var4)) {
         var4 = "multipart/*";
      }

      AttachmentPart var6 = var2.createAttachmentPart(var5, var4);
      var6.setContentId(var1);
      var6.setMimeHeader("Content-Type", var5.getContentType());
      return var6;
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      try {
         return (MimeMultipart)var1.getContent();
      } catch (SOAPException var3) {
         throw new XmlException("Failed to get Source content from the attachment", var3);
      }
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException(" deserializing MimeMultiPart from xs:base64Binary type not supported");
   }
}
