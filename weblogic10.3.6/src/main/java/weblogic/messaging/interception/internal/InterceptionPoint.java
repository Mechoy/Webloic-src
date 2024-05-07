package weblogic.messaging.interception.internal;

import javax.xml.rpc.handler.MessageContext;
import weblogic.messaging.interception.exceptions.InterceptionException;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.exceptions.MessageContextException;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;

class InterceptionPoint {
   private String interceptionPointType = null;
   private String[] interceptionPointName = null;
   private String internalName = null;
   private Association association = null;
   private InterceptionPointTypeWrapper iptw = null;
   private int handleCount = 0;

   InterceptionPoint(String var1, String[] var2, String var3, InterceptionPointTypeWrapper var4) {
      this.interceptionPointType = var1;
      this.interceptionPointName = var2;
      this.internalName = var3;
      this.iptw = var4;
   }

   synchronized void register() {
      ++this.handleCount;
   }

   synchronized void unregister() {
      --this.handleCount;
      if (this.handleCount == 0 & this.association == null) {
         this.iptw.removeIP(this.internalName);
      }

   }

   synchronized int getRegistrationCount() {
      return this.handleCount;
   }

   InterceptionPointHandle createHandle() {
      this.register();
      return new InterceptionPointHandleImpl(this);
   }

   void removeHandle() {
      this.unregister();
   }

   String[] getNameInternal() {
      return this.getName();
   }

   String[] getName() {
      return InterceptionServiceImpl.copyIPName(this.interceptionPointName);
   }

   String getType() {
      return this.interceptionPointType;
   }

   String getInternalName() {
      return this.internalName;
   }

   synchronized void addAssociation(Association var1) {
      this.association = var1;
   }

   synchronized void removeAssociation() {
      this.association = null;
      if (this.handleCount == 0) {
         this.iptw.removeIP(this.internalName);
      }

   }

   synchronized Association getAssociation() {
      return this.association;
   }

   boolean process(MessageContext var1) throws InterceptionServiceException, MessageContextException, InterceptionException {
      return false;
   }

   void processOnly(MessageContext var1) throws InterceptionServiceException, MessageContextException, InterceptionException {
   }

   static String createInternalName(String var0, String[] var1) {
      StringBuffer var2 = new StringBuffer(256);
      var2.append(var0.length());
      var2.append(" " + var0);
      var2.append(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(" " + var1[var3]);
      }

      return var2.toString();
   }
}
