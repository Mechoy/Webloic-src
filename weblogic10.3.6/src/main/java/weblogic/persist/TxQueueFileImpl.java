package weblogic.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionRolledbackException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.t3.srvr.T3Srvr;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.PlatformConstants;
import weblogic.utils.UnsyncHashtable;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class TxQueueFileImpl implements TxQueueFileRemote, XAResource, PlatformConstants {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean verbose = false;
   private String name;
   private String commitFilename;
   private String prepareFilename;
   private Vector queue;
   private Object headMutex = new Object() {
   };
   private Object tailMutex = new Object() {
   };
   private Object headLocker;
   private Object tailLocker;
   private UnsyncHashtable enrolled = new UnsyncHashtable();
   private static Object exists = new Object() {
   };
   private Vector inserts = new Vector();
   private int numRemoved = 0;
   private int numWrites = 0;
   private int fullWriteInterval = 100;
   private int minMillisBetweenWrites = 20;
   private boolean isShutdown;
   private Object preparationMutex = new Object() {
   };
   private Vector preparationInserts;
   private int preparationNumRemoved;
   private Object prepareWriteMutex = new Object() {
   };
   private Vector preparationQueue;
   private File prepareFile;
   private IOException preparationIOE;
   private PrepareThread prepareThread;
   private Object commitMutex = new Object() {
   };
   private Object commitCompleteMutex = new Object() {
   };
   private Object commitWriteMutex = new Object() {
   };
   private Vector commitInserts;
   private int commitNumRemoved;
   private Vector commitQueue;
   private File commitFile;
   private IOException commitIOE;
   private CommitThread commitThread;

   public static void main(String[] var0) {
      try {
         String var1 = var0.length >= 1 ? var0[0] : "DefaultStore";
         String var2 = var0.length >= 2 ? var0[1] : ManagementService.getRuntimeAccess(kernelId).getServer().getName();
         String var3 = var0.length >= 3 ? var0[2] : ManagementService.getRuntimeAccess(kernelId).getServer().getName();
         TxQueueFileStub var4 = new TxQueueFileStub(var1, var2, var3);

         try {
            Hashtable var5 = new Hashtable();
            var5.put("weblogic.jndi.createIntermediateContexts", "true");
            InitialContext var6 = new InitialContext(var5);
            var6.rebind(var1, var4);
         } catch (NamingException var7) {
            T3Srvr.getT3Srvr().getLog().error("There was a communication problem -- this Impl must be in the server", var7);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   protected TxQueueFileImpl(String var1, String var2, String var3) throws IOException {
      this.name = var1;
      this.commitFilename = var2 + File.separator + var1 + ".dat";
      this.prepareFilename = var3 + File.separator + var1 + "Tmp.dat";
      this.loadQueue(new File(var2), new File(var3));
      this.commitThread = new CommitThread(var1 + "-CommitThread");
      this.commitThread.start();
      this.prepareThread = new PrepareThread(var1 + "-PrepareThread");
      this.prepareThread.start();
   }

   public void shutdown() {
      this.isShutdown = true;
      synchronized(this.preparationMutex) {
         this.preparationMutex.notifyAll();
      }

      synchronized(this.commitMutex) {
         this.commitMutex.notifyAll();
      }
   }

   public String getName() {
      return this.name;
   }

   public void put(Object var1) throws TransactionRolledbackException {
      Transaction var2 = null;
      var2 = TxHelper.getTransaction();
      if (var2 == null) {
         try {
            TransactionManager var3 = TxHelper.getTransactionManager();
            var3.begin();
            javax.transaction.Transaction var8 = var3.getTransaction();
            var8.enlistResource(this);
            this.put(var8, var1);
            var8.commit();
         } catch (Exception var5) {
            var5.printStackTrace();
            throw new TransactionRolledbackException("Could not complete transaction . . . Rolled back: " + var5.getMessage());
         }
      } else {
         try {
            synchronized(this) {
               if (this.enrolled.put(var2, exists) == null) {
                  var2.enlistResource(this);
               }
            }
         } catch (Exception var7) {
            throw new TransactionRolledbackException("Could not enroll resource: " + var7.getMessage());
         }

         this.put(var2, var1);
      }

   }

   public Object get() throws TransactionRolledbackException {
      Transaction var1 = null;
      var1 = TxHelper.getTransaction();
      Object var2;
      if (var1 == null) {
         try {
            TransactionManager var3 = TxHelper.getTransactionManager();
            var3.begin();
            javax.transaction.Transaction var8 = var3.getTransaction();
            var8.enlistResource(this);
            var2 = this.get(var8);
            var8.commit();
         } catch (Exception var5) {
            var5.printStackTrace();
            throw new TransactionRolledbackException("Could not complete transaction . . . Rolled back: " + var5.getMessage());
         }
      } else {
         try {
            synchronized(this) {
               if (this.enrolled.put(var1, exists) == null) {
                  var1.enlistResource(this);
               }
            }
         } catch (Exception var7) {
            throw new TransactionRolledbackException("Could not enroll resource: " + var7.getMessage());
         }

         var2 = this.get(var1);
      }

      return var2;
   }

   public Object getW() throws DeadlockException, TransactionRolledbackException {
      Transaction var1 = null;
      var1 = TxHelper.getTransaction();
      int var2 = this.queue.size();
      if (var1 != null && var1.equals(this.tailLocker)) {
         var2 += this.inserts.size();
      }

      if (var1 != null && var1.equals(this.headLocker)) {
         var2 -= this.numRemoved;
      }

      if (var2 == 0 && var1 != null && var1.equals(this.tailLocker)) {
         throw new DeadlockException("Attempting to do a get in transaction that will never succeed\nInserts: " + this.inserts.size() + " Original Size: " + this.queue.size() + " Removes: " + this.numRemoved + "\ntailLocker: " + this.tailLocker + " Tx: " + var1);
      } else {
         synchronized(this.commitCompleteMutex) {
            Object var3;
            while((var3 = this.get()) == null) {
               try {
                  this.commitCompleteMutex.wait();
               } catch (InterruptedException var7) {
               }
            }

            return var3;
         }
      }
   }

   public Object getW(long var1) throws QueueTimeoutException, TransactionRolledbackException {
      Transaction var3 = TxHelper.getTransaction();

      Object var4;
      long var7;
      for(long var5 = System.currentTimeMillis(); (var4 = this.get()) == null && var1 > 0L; var5 = var7) {
         try {
            Thread.sleep(50L);
         } catch (InterruptedException var9) {
         }

         var7 = System.currentTimeMillis();
         var1 -= var7 - var5;
      }

      if (var4 == null) {
         throw new QueueTimeoutException("GetW() timed out");
      } else {
         return var4;
      }
   }

   private void put(javax.transaction.Transaction var1, Object var2) {
      synchronized(this.tailMutex) {
         while(this.tailLocker != null && !var1.equals(this.tailLocker)) {
            try {
               this.tailMutex.wait();
            } catch (InterruptedException var6) {
            }
         }

         if (this.tailLocker == null) {
            this.inserts = new Vector();
         }

         this.tailLocker = var1;
      }

      this.inserts.addElement(var2);
   }

   private Object get(javax.transaction.Transaction var1) {
      synchronized(this.headMutex) {
         while(this.headLocker != null && !var1.equals(this.headLocker)) {
            try {
               this.headMutex.wait();
            } catch (InterruptedException var5) {
            }
         }

         if (this.headLocker == null) {
            this.numRemoved = 0;
         }

         this.headLocker = var1;
      }

      int var3 = this.numRemoved - this.queue.size();
      Object var2;
      if (var3 >= 0) {
         if (!var1.equals(this.tailLocker) || this.inserts.size() <= var3) {
            return null;
         }

         var2 = this.inserts.elementAt(var3);
      } else {
         var2 = this.queue.elementAt(this.numRemoved);
      }

      ++this.numRemoved;
      return var2;
   }

   private void decipherFailure(String var1, String var2, IOException var3) throws XAException {
      String var4 = var2.substring(0, var2.lastIndexOf(FILE_SEP));
      File var5 = new File(var4);
      File var6 = new File(var2);
      String var7 = "Could not " + var1 + ": ";
      String var8 = var3 == null ? "" : EOL + "Caught " + var3;
      if (!var5.exists()) {
         throw new XAException(var7 + "Directory '" + var5 + "' does not exist." + var8);
      } else if (!var5.isDirectory()) {
         throw new XAException(var7 + "File '" + var5 + "' exists but is not a directory." + var8);
      } else if (!var6.canWrite()) {
         throw new XAException(var7 + "Can't write to file '" + var6 + "'." + var8);
      } else if (var3 != null) {
         throw new XAException(var7 + var8);
      } else {
         throw new XAException(var7 + "Possible problems: " + EOL + "  Disk full" + EOL + "  File owned by different process (NT)" + EOL + "  Hardware error");
      }
   }

   public void end(Xid var1, int var2) throws XAException {
   }

   public void forget(Xid var1) throws XAException {
   }

   public int getTransactionTimeout() throws XAException {
      return 0;
   }

   public boolean isSameRM(XAResource var1) throws XAException {
      return this == var1;
   }

   public Xid[] recover(int var1) throws XAException {
      return null;
   }

   public boolean setTransactionTimeout(int var1) throws XAException {
      return true;
   }

   public void start(Xid var1, int var2) throws XAException {
   }

   public int prepare(Xid var1) throws XAException {
      Transaction var2 = TxHelper.getTransaction();
      if (var2 == null) {
         throw new XAException("Transaction unexpectedly null");
      } else {
         synchronized(this.preparationMutex) {
            synchronized(this.prepareWriteMutex) {
               if (this.preparationQueue == null) {
                  this.preparationQueue = (Vector)this.queue.clone();
               }
            }

            if (var2.equals(this.headLocker)) {
               this.preparationNumRemoved = this.numRemoved;
            } else {
               this.preparationNumRemoved = 0;
            }

            if (var2.equals(this.tailLocker)) {
               this.preparationInserts = this.inserts;
            } else {
               this.preparationInserts = null;
            }

            this.fillInQueue(this.preparationQueue, this.preparationInserts, this.preparationNumRemoved);
         }

         this.prepareThread.write();
         if (this.preparationIOE != null) {
            throw new XAException(this.preparationIOE.getMessage());
         } else {
            return 0;
         }
      }
   }

   public void rollback(Xid var1) throws XAException {
      this.commitThread.write();
      this.releaseLocks();
   }

   public void commit(Xid var1, boolean var2) throws XAException {
      if (var2) {
         this.commitOnePhase(var1);
      } else {
         this.commit(var1);
      }

   }

   private void commitOnePhase(Xid var1) throws XAException {
      int var2 = this.prepare(var1);
      if (var2 == 0) {
         this.commit(var1);
      }

   }

   private void commit(Xid var1) throws XAException {
      Transaction var2 = null;
      var2 = TxHelper.getTransaction();
      if (var2 == null) {
         throw new XAException("No transaction");
      } else {
         Vector var3;
         int var4;
         synchronized(this.commitMutex) {
            synchronized(this.commitWriteMutex) {
               if (this.commitQueue == null) {
                  this.commitQueue = (Vector)this.queue.clone();
               }
            }

            if (var2.equals(this.headLocker)) {
               this.commitNumRemoved = this.numRemoved;
            } else {
               this.commitNumRemoved = 0;
            }

            if (var2.equals(this.tailLocker)) {
               this.commitInserts = this.inserts;
            } else {
               this.commitInserts = null;
            }

            this.fillInQueue(this.commitQueue, var3 = this.commitInserts, var4 = this.commitNumRemoved);
         }

         this.commitThread.write();
         if (this.commitIOE != null) {
            this.decipherFailure("commit", this.commitFilename, this.commitIOE);
         }

         this.fillInQueue(this.queue, var3, var4);
         this.releaseLocks();
         synchronized(this.commitCompleteMutex) {
            this.commitCompleteMutex.notifyAll();
         }
      }
   }

   protected void write(Vector var1, Vector var2, int var3, File var4) throws IOException {
      int var5 = this.numWrites++ >> 2;
      boolean var6 = var5 % this.fullWriteInterval != 0;
      UnsyncByteArrayOutputStream var7 = new UnsyncByteArrayOutputStream();
      ObjectOutputStream var8 = new ObjectOutputStream(var7);
      if (var6) {
         var8.writeBoolean(true);
         var8.writeObject(var2);
         var8.writeInt(var3);
      } else {
         var8.writeObject(var1);
      }

      var8.flush();
      var8.close();
      var7.close();
      FileOutputStream var9 = new FileOutputStream(var4.getAbsolutePath(), var6);
      var9.write(var7.toByteArray());
      var9.flush();
      var9.getFD().sync();
      var9.close();
   }

   private Vector readQueue(File var1) throws IOException, ClassNotFoundException {
      FileInputStream var2 = null;
      ObjectInputStream var3 = null;
      Vector var4 = null;

      try {
         var2 = new FileInputStream(var1);
         var3 = new ObjectInputStream(var2);
         var4 = (Vector)var3.readObject();

         while(true) {
            try {
               var3 = new ObjectInputStream(var2);
               var3.readBoolean();
            } catch (IOException var12) {
               return var4;
            }

            try {
               Vector var5 = (Vector)var3.readObject();
               int var6 = var3.readInt();
               this.fillInQueue(var4, var5, var6);
            } catch (IOException var11) {
               throw var11;
            }
         }
      } finally {
         if (var3 != null) {
            var3.close();
         }

         if (var2 != null) {
            var2.close();
         }

      }
   }

   private void loadQueue(File var1, File var2) throws IOException {
      this.queue = new Vector();
      this.ensureDirectories(var1, var2);
      this.commitFile = new File(this.commitFilename);
      this.prepareFile = new File(this.prepareFilename);
      if (this.commitFile.exists()) {
         if (this.prepareFile.exists() && this.prepareFile.lastModified() > this.commitFile.lastModified()) {
            try {
               this.queue = this.readQueue(this.prepareFile);
               return;
            } catch (Exception var7) {
            }
         }

         try {
            this.queue = this.readQueue(this.commitFile);
            return;
         } catch (Exception var6) {
            try {
               if (this.prepareFile.exists() && this.prepareFile.lastModified() < this.commitFile.lastModified()) {
                  this.queue = this.readQueue(this.prepareFile);
                  return;
               }
            } catch (Exception var5) {
               return;
            }
         }
      } else {
         try {
            if (this.prepareFile.exists()) {
               this.queue = this.readQueue(this.prepareFile);
               return;
            }
         } catch (Exception var4) {
            return;
         }
      }

   }

   private void ensureDirectories(File var1, File var2) throws IOException {
      if (!var1.exists() && !var1.mkdirs()) {
         throw new IOException("Couldn't create " + var1);
      } else if (!var2.exists() && !var2.mkdirs()) {
         throw new IOException("Couldn't create " + var2);
      }
   }

   private void fillInQueue(Vector var1, Vector var2, int var3) {
      synchronized(this) {
         if (var2 != null) {
            int var5 = var2.size();

            for(int var6 = 0; var6 < var5; ++var6) {
               var1.addElement(var2.elementAt(var6));
            }
         }

         while(var3 > 0) {
            var1.removeElementAt(0);
            --var3;
         }

      }
   }

   private synchronized void releaseLocks() {
      Transaction var1 = null;
      var1 = TxHelper.getTransaction();
      if (var1 == null) {
         throw new NestedRuntimeException("Transaction is unexpectedly null");
      } else {
         this.enrolled.remove(var1);
         synchronized(this.tailMutex) {
            if (var1.equals(this.tailLocker)) {
               this.tailLocker = null;
               this.inserts = null;
            }

            this.tailMutex.notifyAll();
         }

         synchronized(this.headMutex) {
            if (var1.equals(this.headLocker)) {
               this.headLocker = null;
               this.numRemoved = 0;
            }

            this.headMutex.notifyAll();
         }
      }
   }

   private class CommitThread extends Thread {
      private int writeCalled = 0;
      private int writeDone = 0;

      public CommitThread(String var2) {
         super(var2);
      }

      public void run() {
         while(true) {
            synchronized(TxQueueFileImpl.this.commitMutex) {
               long var2 = (long)TxQueueFileImpl.this.minMillisBetweenWrites;

               while(true) {
                  if (TxQueueFileImpl.this.commitQueue == null) {
                     var2 = (long)TxQueueFileImpl.this.minMillisBetweenWrites;
                  } else if (var2 <= 0L) {
                     break;
                  }

                  long var4 = System.currentTimeMillis();

                  try {
                     TxQueueFileImpl.this.commitMutex.wait(var2);
                  } catch (InterruptedException var11) {
                  }

                  if (TxQueueFileImpl.this.isShutdown) {
                     break;
                  }

                  var2 -= System.currentTimeMillis() - var4;
               }

               if (TxQueueFileImpl.this.commitQueue != null) {
                  try {
                     TxQueueFileImpl.this.write(TxQueueFileImpl.this.commitQueue, TxQueueFileImpl.this.commitInserts, TxQueueFileImpl.this.commitNumRemoved, TxQueueFileImpl.this.commitFile);
                     TxQueueFileImpl.this.commitIOE = null;
                  } catch (IOException var10) {
                     TxQueueFileImpl.this.commitIOE = var10;
                  }
               }

               synchronized(TxQueueFileImpl.this.commitWriteMutex) {
                  TxQueueFileImpl.this.commitQueue = null;
                  TxQueueFileImpl.this.commitWriteMutex.notifyAll();
               }

               if (TxQueueFileImpl.this.isShutdown) {
                  return;
               }
            }
         }
      }

      public void write() {
         if (TxQueueFileImpl.this.isShutdown) {
            throw new RuntimeException("Queue has been shutdown");
         } else {
            synchronized(TxQueueFileImpl.this.commitWriteMutex) {
               while(TxQueueFileImpl.this.commitQueue != null) {
                  try {
                     TxQueueFileImpl.this.commitWriteMutex.wait();
                  } catch (InterruptedException var4) {
                  }
               }

            }
         }
      }
   }

   private class PrepareThread extends Thread {
      private int writeCalled = 0;
      private int writeDone = 0;

      public PrepareThread(String var2) {
         super(var2);
      }

      public void run() {
         while(true) {
            synchronized(TxQueueFileImpl.this.preparationMutex) {
               long var2 = (long)TxQueueFileImpl.this.minMillisBetweenWrites;

               while(true) {
                  if (TxQueueFileImpl.this.preparationQueue == null) {
                     var2 = (long)TxQueueFileImpl.this.minMillisBetweenWrites;
                  } else if (var2 <= 0L) {
                     break;
                  }

                  long var4 = System.currentTimeMillis();

                  try {
                     TxQueueFileImpl.this.preparationMutex.wait(var2);
                  } catch (InterruptedException var11) {
                  }

                  if (TxQueueFileImpl.this.isShutdown) {
                     break;
                  }

                  var2 -= System.currentTimeMillis() - var4;
               }

               if (TxQueueFileImpl.this.preparationQueue != null) {
                  try {
                     TxQueueFileImpl.this.write(TxQueueFileImpl.this.preparationQueue, TxQueueFileImpl.this.preparationInserts, TxQueueFileImpl.this.preparationNumRemoved, TxQueueFileImpl.this.prepareFile);
                     TxQueueFileImpl.this.preparationIOE = null;
                  } catch (IOException var10) {
                     TxQueueFileImpl.this.preparationIOE = var10;
                  }
               }

               synchronized(TxQueueFileImpl.this.prepareWriteMutex) {
                  TxQueueFileImpl.this.preparationQueue = null;
                  TxQueueFileImpl.this.prepareWriteMutex.notifyAll();
               }

               if (TxQueueFileImpl.this.isShutdown) {
                  return;
               }
            }
         }
      }

      public void write() {
         if (TxQueueFileImpl.this.isShutdown) {
            throw new RuntimeException("Queue has been shutdown");
         } else {
            synchronized(TxQueueFileImpl.this.prepareWriteMutex) {
               while(TxQueueFileImpl.this.preparationQueue != null) {
                  try {
                     TxQueueFileImpl.this.prepareWriteMutex.wait();
                  } catch (InterruptedException var4) {
                  }
               }

            }
         }
      }
   }
}
