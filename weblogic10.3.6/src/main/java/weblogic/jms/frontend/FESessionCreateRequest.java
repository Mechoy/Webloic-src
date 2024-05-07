package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FESessionCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 5179168005654945285L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int TRANSACTED_MASK = 256;
   private static final int XA_SESSION_MASK = 512;
   private boolean transacted;
   private boolean xaSession;
   private int acknowledgeMode;
   private transient String workManager;

   public FESessionCreateRequest(JMSID var1, boolean var2, boolean var3, int var4, String var5) {
      super(var1, 6663);
      this.transacted = var2;
      this.xaSession = var3;
      this.acknowledgeMode = var4;
      this.workManager = var5;
   }

   public final boolean getTransacted() {
      return this.transacted;
   }

   public final boolean getXASession() {
      return this.xaSession;
   }

   public final int getAcknowledgeMode() {
      return this.acknowledgeMode;
   }

   public String getPushWorkManager() {
      return this.workManager;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new FESessionCreateResponse();
   }

   public FESessionCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      byte var2 = 1;
      int var3 = var2;
      if (this.transacted) {
         var3 = var2 | 256;
      }

      if (this.xaSession) {
         var3 |= 512;
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      var1.writeShort((short)this.acknowledgeMode);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.transacted = (var2 & 256) != 0;
         this.xaSession = (var2 & 512) != 0;
         this.acknowledgeMode = var1.readShort();
      }
   }
}
