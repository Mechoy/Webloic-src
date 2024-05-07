package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;

final class StateDumpMessage implements GroupMessage, Externalizable {
   private static final long serialVersionUID = -3278918830125969257L;
   ArrayList offers;
   int senderNum;
   long currentSeqNum;

   StateDumpMessage(ArrayList var1, int var2, long var3) {
      this.offers = var1;
      this.senderNum = var2;
      this.currentSeqNum = var3;
   }

   public void execute(HostID var1) {
      AnnouncementManager.theOne().receiveStateDump(var1, this);
   }

   public String toString() {
      return "StateDump numOffers:" + this.offers.size() + " seqNum " + this.currentSeqNum;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      synchronized(this.offers) {
         ((WLObjectOutput)var1).writeArrayList(this.offers);
      }

      var1.writeInt(this.senderNum);
      var1.writeLong(this.currentSeqNum);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.offers = ((WLObjectInput)var1).readArrayList();
      this.senderNum = var1.readInt();
      this.currentSeqNum = var1.readLong();
   }

   public StateDumpMessage() {
   }
}
