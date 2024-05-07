package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.dispatcher.Response;

public final class JMSBrowserGetEnumerationResponse extends Response implements Externalizable {
   static final long serialVersionUID = 8459890622485517494L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private JMSID enumerationId;

   public JMSBrowserGetEnumerationResponse(JMSID var1) {
      this.enumerationId = var1;
   }

   public final JMSID getEnumerationId() {
      return this.enumerationId;
   }

   public JMSBrowserGetEnumerationResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      var1.writeInt(1);
      super.writeExternal(var1);
      this.enumerationId.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         JMSUtilities.versionIOException(var3, 1, 1);
      }

      super.readExternal(var1);
      this.enumerationId = new JMSID();
      this.enumerationId.readExternal(var1);
   }
}
