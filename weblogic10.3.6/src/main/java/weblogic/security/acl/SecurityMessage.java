package weblogic.security.acl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.cluster.GroupMessage;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;

/** @deprecated */
public final class SecurityMessage implements GroupMessage, Externalizable {
   private static final long serialVersionUID = -5638732491394667306L;
   private int nextSeqNo;
   private SecurityMulticastRecord record;

   public int nextSeqNo() {
      return this.nextSeqNo;
   }

   public SecurityMulticastRecord record() {
      return this.record;
   }

   public SecurityMessage(int var1, SecurityMulticastRecord var2) {
      this.nextSeqNo = var1;
      this.record = var2;
   }

   public String toString() {
      return "SecurityMessage record:" + this.record + " nextSeqNo:" + this.nextSeqNo;
   }

   public void execute(HostID var1) {
      Security.receiveSecurityMessage(var1, this);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.nextSeqNo);
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeObjectWL(this.record);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.nextSeqNo = var1.readInt();
      WLObjectInput var2 = (WLObjectInput)var1;
      this.record = (SecurityMulticastRecord)var2.readObjectWL();
   }

   public SecurityMessage() {
   }
}
