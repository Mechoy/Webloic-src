package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;

public class UpgradeDestinationDeleteRecord implements Externalizable {
   private static final byte EXTVERSION = 1;
   private static final int QUEUESLOT = -1;
   static final long serialVersionUID = 68783746469679355L;
   private long creationTime;
   private String destinationName;

   long getCreationTime() {
      return this.creationTime;
   }

   String getDestinationName() {
      return this.destinationName;
   }

   public void writeExternal(ObjectOutput var1) {
      throw new AssertionError("Cannot call writeExternal on an upgrade object");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.destinationName = var1.readUTF();
         this.creationTime = var1.readLong();
      }
   }
}
