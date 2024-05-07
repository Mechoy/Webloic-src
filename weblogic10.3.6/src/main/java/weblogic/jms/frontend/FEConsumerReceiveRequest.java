package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.client.ConsumerInternal;
import weblogic.jms.common.JMSConsumerReceiveResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.CompletionListener;
import weblogic.messaging.dispatcher.Response;

public final class FEConsumerReceiveRequest extends Request implements Externalizable, CompletionListener {
   static final long serialVersionUID = 8859525699995436679L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int TIMEOUT_MASK = 3840;
   private static final int TIMEOUT_NEVER = 256;
   private static final int TIMEOUT_NO_WAIT = 512;
   static final int START = 0;
   static final int CONTINUE = 1;
   private long timeout;
   private transient CompletionListener appListener;
   private transient ConsumerInternal consumerInternal;

   public FEConsumerReceiveRequest(JMSID var1, long var2, CompletionListener var4, ConsumerInternal var5) {
      super(var1, 3338);
      this.timeout = var2;
      if (var4 != null) {
         this.consumerInternal = var5;
         this.appListener = var4;
         this.setListener(this);
      }

   }

   long getTimeout() {
      return this.timeout;
   }

   public int remoteSignature() {
      return 19;
   }

   public Response createResponse() {
      return new JMSConsumerReceiveResponse();
   }

   public void onCompletion(Object var1) {
      CompletionListener var2;
      synchronized(this) {
         if (this.appListener == null) {
            return;
         }

         var2 = this.appListener;
         this.appListener = null;
      }

      try {
         this.consumerInternal.getSession().proccessReceiveResponse(this.consumerInternal, var1, var2);
      } catch (Throwable var5) {
         var2.onException(var5);
      }

   }

   public void onException(Throwable var1) {
      CompletionListener var2;
      synchronized(this) {
         if (this.appListener == null) {
            return;
         }

         var2 = this.appListener;
         this.appListener = null;
      }

      var2.onException(var1);
   }

   public FEConsumerReceiveRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.timeout == Long.MAX_VALUE) {
         var2 |= 256;
      } else if (this.timeout == 9223372036854775806L) {
         var2 |= 512;
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      if ((var2 & 3840) == 0) {
         var1.writeLong(this.timeout);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 3840) == 256) {
            this.timeout = Long.MAX_VALUE;
         } else if ((var2 & 3840) == 512) {
            this.timeout = 9223372036854775806L;
         } else {
            this.timeout = var1.readLong();
         }

      }
   }
}
