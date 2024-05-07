package weblogic.messaging.saf.common;

import java.io.Externalizable;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;

public final class SAFConversationHandleImpl implements SAFConversationHandle {
   private final String conversationName;
   private final String dynamicConversationName;
   private final long conversationTimeout;
   private final long conversationMaxIdleTime;
   private final SAFConversationInfo offer;
   private final String createConvMsgID;
   private final Externalizable conversationContext;

   public SAFConversationHandleImpl(String var1, long var2, long var4, SAFConversationInfo var6, String var7, Externalizable var8) {
      this((String)null, var1, var2, var4, var6, var7, var8);
   }

   public SAFConversationHandleImpl(String var1, String var2, long var3, long var5, SAFConversationInfo var7, String var8, Externalizable var9) {
      this.conversationName = var1;
      this.dynamicConversationName = var2;
      this.conversationTimeout = var3;
      this.conversationMaxIdleTime = var5;
      this.offer = var7;
      this.createConvMsgID = var8;
      this.conversationContext = var9;
   }

   public String getConversationName() {
      return this.conversationName;
   }

   public String getDynamicConversationName() {
      return this.dynamicConversationName;
   }

   public long getConversationTimeout() {
      return this.conversationTimeout;
   }

   public long getConversationMaxIdleTime() {
      return this.conversationMaxIdleTime;
   }

   public SAFConversationInfo getOffer() {
      return this.offer;
   }

   public String getCreateConversationMessageID() {
      return this.createConvMsgID;
   }

   public Externalizable getConversationContext() {
      return this.conversationContext;
   }
}
