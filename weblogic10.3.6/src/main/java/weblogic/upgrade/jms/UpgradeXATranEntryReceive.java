package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSUtilities;

public class UpgradeXATranEntryReceive implements Externalizable {
   private static final byte EXTVERSION = 3;
   static final long serialVersionUID = -8241869299022940995L;
   private long timeStamp;
   private JMSMessageId recoveredMessageId;
   private UpgradeXAXid recoveredBEXAXid;
   private DestinationImpl recoveredDestination;

   long getTimeStamp() {
      return this.timeStamp;
   }

   JMSMessageId getRecoveredMessageId() {
      return this.recoveredMessageId;
   }

   UpgradeXAXid getRecoveredBEXAXid() {
      return this.recoveredBEXAXid;
   }

   DestinationImpl getRecoveredDestination() {
      return this.recoveredDestination;
   }

   public final void writeExternal(ObjectOutput var1) {
      throw new AssertionError("Cannot call writeExternal on an upgrade object");
   }

   public final void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 3) {
         throw JMSUtilities.versionIOException(var2, 3, 3);
      } else {
         this.recoveredMessageId = new JMSMessageId();
         this.recoveredMessageId.readExternal(var1);
         if (var1.readBoolean()) {
            this.recoveredBEXAXid = new UpgradeXAXid();
            this.recoveredBEXAXid.readExternal(var1);
         }

         this.recoveredDestination = new DestinationImpl();
         this.recoveredDestination.readExternal(var1);
         this.timeStamp = var1.readLong();
      }
   }
}
