package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.awt.image.BufferedImage;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class ImageTypeMapper extends AttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(ImageTypeMapper.class);

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      if (var3 == null) {
         return null;
      } else {
         if (StringUtil.isEmpty(var4)) {
            var4 = "image/jpeg";
         }

         AttachmentPart var5 = var2.createAttachmentPart(new DataHandler(var3, var4));
         var5.setContentId(var1);
         return var5;
      }
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      BufferedImage var2 = null;
      if (var1 != null) {
         try {
            var2 = ImageIO.read(var1.getRawContent());
         } catch (Exception var4) {
            throw new XmlException(var4);
         }
      }

      return var2;
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException(" deserializing Image from xs:base64Binary type not supported");
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
