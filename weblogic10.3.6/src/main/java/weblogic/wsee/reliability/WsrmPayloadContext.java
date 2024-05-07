package weblogic.wsee.reliability;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.wsee.policy.deployment.ProviderRegistry;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.util.Verbose;

public class WsrmPayloadContext implements Externalizable {
   private static final boolean verbose = Verbose.isVerbose(WsrmPayloadContext.class);
   private NormalizedExpression requestPolicy;
   private NormalizedExpression responsePolicy;
   private String conversationKey = null;
   private boolean waitForConversationId = false;
   private boolean startConversation = false;
   private boolean emptyLastMessage = false;
   private int retryCount = -1;
   private long retryDelay = -1L;

   public WsrmPayloadContext() {
      this.initExternalization();
   }

   public boolean isEmptyLastMessage() {
      return this.emptyLastMessage;
   }

   public void setEmptyLastMessage(boolean var1) {
      this.emptyLastMessage = var1;
   }

   public void setWaitForConversationId(boolean var1) {
      this.waitForConversationId = var1;
   }

   public boolean getWaitForConversationId() {
      return this.waitForConversationId;
   }

   public void setConversationKey(String var1) {
      this.conversationKey = var1;
   }

   public String getConversationKey() {
      return this.conversationKey;
   }

   public void setStartConversation(boolean var1) {
      this.startConversation = var1;
   }

   public boolean getStartConversation() {
      return this.startConversation;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      if (!"9.2".equals(var2)) {
         throw new IOException("Wrong version, expected: 9.2 actual: " + var2);
      } else {
         this.requestPolicy = ExternalizationUtils.readNormalizedExpression(var1);
         this.responsePolicy = ExternalizationUtils.readNormalizedExpression(var1);
         this.waitForConversationId = var1.readBoolean();
         this.startConversation = var1.readBoolean();
         int var3 = var1.readInt();
         if (var3 > 0) {
            this.conversationKey = var1.readUTF();
         } else {
            this.conversationKey = null;
         }

         this.retryCount = var1.readInt();
         this.retryDelay = var1.readLong();
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF("9.2");
      ExternalizationUtils.writeNormalizedExpression(this.requestPolicy, var1);
      ExternalizationUtils.writeNormalizedExpression(this.responsePolicy, var1);
      var1.writeBoolean(this.waitForConversationId);
      var1.writeBoolean(this.startConversation);
      if (this.conversationKey != null) {
         var1.writeInt(1);
         var1.writeUTF(this.conversationKey);
      } else {
         var1.writeInt(0);
      }

      var1.writeInt(this.retryCount);
      var1.writeLong(this.retryDelay);
   }

   public NormalizedExpression getResponsePolicy() {
      return this.responsePolicy;
   }

   public void setResponsePolicy(NormalizedExpression var1) {
      this.responsePolicy = var1;
   }

   public NormalizedExpression getRequestPolicy() {
      return this.requestPolicy;
   }

   public void setRequestPolicy(NormalizedExpression var1) {
      this.requestPolicy = var1;
   }

   private void initExternalization() {
      try {
         ProviderRegistry.getTheRegistry();
      } catch (PolicyException var2) {
         throw new AssertionError(var2);
      }
   }

   public int getRetryCount() {
      return this.retryCount;
   }

   public void setRetryCount(int var1) {
      this.retryCount = var1;
   }

   public long getRetryDelay() {
      return this.retryDelay;
   }

   public void setRetryDelay(long var1) {
      this.retryDelay = var1;
   }
}
