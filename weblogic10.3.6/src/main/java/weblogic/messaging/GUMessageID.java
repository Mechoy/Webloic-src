package weblogic.messaging;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.messaging.common.MessageIDFactory;
import weblogic.messaging.common.MessageIDImpl;
import weblogic.messaging.common.MessagingUtilities;

public final class GUMessageID extends MessageIDImpl {
   static final long serialVersionUID = -5575306479376894391L;
   private static final byte DIABLOVERSION = 1;
   private static MessageIDFactory messageIDFactory = new MessageIDFactory();

   public static GUMessageID create() {
      return new GUMessageID(messageIDFactory);
   }

   private GUMessageID(MessageIDFactory var1) {
      super(var1);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof GUMessageID) ? false : super.equals(var1);
      }
   }

   public GUMessageID() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw MessagingUtilities.versionIOException(var2, 1, 1);
      } else {
         super.readExternal(var1);
      }
   }
}
