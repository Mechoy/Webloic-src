package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.Message;

public final class HdrMessageImpl extends MessageImpl implements Externalizable {
   private static final byte EXTVERSION = 1;
   static final long serialVersionUID = -5400333814519213733L;
   private int controlOpcode;

   public HdrMessageImpl() {
   }

   public HdrMessageImpl(Message var1) throws javax.jms.JMSException {
      this(var1, (javax.jms.Destination)null, (javax.jms.Destination)null);
   }

   public void nullBody() {
   }

   public HdrMessageImpl(Message var1, javax.jms.Destination var2, javax.jms.Destination var3) throws javax.jms.JMSException {
      super(var1, var2, var3);
   }

   public byte getType() {
      return 2;
   }

   public final int getControlOpcode() {
      return this.controlOpcode;
   }

   public final void setControlOpcode(int var1) {
      assert (var1 & -16711681) == 0;

      this.controlOpcode = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      ObjectOutput var2;
      if (var1 instanceof MessageImpl.JMSObjectOutputWrapper) {
         var2 = ((MessageImpl.JMSObjectOutputWrapper)var1).getInnerObjectOutput();
      } else {
         var2 = var1;
      }

      var2.writeByte(1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      }
   }

   public MessageImpl copy() throws javax.jms.JMSException {
      HdrMessageImpl var1 = new HdrMessageImpl();
      this.copy(var1);
      return var1;
   }

   public long getPayloadSize() {
      return 0L;
   }

   public void decompressMessageBody() {
   }
}
