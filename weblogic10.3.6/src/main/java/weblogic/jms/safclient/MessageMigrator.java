package weblogic.jms.safclient;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.jms.JMSException;
import javax.jms.Message;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.safclient.admin.ConfigurationUtils;
import weblogic.jms.safclient.agent.AgentManager;
import weblogic.jms.safclient.jndi.ContextImpl;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.SendOptions;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.kernel.internal.KernelImpl;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class MessageMigrator {
   private static String SAFCLIENT_KERNEL_PREFIX = "weblogic.messaging.ClientSAFAgent";
   private static String SAFCLIENT_KERNEL_NAME_PREFIX = "ClientSAFAgent";
   private static String DEFAULT_CUTOFF_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
   private static SimpleDateFormat CUTOFF_TIME_FORMAT = null;
   public static final boolean revertBugFix;
   private static Map<File, Boolean> migrationDone;
   private PersistentStoreXA pStore;
   private ContextImpl context;
   private File pagingDirectory;
   private List<Kernel> oldKernels = new ArrayList();
   private Kernel kernel0;
   private Queue queue0;
   private Sequence sequence0;
   private int migrateTotal = 0;

   MessageMigrator(File var1, PersistentStoreXA var2, ContextImpl var3) {
      this.pStore = var2;
      this.context = var3;
      this.pagingDirectory = new File(var1, "paging");
   }

   static void migrateMessagesIfNecessary(File var0, PersistentStoreXA var1, ContextImpl var2) throws JMSException {
      if (!revertBugFix) {
         boolean var3 = Boolean.valueOf(System.getProperty("weblogic.jms.safclient.MigrateExistingMessages", "true"));
         if (var3) {
            Object var4 = migrationDone.get(var0);
            if (var4 == null || !(Boolean)var4) {
               String var5 = System.getProperty("weblogic.jms.safclient.MigrationCutoffTime");
               long var6 = -1L;
               if (var5 != null) {
                  try {
                     var6 = CUTOFF_TIME_FORMAT.parse(var5).getTime();
                  } catch (ParseException var9) {
                     throwJMSException(var9, "The cutoff time property " + var5 + " is not of " + CUTOFF_TIME_FORMAT + " format");
                  }
               }

               MessageMigrator var8 = new MessageMigrator(var0, var1, var2);
               var8.migrateMessages(var6);
               migrationDone.put(var0, new Boolean(true));
            }
         }
      }
   }

   static void discover(File var0, PersistentStoreXA var1, ContextImpl var2, String var3, long var4) throws JMSException {
      File var6;
      if (var3 != null && !var3.equals("")) {
         var6 = new File(var3);
      } else {
         var6 = new File(var0, "SAF_DISCOVERY");
      }

      MessageMigrator var7 = new MessageMigrator(var0, var1, var2);
      var7.discoverLocalSAF(var6, var4);
   }

   private void discoverLocalSAF(File var1, long var2) throws JMSException {
      PrintStream var4 = null;

      try {
         if (var1 == null) {
            var4 = System.out;
         } else {
            if (!var1.exists()) {
               var1.createNewFile();
            }

            var4 = new PrintStream(var1);
         }
      } catch (IOException var15) {
         throwJMSException(var15, "Error in open discovery file " + var1);
      }

      try {
         this.printDiscovery(var4, 0, "Client SAF discovery:");
         this.settlePagingDirectory();
         this.openKernels();
         Map var5 = this.context.getDestinationMap();
         Iterator var6 = var5.keySet().iterator();

         while(var6.hasNext()) {
            Object var7 = var6.next();
            String var8 = (String)var7;
            Iterator var9 = ((Map)var5.get(var8)).keySet().iterator();

            while(var9.hasNext()) {
               Object var10 = var9.next();
               this.discoverDestination(var8, (String)var10, var2, var4, 1);
            }
         }
      } catch (JMSException var16) {
         this.printDiscovery(var4, 0, "\nEncouter error during discovery:" + var16.getMessage());
      } finally {
         this.closeKernels();
         if (!var4.equals(System.out)) {
            var4.close();
         }

      }

      System.out.println("Client SAF discovery has been written to file " + var1);
   }

   private void discoverDestination(String var1, String var2, long var3, PrintStream var5, int var6) {
      this.printDiscovery(var5, var6, "Group:" + var1 + ", destination:" + var2);
      if (this.kernel0 != null) {
         this.discoverKernelQueue(this.kernel0, var1, var2, var3, var5, var6 + 1);
      }

      Iterator var7 = this.oldKernels.iterator();

      while(var7.hasNext()) {
         Kernel var8 = (Kernel)var7.next();
         this.discoverKernelQueue(var8, var1, var2, var3, var5, var6 + 1);
      }

   }

   private void discoverKernelQueue(Kernel var1, String var2, String var3, long var4, PrintStream var6, int var7) {
      String var8 = "(group:" + var2 + ", destination:" + var3 + ")";
      Cursor var9 = null;

      try {
         Queue var10 = this.openKernelQueue(var1, var2, var3);
         if (var10 == null) {
            this.printDiscovery(var6, var7, "Kernel " + var1.getName() + ": No kernel queue was created for " + var8);
            return;
         }

         this.printDiscovery(var6, var7, "Kernel " + var1.getName() + ": queue " + var10.getName() + " was created for " + var8);
         var9 = var10.createCursor(true, (Expression)null, 1073);
         this.printDiscovery(var6, var7 + 1, "Total message in kernel queue " + var9.size());
         MessageElement var11 = null;
         MessageElement var12 = null;
         MessageElement var13 = null;
         int var14 = 0;
         int var15 = 0;

         while((var11 = var9.next()) != null) {
            if (var14 == 0) {
               var12 = var11;
            } else {
               var13 = var11;
            }

            ++var14;
            if (((Message)var11.getMessage()).getJMSTimestamp() < var4) {
               ++var15;
            }
         }

         String var16 = "No cutoff time is specified";
         if (var4 > 0L) {
            var16 = "The number of messages before cutoff time is " + var15;
         }

         this.printDiscovery(var6, var7 + 1, var16);
         if (var12 != null) {
            this.printDiscovery(var6, var7 + 1, "The first message in this kernel queue:");
            this.discoverMessage(var12, var6, var7 + 1);
         }

         if (var13 != null) {
            this.printDiscovery(var6, var7 + 1, "The last message in this kernel queue:");
            this.discoverMessage(var13, var6, var7 + 1);
         }
      } catch (Throwable var20) {
         this.printDiscovery(var6, var7, "Encounter error when discovery " + var8 + " in kernel " + var1.getName());
         this.printDiscovery(var6, var7, "Error: " + var20.getMessage());
      } finally {
         if (var9 != null) {
            var9.close();
         }

      }

   }

   private void discoverMessage(MessageElement var1, PrintStream var2, int var3) {
      if (var1 != null) {
         WLMessage var4 = (WLMessage)var1.getMessage();

         try {
            this.printDiscovery(var2, var3 + 1, "JMSMessageID=" + var4.getJMSMessageID());
         } catch (JMSException var8) {
         }

         try {
            this.printDiscovery(var2, var3 + 1, "JMSCorrelationID=" + var4.getJMSCorrelationID());
         } catch (JMSException var7) {
         }

         try {
            this.printDiscovery(var2, var3 + 1, "JMSTimestamp=" + CUTOFF_TIME_FORMAT.format(new Date(var4.getJMSTimestamp())));
         } catch (JMSException var6) {
         }

         this.printDiscovery(var2, var3 + 1, "SAFSequenceName=" + var1.getSequence().getName());
         this.printDiscovery(var2, var3 + 1, "SAFSeqNumber=" + var1.getSequenceNum());
         this.printDiscovery(var2, var3 + 1, "UnitOfOrder=" + var4.getUnitOfOrder());
      }
   }

   private void printDiscovery(PrintStream var1, int var2, String var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         var1.print("    ");
      }

      var1.println(var3);
   }

   private void migrateMessages(long var1) throws JMSException {
      String var3 = "NONE";

      try {
         this.settlePagingDirectory();
         this.openKernels();
         if (this.oldKernels.size() != 0) {
            if (this.kernel0 == null) {
               this.kernel0 = this.openKernel(SAFCLIENT_KERNEL_NAME_PREFIX + "0");
            }

            var3 = "PARTIAL";
            Map var4 = this.context.getDestinationMap();
            Iterator var5 = var4.keySet().iterator();

            while(var5.hasNext()) {
               Object var6 = var5.next();
               String var7 = (String)var6;
               Iterator var8 = ((Map)var4.get(var7)).keySet().iterator();

               while(var8.hasNext()) {
                  Object var9 = var8.next();
                  this.migrateDestination(var7, (String)var9, var1);
               }
            }

            var3 = "COMPLETE";
            return;
         }
      } finally {
         this.closeKernels();
         if (var3.equals("COMPLETE")) {
            System.out.println("The message migration was successfully done. The total messages migrated was " + this.migrateTotal);
         } else if (var3.equals("PARTIAL")) {
            System.out.println("The message migration was partially done. The total messages migrated was " + this.migrateTotal);
         }

      }

   }

   private void migrateDestination(String var1, String var2, long var3) throws JMSException {
      this.setupKernelQueue0(var1, var2, var3);
      Iterator var5 = this.oldKernels.iterator();

      while(true) {
         Queue var7;
         do {
            if (!var5.hasNext()) {
               return;
            }

            Kernel var6 = (Kernel)var5.next();
            var7 = this.openKernelQueue(var6, var1, var2);
         } while(var7 == null);

         Cursor var8 = null;

         try {
            var8 = var7.createCursor(true, (Expression)null, 1073);
            MessageElement var9 = null;

            label104:
            while(true) {
               while(true) {
                  if ((var9 = var8.next()) == null) {
                     break label104;
                  }

                  if (var3 > 0L && ((Message)var9.getMessage()).getJMSTimestamp() < var3) {
                     var7.delete(var9);
                  } else {
                     this.moveOneMessageToQueue0(var9, var7);
                  }
               }
            }
         } catch (KernelException var13) {
            throwJMSException(var13, "Failed to migrate message for group:" + var1 + ", destination:" + var2);
         } finally {
            if (var8 != null) {
               var8.close();
            }

         }

         this.deleteKernelQueue(var7);
      }
   }

   private void moveOneMessageToQueue0(MessageElement var1, Queue var2) throws JMSException {
      MessageImpl var3 = (MessageImpl)var1.getMessage();
      SendOptions var4 = new SendOptions();
      var4.setPersistent(var3.getJMSDeliveryMode() == 2);
      int var5 = var3.getJMSRedeliveryLimit();
      if (var5 >= 0) {
         var4.setRedeliveryLimit(var5);
      }

      var4.setExpirationTime(var3.getJMSExpiration());
      var4.setDeliveryTime(var3.getJMSDeliveryTime());
      var4.setSequence(this.sequence0);
      ClientTransactionManager var6 = TransactionHelper.getTransactionHelper().getTransactionManager();
      boolean var7 = false;

      try {
         var6.begin();
         var7 = true;
         KernelRequest var8 = this.queue0.send(var3, var4);
         if (var8 != null) {
            var8.getResult();
         }

         var8 = var2.delete(var1);
         if (var8 != null) {
            var8.getResult();
         }

         var6.commit();
         var7 = false;
         ++this.migrateTotal;
      } catch (Throwable var16) {
         throwJMSException(var16, "Failed to move one message to from other kernel queue to kernel0 queue");
      } finally {
         if (var7) {
            try {
               var6.rollback();
            } catch (Throwable var17) {
               if (JMSDebug.JMSCommon.isDebugEnabled()) {
                  JMSDebug.JMSCommon.debug("Failed to rollback the migration transaction:" + var17.getMessage());
               }
            }
         }

      }

   }

   private void settlePagingDirectory() throws JMSException {
      if (!this.pagingDirectory.exists()) {
         if (!this.pagingDirectory.mkdirs()) {
            throw new JMSException("Failed to create paging directory " + this.pagingDirectory.getAbsolutePath());
         }
      } else if (!this.pagingDirectory.isDirectory()) {
         throw new JMSException("The file " + this.pagingDirectory.getAbsolutePath() + " must be a directory, it will be used for the paging store");
      }

   }

   private Queue openKernelQueue(Kernel var1, String var2, String var3) throws JMSException {
      String var4 = AgentManager.constructDestinationName(var2, var3);
      Queue var5 = var1.findQueue(var4);
      if (var5 != null) {
         try {
            var5.resume(16384);
         } catch (KernelException var7) {
            throwJMSException(var7, "Failed to resume the kernel queue " + var4 + " of kernel " + var1.getName());
         }
      }

      return var5;
   }

   private void setupKernelQueue0(String var1, String var2, long var3) throws JMSException {
      this.queue0 = this.openKernelQueue(this.kernel0, var1, var2);
      String var5;
      if (this.queue0 == null) {
         var5 = AgentManager.constructDestinationName(var1, var2);
         HashMap var6 = new HashMap();
         var6.put("Durable", new Boolean(true));
         var6.put("MaximumMessageSize", new Integer(Integer.MAX_VALUE));

         try {
            this.queue0 = this.kernel0.createQueue(var5, var6);
         } catch (KernelException var16) {
            throwJMSException(var16, "Failed to create the kernel queue " + var5 + " of kernel0");
         }

         try {
            this.queue0.resume(16384);
         } catch (KernelException var15) {
            throwJMSException(var15, "Failed to resume the kernel queue " + var5 + " of kernel0");
         }
      }

      var5 = ConfigurationUtils.getSequenceNameFromQueue(this.queue0);

      try {
         this.sequence0 = this.queue0.findOrCreateSequence(var5, 1);
      } catch (KernelException var14) {
         throwJMSException(var14, "Failed to create sequence " + var5 + " for queue " + this.queue0.getName());
      }

      if (var3 > 0L) {
         Cursor var19 = null;

         try {
            var19 = this.queue0.createCursor(true, (Expression)null, 1073);
            MessageElement var7 = null;

            while((var7 = var19.next()) != null) {
               if (((Message)var7.getMessage()).getJMSTimestamp() < var3) {
                  this.queue0.delete(var7);
               }
            }
         } catch (KernelException var17) {
            throwJMSException(var17, "Failed to cleanup queue0 based on the cutoff time");
         } finally {
            if (var19 != null) {
               var19.close();
            }

         }
      }

   }

   private void deleteKernelQueue(Queue var1) {
      try {
         KernelRequest var2 = new KernelRequest();
         var1.delete(var2);
         var2.getResult();
      } catch (KernelException var3) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("Failed to delete kernel queue " + var1.getName() + " with error:" + var3.getMessage());
         }
      }

   }

   private Kernel openKernel(String var1) throws JMSException {
      HashMap var2 = new HashMap();
      var2.put("PagingDirectory", this.pagingDirectory.getAbsolutePath());
      var2.put("Store", this.pStore);
      WorkManager var3 = WorkManagerFactory.getInstance().getSystem();
      WorkManager var4 = WorkManagerFactory.getInstance().getSystem();
      String var5 = AgentManager.getManagerSequence();
      String var6 = "client.SAF." + var1 + var5;
      String var7 = "client.SAF." + var1 + var5 + ".direct";
      var2.put("WorkManager", var3);
      var2.put("LimitedWorkManager", var4);
      var2.put("LimitedTimerManagerName", var6);
      var2.put("DirectTimerManagerName", var7);
      KernelImpl var8 = null;

      try {
         var8 = new KernelImpl(var1, var2);
         var8.open();
         var8.setProperty("MessageBufferSize", new Long(Long.MAX_VALUE));
         var8.setProperty("MaximumMessageSize", new Integer(Integer.MAX_VALUE));
      } catch (KernelException var10) {
         throwJMSException(var10, "Failed to open the Kernel " + var1);
      }

      return var8;
   }

   private void openKernels() throws JMSException {
      TreeMap var1 = new TreeMap();
      Iterator var2 = this.pStore.getConnectionNames();

      while(true) {
         boolean var12 = false;

         try {
            var12 = true;
            if (!var2.hasNext()) {
               var12 = false;
               break;
            }

            String var3 = (String)var2.next();
            if (var3.startsWith(SAFCLIENT_KERNEL_PREFIX) && var3.endsWith(".header")) {
               boolean var4 = false;

               int var16;
               try {
                  var16 = Integer.parseInt(var3.substring(SAFCLIENT_KERNEL_PREFIX.length(), var3.indexOf(".header")));
               } catch (NumberFormatException var13) {
                  continue;
               }

               String var5 = SAFCLIENT_KERNEL_NAME_PREFIX + var16;
               Kernel var6 = this.openKernel(var5);
               if (var16 == 0) {
                  this.kernel0 = var6;
               } else {
                  var1.put(new Integer(var16), var6);
               }
            }
         } finally {
            if (var12) {
               Iterator var8 = var1.values().iterator();

               while(var8.hasNext()) {
                  Kernel var9 = (Kernel)var8.next();
                  this.oldKernels.add(var9);
               }

            }
         }
      }

      Iterator var15 = var1.values().iterator();

      while(var15.hasNext()) {
         Kernel var17 = (Kernel)var15.next();
         this.oldKernels.add(var17);
      }

   }

   private void closeKernels() {
      if (this.oldKernels != null) {
         Iterator var1 = this.oldKernels.iterator();

         while(var1.hasNext()) {
            Kernel var2 = (Kernel)var1.next();

            try {
               var2.close();
            } catch (KernelException var5) {
               if (JMSDebug.JMSCommon.isDebugEnabled()) {
                  JMSDebug.JMSCommon.debug("Failed to close the kernel " + var2.getName() + " with error:" + var5.getMessage());
               }
            }
         }

         this.oldKernels = null;
      }

      if (this.kernel0 != null) {
         try {
            this.kernel0.close();
         } catch (KernelException var4) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("Failed to close the kernel0 with error:" + var4.getMessage());
            }
         }

         this.kernel0 = null;
      }

   }

   private static void throwJMSException(Throwable var0, String var1) throws JMSException {
      JMSException var2 = new JMSException(var1);
      var2.initCause(var0);
      throw var2;
   }

   static {
      String var0 = null;
      if ((var0 = System.getProperty("weblogic.jms.safclient.MigrationCutoffTimeFormat")) == null) {
         var0 = DEFAULT_CUTOFF_TIME_FORMAT;
      }

      CUTOFF_TIME_FORMAT = new SimpleDateFormat(var0);
      revertBugFix = Boolean.valueOf(System.getProperty("weblogic.jms.safclient.revertBug8174629Fix", "true"));
      migrationDone = new HashMap();
   }
}
