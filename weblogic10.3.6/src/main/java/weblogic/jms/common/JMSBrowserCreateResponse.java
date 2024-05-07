package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Response;

public final class JMSBrowserCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = 4858043004642723716L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private JMSID browserId;

   public JMSBrowserCreateResponse(JMSID var1) {
      this.browserId = var1;
   }

   public JMSID getBrowserId() {
      return this.browserId;
   }

   public JMSBrowserCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      var1.writeInt(1);
      super.writeExternal(var1);
      this.browserId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.browserId = new JMSID();
         this.browserId.readExternal(var1);
      }
   }
}
