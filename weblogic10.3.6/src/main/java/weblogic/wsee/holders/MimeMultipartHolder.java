package weblogic.wsee.holders;

import javax.mail.internet.MimeMultipart;
import javax.xml.rpc.holders.Holder;

public class MimeMultipartHolder implements Holder {
   public MimeMultipart value;

   public MimeMultipartHolder() {
   }

   public MimeMultipartHolder(MimeMultipart var1) {
      this.value = var1;
   }
}
