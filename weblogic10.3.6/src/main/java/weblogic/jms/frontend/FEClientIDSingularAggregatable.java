package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.SingularAggregatable;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.extensions.ConsumerClosedException;

public final class FEClientIDSingularAggregatable extends SingularAggregatable implements Externalizable {
   static final long serialVersionUID = 9144798830174213957L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private String clientID;
   private JMSID feConnectionID;

   public FEClientIDSingularAggregatable(String var1, JMSID var2) {
      this.clientID = var1;
      this.feConnectionID = var2;
   }

   public final void hadConflict(boolean var1) {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("FEClientIDSingularAggregatable.hadConflict clientId = " + this.toString() + " didWin=" + var1);
      }

      if (!var1) {
         try {
            Invocable var2 = (Invocable)InvocableManagerDelegate.delegate.invocableFind(7, this.feConnectionID);
            if (!(var2 instanceof FEConnection)) {
               return;
            }

            FEConnection var3 = (FEConnection)var2;
            var3.close(false, new ConsumerClosedException((MessageConsumer)null, "ClientID conflict, " + this.clientID));
         } catch (JMSException var4) {
         }

      }
   }

   public final String toString() {
      return new String("FEClientIDSingularAggregatable(" + super.toString() + ":" + this.clientID + ")");
   }

   public FEClientIDSingularAggregatable() {
   }

   public final void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(1);
      super.writeExternal(var1);
      var1.writeUTF(this.clientID);
      this.feConnectionID.writeExternal(var1);
   }

   public final void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         this.clientID = var1.readUTF();
         this.feConnectionID = new JMSID();
         this.feConnectionID.readExternal(var1);
      }
   }
}
