package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Response;

public final class FEProducerCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = -6258736813443927954L;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private JMSID producerId;
   private String runtimeMBeanName;

   public FEProducerCreateResponse(JMSID var1, String var2) {
      this.producerId = var1;
      this.runtimeMBeanName = var2;
   }

   public JMSID getProducerId() {
      return this.producerId;
   }

   public String getRuntimeMBeanName() {
      return this.runtimeMBeanName;
   }

   public FEProducerCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      PeerInfo var2 = null;
      if (var1 instanceof PeerInfoable) {
         var2 = ((PeerInfoable)var1).getPeerInfo();
      }

      byte var3;
      if (var2 != null && var2.compareTo(PeerInfo.VERSION_81) < 0) {
         var3 = 1;
      } else {
         var3 = 2;
      }

      var1.writeByte(var3);
      super.writeExternal(var1);
      this.producerId.writeExternal(var1);
      if (var3 >= 2) {
         var1.writeUTF(this.runtimeMBeanName);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1 && var2 != 2) {
         throw JMSUtilities.versionIOException(var2, 1, 2);
      } else {
         super.readExternal(var1);
         this.producerId = new JMSID();
         this.producerId.readExternal(var1);
         if (var2 >= 2) {
            this.runtimeMBeanName = var1.readUTF();
         }

      }
   }
}
