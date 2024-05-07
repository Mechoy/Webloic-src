package weblogic.jms.safclient;

import java.io.File;
import javax.jms.JMSException;
import javax.naming.Context;
import org.w3c.dom.Document;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.extensions.ClientSAF;
import weblogic.jms.safclient.admin.ConfigurationUtils;
import weblogic.jms.safclient.admin.PersistentStoreBean;
import weblogic.jms.safclient.agent.AgentManager;
import weblogic.jms.safclient.jndi.ContextImpl;
import weblogic.jms.safclient.store.StoreUtils;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.transaction.TransactionHelper;

public final class ClientSAFDelegate {
   private Object lock = new Object();
   private boolean closed = true;
   private AgentManager agentManager;
   private ClientSAF userObj;
   private ContextImpl context;
   private File rootDirectory;

   ClientSAFDelegate(ClientSAF var1) {
      this.userObj = var1;
   }

   void discover(Document var1, File var2, String var3, long var4) throws JMSException {
      TransactionHelper.pushTransactionHelper(ClientSAFManager.getTxHelper());
      boolean var14 = false;

      try {
         var14 = true;
         this.initStore(var1, var2);
         this.context = new ContextImpl(this.userObj, var1, this);
         MessageMigrator.discover(var2, StoreUtils.getStore(var2), this.context, var3, var4);
         var14 = false;
      } finally {
         if (var14) {
            TransactionHelper.popTransactionHelper();
            PersistentStoreXA var9 = StoreUtils.getStore(var2);
            if (var9 != null) {
               try {
                  var9.close();
               } catch (Throwable var15) {
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("Failed to close the store:" + var15.getMessage());
                  }
               }

               StoreUtils.removeStore(var2);
            }

         }
      }

      TransactionHelper.popTransactionHelper();
      PersistentStoreXA var6 = StoreUtils.getStore(var2);
      if (var6 != null) {
         try {
            var6.close();
         } catch (Throwable var17) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("Failed to close the store:" + var17.getMessage());
            }
         }

         StoreUtils.removeStore(var2);
      }

   }

   void open(Document var1, File var2, char[] var3) throws JMSException {
      synchronized(this.lock) {
         if (!this.closed) {
            return;
         }

         this.closed = false;
      }

      this.rootDirectory = var2;
      TransactionHelper.pushTransactionHelper(ClientSAFManager.getTxHelper());
      boolean var4 = false;

      try {
         this.initStore(var1, var2);
         this.context = new ContextImpl(this.userObj, var1, this);
         MessageMigrator.migrateMessagesIfNecessary(var2, StoreUtils.getStore(var2), this.context);
         this.agentManager = new AgentManager(var1, this.context, var3, var2);
         var4 = true;
      } finally {
         TransactionHelper.popTransactionHelper();
         if (!var4) {
            this.close();
         }

      }

   }

   private void initStore(Document var1, File var2) throws JMSException {
      PersistentStoreBean var3 = ConfigurationUtils.getPersistentStore(var1);
      File var4 = new File(var3.getStoreDirectory());
      if (!var4.isAbsolute()) {
         var4 = new File(var2, var3.getStoreDirectory());
      }

      if (var4.exists() && !var4.isDirectory()) {
         throw new JMSException("Store directory " + var4.getAbsolutePath() + " must be a directory");
      } else {
         StoreUtils.initStores(var2, var4, var3.getSynchronousWritePolicy());
      }
   }

   boolean close() {
      synchronized(this.lock) {
         if (this.closed) {
            return false;
         } else {
            this.closed = true;
            if (this.context != null) {
               try {
                  this.context.shutdown(new JMSException("The client SAF system is being closed normally"));
               } catch (Throwable var6) {
                  System.out.println(var6.getMessage());
                  var6.printStackTrace();
               }
            }

            if (this.agentManager != null) {
               try {
                  this.agentManager.shutdown();
               } catch (Throwable var5) {
                  System.out.println(var5.getMessage());
                  var5.printStackTrace();
               }
            }

            PersistentStoreXA var2 = StoreUtils.getStore(this.rootDirectory);
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var7) {
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("Failed to close the store:" + var7.getMessage());
                  }
               }

               StoreUtils.removeStore(this.rootDirectory);
            }

            return true;
         }
      }
   }

   ClientSAF getUserObj() {
      return this.userObj;
   }

   public boolean isOpened() {
      synchronized(this.lock) {
         return !this.closed;
      }
   }

   public Context getContext() {
      return this.context;
   }

   public AgentManager getAgentManager() {
      return this.agentManager;
   }

   public TransactionHelper getTransactionHelper() {
      return ClientSAFManager.getTxHelper();
   }
}
