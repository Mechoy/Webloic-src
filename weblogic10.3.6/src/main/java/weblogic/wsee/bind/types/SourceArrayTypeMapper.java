package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.util.ArrayList;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class SourceArrayTypeMapper extends ArrayAttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(SourceArrayTypeMapper.class);
   private SourceTypeMapper mapper = new SourceTypeMapper();

   AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException {
      return this.mapper.createAttachmentPart(var1, var2, var3, var4);
   }

   Object deserializeAttachmentPart(AttachmentPart var1) throws XmlException {
      return this.mapper.deserializeAttachmentPart(var1);
   }

   Object deserializeBase64Binary(String var1) throws XmlException {
      throw new XmlException("encoding Source Array as XMLType xs:base64Binary not supported");
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
      return var1.toArray(new Source[var1.size()]);
   }
}
