package weblogic.wsee.bind.types;

import com.bea.xbean.util.Base64;
import com.bea.xml.XmlException;
import java.io.ByteArrayInputStream;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class ByteArrayTypeMapper extends AttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(ByteArrayTypeMapper.class);
   private static String MIME_TRANSFER_ENC_HEADER = "Content-Transfer-Encoding";
   private static String DEFAULT_MIME_TYPE = "application/octet-stream";
   private String mimeType;

   public ByteArrayTypeMapper() {
      this.mimeType = DEFAULT_MIME_TYPE;
   }

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      String var5 = null;
      if (StringUtil.isEmpty(var4)) {
         var5 = DEFAULT_MIME_TYPE;
      }

      AttachmentPart var6 = var2.createAttachmentPart(var3, var5);
      var6.setContentId(var1);
      var6.addMimeHeader(MIME_TRANSFER_ENC_HEADER, "binary");

      try {
         var6.setRawContent(new ByteArrayInputStream((byte[])((byte[])var3)), var5);
         return var6;
      } catch (SOAPException var8) {
         throw new XmlException(var8.getMessage());
      }
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      throw new XmlException("unexpected call to deserializeAttachmentPart");
   }

   Object deserializeBase64Binary(String var1) {
      return Base64.decode(var1.getBytes());
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

   private void p(String var1) {
      if (verbose) {
         Verbose.log((Object)var1);
      }

   }
}
