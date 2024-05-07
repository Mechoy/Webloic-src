package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;

final class RuntimeStateMessage implements GroupMessage, Externalizable {
   private static final long serialVersionUID = 2610797619397430239L;
   private int srvrRuntimeState;
   private MemberAttributes memberAttributes;
   private long currentSeqNum = -1L;

   RuntimeStateMessage(int var1, MemberAttributes var2, long var3) {
      this.srvrRuntimeState = var1;
      this.memberAttributes = var2;
      this.currentSeqNum = var3;
   }

   public void execute(HostID var1) {
      this.processAttributes(this.memberAttributes);
      MemberManager.theOne().updateMemberRuntimeState(var1, this.srvrRuntimeState, this.currentSeqNum);
   }

   public String toString() {
      return "State:" + this.srvrRuntimeState;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ((WLObjectOutput)var1).writeInt(this.srvrRuntimeState);
      ((WLObjectOutput)var1).writeObjectWL(this.memberAttributes);
      var1.writeLong(this.currentSeqNum);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.srvrRuntimeState = ((WLObjectInput)var1).readInt();
      this.memberAttributes = (MemberAttributes)((WLObjectInput)var1).readObjectWL();
      if (var1.available() > 0) {
         this.currentSeqNum = var1.readLong();
      }

   }

   public RuntimeStateMessage() {
   }

   private void processAttributes(MemberAttributes var1) {
      RemoteMemberInfo var2 = MemberManager.theOne().findOrCreate(var1.identity());

      try {
         if (var2.getAttributes() == null) {
            var2.processAttributes(var1);
         }
      } finally {
         MemberManager.theOne().done(var2);
      }

   }
}
