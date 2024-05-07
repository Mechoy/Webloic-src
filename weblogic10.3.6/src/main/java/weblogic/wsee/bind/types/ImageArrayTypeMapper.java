package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.awt.Image;
import java.util.ArrayList;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class ImageArrayTypeMapper extends ArrayAttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(ImageArrayTypeMapper.class);
   private static final ImageTypeMapper mapper = new ImageTypeMapper();

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      return mapper.createAttachmentPart(var1, var2, var3, var4);
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      return mapper.deserializeAttachmentPart(var1);
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException(" deserializing Image Array from xs:base64Binary type not supported");
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

   Object[] toArray(ArrayList var1) {
      return var1.toArray(new Image[var1.size()]);
   }
}
