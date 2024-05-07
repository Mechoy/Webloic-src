package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.path.helper.KeyString;

public final class BEOrderUpdateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 6686162300621162069L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private KeyString key;
   private BEUOOMember oldMember;
   private BEUOOMember newMember;

   public BEOrderUpdateRequest(JMSID var1, KeyString var2, BEUOOMember var3, BEUOOMember var4) {
      super(var1, 17940);
      this.key = var2;
      this.oldMember = var3;
      this.newMember = var4;
   }

   KeyString getKey() {
      return this.key;
   }

   BEUOOMember getOldMember() {
      return this.oldMember;
   }

   BEUOOMember getNewMember() {
      return this.newMember;
   }

   public int remoteSignature() {
      return 34;
   }

   public Response createResponse() {
      return VoidResponse.THE_ONE;
   }

   public BEOrderUpdateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      var1.writeInt(var2);
      super.writeExternal(var1);
      this.key.writeExternal(var1);
      this.oldMember.writeExternal(var1);
      this.newMember.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.key = new KeyString();
         this.key.readExternal(var1);
         this.oldMember = new BEUOOMember();
         this.oldMember.readExternal(var1);
         this.newMember = new BEUOOMember();
         this.newMember.readExternal(var1);
      }
   }
}
