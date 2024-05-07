package weblogic.jms.backend;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.jms.JMSService;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSMessageOpenDataConverter;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.common.JMSSQLFilter;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.DestinationInfo;
import weblogic.jms.extensions.JMSForwardHelper;
import weblogic.jms.extensions.JMSMessageInfo;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.management.ManagementException;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.QuotaException;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.runtime.CursorRuntimeImpl;
import weblogic.messaging.runtime.OpenDataConverter;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;

final class BEMessageManagementImpl {
   private final String name;
   private long messagesDeletedCurrentCount;
   private long messagesMovedCurrentCount;
   private final Queue queue;
   private final BEDestinationImpl destination;
   private final JMSSQLFilter filter;
   private final CursorRuntimeImpl cursorRuntime;
   private final OpenDataConverter messageHeaderConverter;
   private final OpenDataConverter messageBodyConverter;
   private final TransactionManager tm;

   BEMessageManagementImpl(String var1, Queue var2, BEDestinationImpl var3, CursorRuntimeImpl var4) {
      this.name = var1;
      this.queue = var2;
      this.destination = var3;
      this.filter = new JMSSQLFilter(var2.getKernel());
      this.cursorRuntime = var4;
      this.messageHeaderConverter = new JMSMessageOpenDataConverter(false);
      this.messageBodyConverter = new JMSMessageOpenDataConverter(true);
      this.tm = (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
   }

   Long getMessagesMovedCurrentCount() {
      return new Long(this.messagesMovedCurrentCount);
   }

   Long getMessagesDeletedCurrentCount() {
      return new Long(this.messagesDeletedCurrentCount);
   }

   String getMessages(String var1, Integer var2) throws ManagementException {
      return this.getMessages(var1, var2, new Integer(Integer.MAX_VALUE));
   }

   String getMessages(String var1, Integer var2, Integer var3) throws ManagementException {
      try {
         this.destination.getJMSDestinationSecurity().checkBrowsePermission();
      } catch (JMSSecurityException var7) {
         this.throwManagementException("Authorization failure.", var7);
      }

      Cursor var4 = null;

      try {
         var4 = this.queue.createCursor(true, this.filter.createExpression(new JMSSQLExpression(var1)), var3);
      } catch (KernelException var6) {
         this.throwManagementException("Error creating message cursor for " + this.name, var6);
      }

      JMSMessageCursorDelegate var5 = new JMSMessageCursorDelegate(this.cursorRuntime, this.messageHeaderConverter, var4, this.messageBodyConverter, this.destination, var2);
      this.cursorRuntime.addCursorDelegate(var5);
      return var5.getHandle();
   }

   Integer moveMessages(String var1, CompositeData var2) throws ManagementException {
      return this.moveMessages(var1, var2, new Integer(0));
   }

   Integer moveMessages(String var1, CompositeData var2, Integer var3) throws ManagementException {
      if (var2 == null) {
         this.throwManagementException("moveMessages operation failed. The target destination is null.");
      }

      if (var3 == null) {
         this.throwManagementException("moveMessages operation failed.  The timeout value is null.");
      }

      DestinationImpl var4 = null;

      try {
         DestinationInfo var5 = new DestinationInfo(var2);
         var4 = (DestinationImpl)var5.getDestination();
      } catch (OpenDataException var62) {
         this.throwManagementException("moveMessages operation failed.", var62);
      }

      try {
         this.destination.getJMSDestinationSecurity().checkBrowsePermission();
      } catch (JMSSecurityException var61) {
         this.throwManagementException("Authorization failure.", var61);
      }

      try {
         this.destination.getJMSDestinationSecurity().checkReceivePermission();
      } catch (JMSSecurityException var60) {
         this.throwManagementException("Authorization failure.", var60);
      }

      Transaction var66 = this.suspendTransaction();
      int var6 = 0;
      Cursor var7 = null;
      ConnectionFactory var8 = null;
      Connection var9 = null;
      Session var10 = null;
      WLMessageProducer var11 = null;

      Integer var67;
      try {
         var8 = JMSServerUtilities.getXAConnectionFactory();

         try {
            var9 = var8.createConnection();
            var10 = var9.createSession(false, 2);
            var11 = (WLMessageProducer)var10.createProducer(var4);
         } catch (JMSException var59) {
            this.moveError(this.name, var4.getName(), var59);
         }

         try {
            this.tm.begin("JMS Move Messages", var3);
         } catch (NotSupportedException var57) {
            this.moveError(this.name, var4.getName(), var57);
         } catch (SystemException var58) {
            this.moveError(this.name, var4.getName(), var58);
         }

         MessageElement var12 = null;

         try {
            var7 = this.queue.createCursor(true, this.filter.createExpression(new JMSSQLExpression(var1)), 1073);
            var6 = var7.size();

            while((var12 = var7.next()) != null) {
               MessageImpl var13 = (MessageImpl)var12.getMessage();
               var13.setForward(true);
               JMSForwardHelper.ForwardFromMessage(var11, var13, true);
               KernelRequest var14 = this.queue.delete(var12);
               if (var14 != null) {
                  var14.getResult();
               }
            }
         } catch (KernelException var63) {
            this.moveError(this.name, var4.getName(), var63);
         } catch (JMSException var64) {
            this.moveError(this.name, var4.getName(), var64);
         }

         try {
            this.tm.commit();
         } catch (SecurityException var51) {
            this.moveError(this.name, var4.getName(), var51);
         } catch (IllegalStateException var52) {
            this.moveError(this.name, var4.getName(), var52);
         } catch (RollbackException var53) {
            this.moveError(this.name, var4.getName(), var53);
         } catch (HeuristicMixedException var54) {
            this.moveError(this.name, var4.getName(), var54);
         } catch (HeuristicRollbackException var55) {
            this.moveError(this.name, var4.getName(), var55);
         } catch (SystemException var56) {
            this.moveError(this.name, var4.getName(), var56);
         }

         this.incrementMessagesMovedCurrentCount(var6);
         var67 = new Integer(var6);
      } finally {
         try {
            if (this.tm.getTransaction() != null) {
               this.tm.rollback();
            }
         } catch (IllegalStateException var48) {
            this.debug("Error rolling back move messages transaction", var48);
         } catch (SecurityException var49) {
            this.debug("Error rolling back move messages transaction", var49);
         } catch (SystemException var50) {
            this.debug("Error rolling back move messages transaction", var50);
         }

         if (var11 != null) {
            try {
               var11.close();
            } catch (JMSException var47) {
               this.debug("Unable to close move messages producer", var47);
            }
         }

         if (var10 != null) {
            try {
               var10.close();
            } catch (JMSException var46) {
               this.debug("Unable to close move messages session", var46);
            }
         }

         if (var9 != null) {
            try {
               var9.close();
            } catch (JMSException var45) {
               this.debug("Unable to close move messages connection", var45);
            }
         }

         if (var7 != null) {
            var7.close();
         }

         if (var66 != null) {
            this.resumeTransaction(var66);
         }

      }

      return var67;
   }

   CompositeData getMessage(String var1) throws ManagementException {
      try {
         this.destination.getJMSDestinationSecurity().checkBrowsePermission();
      } catch (JMSSecurityException var11) {
         this.throwManagementException("Authorization failure.", var11);
      }

      CompositeData var2 = null;
      Cursor var3 = null;

      MessageElement var4;
      try {
         var3 = this.queue.createCursor(true, this.filter.createExpression(new JMSSQLExpression("JMSMessageID = '" + var1 + "'")), 1);
         if (var3.size() != 0) {
            if (var3.size() > 1) {
               this.throwManagementException("Multiple messages exist for messageID " + var1);
            }

            var4 = var3.next();
            var2 = this.messageBodyConverter.createCompositeData(var4);
            return var2;
         }

         var4 = null;
      } catch (OpenDataException var12) {
         this.throwManagementException("Failed to convert message with message ID " + var1 + " to open data representation.", var12);
         return var2;
      } catch (KernelException var13) {
         this.throwManagementException("Failed to get message with message ID " + var1, var13);
         return var2;
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

      return var4;
   }

   Integer deleteMessages(String var1) throws ManagementException {
      try {
         this.destination.getJMSDestinationSecurity().checkReceivePermission();
      } catch (JMSSecurityException var12) {
         this.throwManagementException("Authorization failure.", var12);
      }

      Transaction var2 = this.suspendTransaction();
      Cursor var3 = null;
      int var4 = 0;

      try {
         var3 = this.queue.createCursor(true, this.filter.createExpression(new JMSSQLExpression(var1)), 1073);

         for(MessageElement var5 = null; (var5 = var3.next()) != null; ++var4) {
            KernelRequest var6 = this.queue.delete(var5);
            if (var6 != null) {
               var6.getResult();
            }
         }

         this.incrementMessagesDeletedCurrentCount(var4);
      } catch (KernelException var13) {
         this.throwManagementException("Error while deleting messages", var13);
      } finally {
         if (var3 != null) {
            var3.close();
         }

         if (var2 != null) {
            this.resumeTransaction(var2);
         }

      }

      return new Integer(var4);
   }

   Void importMessages(CompositeData[] var1, Boolean var2) throws ManagementException {
      if (var2) {
         String var21 = "importMessages operation not supported with replaceOnly=true";
         this.debug(var21);
         throw new UnsupportedOperationException(var21);
      } else if (var1 != null && var1.length != 0) {
         try {
            this.destination.getJMSDestinationSecurity().checkSendPermission();
         } catch (JMSSecurityException var19) {
            this.throwManagementException("Authorization failure.", var19);
         }

         Transaction var3 = this.suspendTransaction();

         try {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               if (var1[var4] != null) {
                  MessageImpl var5 = null;

                  try {
                     JMSMessageInfo var6 = new JMSMessageInfo(var1[var4]);
                     var5 = (MessageImpl)var6.getMessage();
                  } catch (OpenDataException var18) {
                     this.throwManagementException("Unable to convert Open Data type to Message", var18);
                  }

                  if (var5.getDeliveryCount() < 0) {
                     throw new ManagementException("Import of message " + var5.getJMSMessageID() + " to destination " + this.name + " failed due to an invalid delivery count of " + var5.getDeliveryCount());
                  }

                  if (var5.getAdjustedDeliveryMode() == 2) {
                     if (this.destination.isTemporary()) {
                        this.downgradeMessage(var5);
                     } else if (!this.destination.getBackEnd().isStoreEnabled()) {
                        if (this.destination.getBackEnd().isAllowsPersistentDowngrade()) {
                           this.downgradeMessage(var5);
                        } else {
                           this.throwManagementException("Unable to downgrade message " + var5.getJMSMessageID() + " while importing to destination " + this.name + " because persistence downgrade is not supported.");
                        }
                     }
                  }

                  if (!var2) {
                     var5.setId(JMSService.getJMSService().getNextMessageId());
                  }

                  try {
                     KernelRequest var22 = this.queue.send(var5, this.destination.createSendOptions(0L, (Sequence)null, var5));
                     if (var22 != null) {
                        var22.getResult();
                     }
                  } catch (QuotaException var15) {
                     this.throwManagementException("Quota exceeded on target", var15);
                  } catch (weblogic.messaging.kernel.IllegalStateException var16) {
                     this.throwManagementException("Destination " + this.name + " state does not allow message production", var16);
                  } catch (KernelException var17) {
                     this.throwManagementException("Internal error during import operation", var17);
                  }
               }
            }
         } finally {
            if (var3 != null) {
               this.resumeTransaction(var3);
            }

         }

         return null;
      } else {
         return null;
      }
   }

   private synchronized void incrementMessagesDeletedCurrentCount(int var1) {
      this.messagesDeletedCurrentCount += (long)var1;
   }

   private synchronized void incrementMessagesMovedCurrentCount(int var1) {
      this.messagesMovedCurrentCount += (long)var1;
   }

   private Transaction suspendTransaction() throws ManagementException {
      try {
         return this.tm.suspend();
      } catch (SystemException var2) {
         throw new ManagementException("Unable to suspend existing transaction.", var2);
      }
   }

   private void resumeTransaction(Transaction var1) {
      try {
         this.tm.resume(var1);
      } catch (SystemException var3) {
         this.debug("Error restoring transaction context.", var3);
      } catch (InvalidTransactionException var4) {
         this.debug("Error restoring transaction context.", var4);
      } catch (IllegalStateException var5) {
         this.debug("Error restoring transaction context.", var5);
      }

   }

   private void downgradeMessage(MessageImpl var1) throws ManagementException {
      var1.setAdjustedDeliveryMode(1);

      try {
         var1.setJMSDeliveryMode(1);
      } catch (JMSException var3) {
         this.throwManagementException("Unable to downgrade message.", var3);
      }

   }

   private void moveError(String var1, String var2, Throwable var3) throws ManagementException {
      String var4 = "Error occurred while processing the requested move operation from source " + this.name + " to destination " + var2;
      this.throwManagementException(var4, var3);
   }

   private void throwManagementException(String var1) throws ManagementException {
      this.debug(var1);
      throw new ManagementException(var1);
   }

   private void throwManagementException(String var1, Throwable var2) throws ManagementException {
      this.debug(var1, var2);
      throw new ManagementException(var1, var2);
   }

   private void debug(String var1) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug(var1);
      }

   }

   private void debug(String var1, Throwable var2) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug(var1, var2);
      }

   }
}
