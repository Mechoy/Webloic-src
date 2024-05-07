package weblogic.messaging.interception.internal;

import javax.xml.rpc.handler.MessageContext;
import weblogic.messaging.interception.MIExceptionLogger;
import weblogic.messaging.interception.exceptions.InterceptionException;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.exceptions.MessageContextException;
import weblogic.messaging.interception.interfaces.AssociationInfo;
import weblogic.messaging.interception.interfaces.CarrierCallBack;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;

public class InterceptionPointHandleImpl implements InterceptionPointHandle {
   private InterceptionPoint interceptionPoint = null;
   private boolean unregistered = false;

   InterceptionPointHandleImpl(InterceptionPoint var1) {
      this.interceptionPoint = var1;
   }

   public String[] getName() throws InterceptionServiceException {
      InterceptionPoint var1 = null;
      synchronized(this) {
         this.checkUnregistered();
         var1 = this.interceptionPoint;
      }

      return var1.getName();
   }

   public String getType() throws InterceptionServiceException {
      InterceptionPoint var1 = null;
      synchronized(this) {
         this.checkUnregistered();
         var1 = this.interceptionPoint;
      }

      return var1.getType();
   }

   synchronized void unregister() throws InterceptionServiceException {
      this.checkUnregistered();
      this.unregistered = true;
      this.interceptionPoint.unregister();
      this.interceptionPoint = null;
   }

   public synchronized boolean hasAssociation() throws InterceptionServiceException {
      this.checkUnregistered();
      return this.interceptionPoint.getAssociation() != null;
   }

   public AssociationInfo getAssociationInfo() throws InterceptionServiceException {
      InterceptionPoint var1 = null;
      synchronized(this) {
         this.checkUnregistered();
         var1 = this.interceptionPoint;
      }

      Association var2 = var1.getAssociation();
      return var2 == null ? null : var1.getAssociation().getInfoInternal();
   }

   public boolean process(MessageContext var1) throws InterceptionServiceException, MessageContextException, InterceptionException {
      InterceptionPoint var2 = null;
      synchronized(this) {
         this.checkUnregistered();
         var2 = this.interceptionPoint;
      }

      Association var3 = var2.getAssociation();
      return var3 == null ? true : var3.process(var1);
   }

   public void processAsync(MessageContext var1, CarrierCallBack var2) throws InterceptionServiceException, MessageContextException, InterceptionException {
      InterceptionPoint var3 = null;
      synchronized(this) {
         this.checkUnregistered();
         var3 = this.interceptionPoint;
      }

      Association var4 = var3.getAssociation();
      if (var4 == null) {
         var2.onCallBack(true);
      } else {
         var4.process(var1, var2);
      }
   }

   public void processOnly(MessageContext var1) throws InterceptionServiceException, MessageContextException, InterceptionException {
      InterceptionPoint var2 = null;
      synchronized(this) {
         this.checkUnregistered();
         var2 = this.interceptionPoint;
      }

      Association var3 = var2.getAssociation();
      if (var3 != null) {
         var3.processOnly(var1);
      }
   }

   public void processOnlyAsync(MessageContext var1, CarrierCallBack var2) throws InterceptionServiceException, MessageContextException, InterceptionException {
      InterceptionPoint var3 = null;
      synchronized(this) {
         this.checkUnregistered();
         var3 = this.interceptionPoint;
      }

      Association var4 = var3.getAssociation();
      if (var4 == null) {
         var2.onCallBack(true);
      } else {
         var4.processOnly(var1, var2);
      }
   }

   private void checkUnregistered() throws InterceptionServiceException {
      if (this.unregistered) {
         throw new InterceptionServiceException(MIExceptionLogger.logUnregisterInterceptionPointAlreayRemoveErrorLoggable("InterceptionPoint has been unregistered").getMessage());
      }
   }
}
