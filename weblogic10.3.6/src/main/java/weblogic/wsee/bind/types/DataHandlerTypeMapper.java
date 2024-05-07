package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class DataHandlerTypeMapper extends AttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(DataHandlerTypeMapper.class);

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      DataHandler var5 = (DataHandler)var3;
      AttachmentPart var6 = var2.createAttachmentPart(var5);
      var6.setContentId(var1);
      return var6;
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      try {
         return var1.getDataHandler();
      } catch (SOAPException var3) {
         throw new XmlException("Failed to get Source content from the attachment", var3);
      }
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException(" deserializing DataHandler from xs:base64Binary type not supported");
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
