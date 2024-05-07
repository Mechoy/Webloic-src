package weblogic.persist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.PrintStream;
import java.io.StreamCorruptedException;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRolledbackException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.jndi.Environment;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.utils.enumerations.BatchingEnumerationWrapper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.t3.srvr.T3Srvr;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.UnsyncHashtable;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public class TxIndexedFileImpl implements TxIndexedFileRemote, XAResource, PlatformConstants {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean VERBOSE = false;
   int fullWriteInterval = Integer.getInteger("txindexedfile.fullwriteinterval", 100);
   static PrintStream ps;
   private String name;
   private String commitFilename;
   private String prepareFilename;
   private UnsyncHashtable database;
   private CommitThread commitThread;
   private PrepareThread prepareThread;
   private UnsyncHashtable bytes = new UnsyncHashtable();
   private ByteArrayOutputStream baos;
   private UnsyncHashtable enrolled = new UnsyncHashtable();
   private static Object exists = new Object();
   private UnsyncHashtable writeLocks = new UnsyncHashtable();
   private UnsyncHashtable changes = new UnsyncHashtable();
   private static Object removeObject = "\u00000\u00000\u00000\u00000";
   private int numWrites = 0;
   protected Object preparationMutex = new Object();
   protected Object commitMutex = new Object();
   protected UnsyncHashtable preparationChanges;
   protected IOException preparationIOE;
   protected UnsyncHashtable commitChanges;
   protected IOException commitIOE;
   protected File commitFile;
   protected File prepareFile;
   private static final int MIN_MILLIS_BETWEEN_WRITES = Integer.getInteger("txindexedfile.writemillis", 50);
   private Object prepareWriteMutex = new Object();
   private Object commitWriteMutex = new Object();
   protected boolean isShutdown = false;
   private static TransactionManager tms = null;

   public static void main(String[] var0) {
      try {
         String var1 = var0.length >= 1 ? var0[0] : "DefaultStore";
         String var2 = var0.length >= 2 ? var0[1] : ManagementService.getRuntimeAccess(kernelId).getServer().getName();
         String var3 = var0.length >= 3 ? var0[2] : ManagementService.getRuntimeAccess(kernelId).getServer().getName();
         TxIndexedFileStub var4 = new TxIndexedFileStub(var1, var2, var3);

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

   private UnsyncHashtable readDatabase(File var1) throws IOException, ClassNotFoundException {
      FileInputStream var2 = null;
      MyObjectInputStream var3 = null;
      UnsyncHashtable var4 = null;

      try {
         var2 = new FileInputStream(var1);
         var3 = new MyObjectInputStream(var2);
         var4 = (UnsyncHashtable)var3.readObject();

         while(true) {
            try {
               var3 = new MyObjectInputStream(var2);
               var3.readBoolean();
            } catch (IOException var11) {
               return var4;
            }

            try {
               UnsyncHashtable var5 = (UnsyncHashtable)var3.readObject();
               this.fillInDatabase(var4, var5);
            } catch (IOException var10) {
               throw var10;
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

   private void loadDatabase(File var1, File var2) throws IOException {
      this.database = new UnsyncHashtable();
      this.ensureDirectories(var1, var2);
      this.commitFile = new File(this.commitFilename);
      this.prepareFile = new File(this.prepareFilename);
      if (this.commitFile.exists()) {
         if (this.prepareFile.exists() && this.prepareFile.lastModified() > this.commitFile.lastModified()) {
            try {
               this.database = this.readDatabase(this.prepareFile);
               return;
            } catch (Exception var7) {
            }
         }

         try {
            this.database = this.readDatabase(this.commitFile);
            return;
         } catch (Exception var6) {
            try {
               if (this.prepareFile.exists() && this.prepareFile.lastModified() < this.commitFile.lastModified()) {
                  this.database = this.readDatabase(this.prepareFile);
                  return;
               }
            } catch (Exception var5) {
               return;
            }
         }
      } else {
         try {
            if (this.prepareFile.exists()) {
               this.database = this.readDatabase(this.prepareFile);
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

   protected TxIndexedFileImpl(String var1, String var2, String var3) throws IOException {
      this.name = var1;
      this.commitFilename = var2 + File.separator + var1 + ".dat";
      this.prepareFilename = var3 + File.separator + var1 + "Tmp.dat";
      this.loadDatabase(new File(var2), new File(var3));
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

   public void store(String var1, Object var2) throws IOException, TransactionRolledbackException {
      Transaction var3 = null;

      try {
         var3 = getTransaction();
      } catch (SystemException var7) {
         throw new TransactionRolledbackException("Could not obtain transaction:\n " + var7.getMessage());
      }

      if (var3 == null) {
         try {
            getTransactionManager().begin("persist internal");
            var3 = getTransaction();
            var3.enlistResource(this);
            this.write(var3.getXID(), var1, var2);
            var3.commit();
         } catch (Exception var6) {
            var6.printStackTrace();
            throw new TransactionRolledbackException("Could not complete transaction . . . Rolled back: " + var6.getMessage());
         }
      } else {
         try {
            synchronized(this) {
               if (this.enrolled.put(var3.getXID(), exists) == null) {
                  var3.enlistResource(this);
               }
            }
         } catch (Exception var9) {
            throw new TransactionRolledbackException("Could not enroll resource: " + var9.getMessage());
         }

         this.write(var3.getXID(), var1, var2);
      }

   }

   public Object retrieve(String var1) {
      Transaction var2 = null;

      try {
         var2 = getTransaction();
      } catch (SystemException var4) {
         var4.printStackTrace();
         return null;
      }

      return this.read(var2 == null ? null : var2.getXID(), var1);
   }

   public Enumeration keys() {
      synchronized(this) {
         if (this.baos == null) {
            this.baos = new ByteArrayOutputStream();
         } else {
            this.baos.reset();
         }

         UnsyncHashtable var1;
         try {
            ObjectOutputStream var3 = new ObjectOutputStream(this.baos);
            var3.writeObject(this.database);
            var3.flush();
            var3.close();
            byte[] var4 = this.baos.toByteArray();
            MyObjectInputStream var5 = new MyObjectInputStream(new ByteArrayInputStream(var4));
            var1 = (UnsyncHashtable)var5.readObject();
            var5.close();
         } catch (Exception var7) {
            throw new IllegalArgumentException("Could not make clone of object: " + var7);
         }

         return new BatchingEnumerationWrapper(var1.keys(), 10);
      }
   }

   private void write(Xid var1, String var2, Object var3) {
      synchronized(this.writeLocks) {
         Transaction var5 = (Transaction)this.writeLocks.get(var2);

         while(true) {
            if (var5 == null || var5.equals(var1)) {
               this.writeLocks.put(var2, var1);
               break;
            }

            synchronized(var5) {
               try {
                  var5.wait(100L);
               } catch (InterruptedException var12) {
               }

               var5 = (Transaction)this.writeLocks.get(var2);
            }
         }
      }

      synchronized(this.changes) {
         UnsyncHashtable var15 = (UnsyncHashtable)this.changes.get(var1);
         if (var15 == null) {
            var15 = new UnsyncHashtable();
            this.changes.put(var1, var15);
         }

         if (var3 == null) {
            var3 = removeObject;
         }

         var15.put(var2, var3);
      }
   }

   private Object read(Xid var1, String var2) {
      Xid var3;
      synchronized(this.writeLocks) {
         var3 = (Xid)this.writeLocks.get(var2);

         while(var3 != null && !var3.equals(var1)) {
            synchronized(var3) {
               try {
                  var3.wait();
               } catch (InterruptedException var11) {
               }

               var3 = (Xid)this.writeLocks.get(var2);
            }
         }
      }

      synchronized(this) {
         if (var1 != null && var1.equals(var3)) {
            UnsyncHashtable var5 = (UnsyncHashtable)this.changes.get(var1);
            if (var5 != null) {
               Object var6 = var5.get(var2);
               if (var6 == removeObject) {
                  return null;
               }

               if (var6 != null) {
                  return var6;
               }
            }
         }

         return this.database.get(var2);
      }
   }

   private synchronized void releaseLocks(Xid var1) {
      this.enrolled.remove(var1);
      this.bytes.remove(var1);
      UnsyncHashtable var2 = (UnsyncHashtable)this.changes.remove(var1);
      Enumeration var3 = var2.keys();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         Object var5 = this.writeLocks.remove(var4);
         if (var5 != null && !var1.equals(var5)) {
            synchronized(var5) {
               var5.notifyAll();
            }
         }
      }

      synchronized(var1) {
         var1.notifyAll();
      }
   }

   protected void write(UnsyncHashtable var1, File var2) throws IOException {
      int var3 = this.numWrites++ >> 2;
      boolean var4 = var3 % this.fullWriteInterval != 0;
      UnsyncByteArrayOutputStream var5 = new UnsyncByteArrayOutputStream();
      ObjectOutputStream var6 = new ObjectOutputStream(var5);
      if (var4) {
         var6.writeBoolean(true);
         var6.writeObject(var1);
      } else {
         UnsyncHashtable var7;
         synchronized(this) {
            var7 = (UnsyncHashtable)this.database.clone();
         }

         this.fillInDatabase(var7, var1);
         var6.writeObject(var7);
      }

      var6.flush();
      var6.close();
      var5.close();
      FileOutputStream var11 = new FileOutputStream(var2.getAbsolutePath(), var4);
      var11.write(var5.toByteArray());
      var11.flush();
      var11.getFD().sync();
      var11.close();
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
      try {
         synchronized(this.preparationMutex) {
            synchronized(this.prepareWriteMutex) {
               if (this.preparationChanges == null) {
                  this.preparationChanges = new UnsyncHashtable();
               }

               this.copy(this.preparationChanges, (UnsyncHashtable)this.changes.get(var1));
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      this.prepareThread.write();
      if (this.preparationIOE != null) {
         throw new XAException(StackTraceUtils.throwable2StackTrace(this.preparationIOE));
      } else {
         return 0;
      }
   }

   public void rollback(Xid var1) throws XAException {
      this.commitThread.write();
      this.releaseLocks(var1);
   }

   public void commit(Xid var1, boolean var2) throws XAException {
      if (var2) {
         try {
            int var3 = this.prepare(var1);
            if (var3 == 0) {
               this.commit(var1);
            }
         } catch (XAException var4) {
            this.decipherFailure("prepare", this.prepareFilename, (IOException)null);
         }

      } else {
         this.commit(var1);
      }
   }

   public void commit(Xid var1) throws XAException {
      synchronized(this.commitMutex) {
         synchronized(this.commitWriteMutex) {
            if (this.commitChanges == null) {
               this.commitChanges = new UnsyncHashtable();
            }

            this.copy(this.commitChanges, (UnsyncHashtable)this.changes.get(var1));
         }
      }

      this.commitThread.write();
      if (this.commitIOE != null) {
         this.decipherFailure("commit", this.commitFilename, this.commitIOE);
      }

      this.fillInDatabase(this.database, var1);
      this.releaseLocks(var1);
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
         throw new XAException(var7 + "Possible problems: " + EOL + "  Disk full" + EOL + "  No available file descriptors (Unix)" + EOL + "  File owned by different process (NT)" + EOL + "  Hardware error");
      }
   }

   private void fillInDatabase(UnsyncHashtable var1, Xid var2) {
      this.fillInDatabase(var1, (UnsyncHashtable)null, var2);
   }

   private void fillInDatabase(UnsyncHashtable var1, UnsyncHashtable var2, Xid var3) {
      synchronized(this) {
         UnsyncHashtable var5 = (UnsyncHashtable)this.changes.get(var3);
         Enumeration var6 = var5.keys();

         while(var6.hasMoreElements()) {
            String var7 = (String)var6.nextElement();
            Object var8 = var5.get(var7);
            if (var2 != null) {
               var2.put(var7, var8);
            }

            if (var8.equals(removeObject)) {
               var1.remove(var7);
            } else {
               var1.put(var7, var8);
            }
         }

      }
   }

   private void fillInDatabase(UnsyncHashtable var1, UnsyncHashtable var2) {
      Enumeration var3 = var2.keys();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         Object var5 = var2.get(var4);
         if (var5.equals(removeObject)) {
            var1.remove(var4);
         } else {
            var1.put(var4, var5);
         }
      }

   }

   private void copy(UnsyncHashtable var1, UnsyncHashtable var2) {
      Enumeration var3 = var2.keys();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         Object var5 = var2.get(var4);
         var1.put(var4, var5);
      }

   }

   private static Transaction getTransaction() throws SystemException {
      return (Transaction)getTransactionManager().getTransaction();
   }

   private static TransactionManager getTransactionManager() throws SystemException {
      if (tms == null) {
         Class var0 = TxIndexedFileImpl.class;
         synchronized(TxIndexedFileImpl.class) {
            if (tms == null) {
               try {
                  Environment var1 = new Environment();
                  var1.setCreateIntermediateContexts(true);
                  Context var2 = var1.getInitialContext();
                  tms = (TransactionManager)var2.lookup("weblogic.transaction.TransactionManager");
               } catch (NamingException var4) {
                  throw new SystemException("Naming lookup problem: " + StackTraceUtils.throwable2StackTrace(var4));
               }
            }
         }
      }

      return tms;
   }

   private class CommitThread extends Thread {
      private int writeCalled = 0;
      private int writeDone = 0;

      public CommitThread(String var2) {
         super(var2);
      }

      public void run() {
         while(true) {
            synchronized(TxIndexedFileImpl.this.commitMutex) {
               long var2 = (long)TxIndexedFileImpl.MIN_MILLIS_BETWEEN_WRITES;

               while(true) {
                  if (TxIndexedFileImpl.this.commitChanges == null) {
                     var2 = (long)TxIndexedFileImpl.MIN_MILLIS_BETWEEN_WRITES;
                  } else if (var2 <= 0L) {
                     break;
                  }

                  long var4 = System.currentTimeMillis();

                  try {
                     TxIndexedFileImpl.this.commitMutex.wait(var2);
                  } catch (InterruptedException var11) {
                  }

                  if (TxIndexedFileImpl.this.isShutdown) {
                     break;
                  }

                  var2 -= System.currentTimeMillis() - var4;
               }

               if (TxIndexedFileImpl.this.commitChanges != null) {
                  try {
                     TxIndexedFileImpl.this.write(TxIndexedFileImpl.this.commitChanges, TxIndexedFileImpl.this.commitFile);
                     TxIndexedFileImpl.this.commitIOE = null;
                  } catch (IOException var10) {
                     TxIndexedFileImpl.this.commitIOE = var10;
                  }
               }

               synchronized(TxIndexedFileImpl.this.commitWriteMutex) {
                  TxIndexedFileImpl.this.commitChanges = null;
                  TxIndexedFileImpl.this.commitWriteMutex.notifyAll();
               }

               if (TxIndexedFileImpl.this.isShutdown) {
                  return;
               }
            }
         }
      }

      public void write() {
         if (TxIndexedFileImpl.this.isShutdown) {
            throw new RuntimeException("Database has been shutdown");
         } else {
            synchronized(TxIndexedFileImpl.this.commitWriteMutex) {
               while(TxIndexedFileImpl.this.commitChanges != null) {
                  try {
                     TxIndexedFileImpl.this.commitWriteMutex.wait();
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
            synchronized(TxIndexedFileImpl.this.preparationMutex) {
               long var2 = (long)TxIndexedFileImpl.MIN_MILLIS_BETWEEN_WRITES;

               while(true) {
                  if (TxIndexedFileImpl.this.preparationChanges == null) {
                     var2 = (long)TxIndexedFileImpl.MIN_MILLIS_BETWEEN_WRITES;
                  } else if (var2 <= 0L) {
                     break;
                  }

                  long var4 = System.currentTimeMillis();

                  try {
                     TxIndexedFileImpl.this.preparationMutex.wait(var2);
                  } catch (InterruptedException var11) {
                  }

                  if (TxIndexedFileImpl.this.isShutdown) {
                     break;
                  }

                  var2 -= System.currentTimeMillis() - var4;
               }

               if (TxIndexedFileImpl.this.preparationChanges != null) {
                  try {
                     TxIndexedFileImpl.this.write(TxIndexedFileImpl.this.preparationChanges, TxIndexedFileImpl.this.prepareFile);
                     TxIndexedFileImpl.this.preparationIOE = null;
                  } catch (IOException var10) {
                     TxIndexedFileImpl.this.preparationIOE = var10;
                  }
               }

               synchronized(TxIndexedFileImpl.this.prepareWriteMutex) {
                  TxIndexedFileImpl.this.preparationChanges = null;
                  TxIndexedFileImpl.this.prepareWriteMutex.notifyAll();
               }

               if (TxIndexedFileImpl.this.isShutdown) {
                  return;
               }
            }
         }
      }

      public void write() {
         if (TxIndexedFileImpl.this.isShutdown) {
            throw new RuntimeException("Database has been shutdown");
         } else {
            synchronized(TxIndexedFileImpl.this.prepareWriteMutex) {
               while(TxIndexedFileImpl.this.preparationChanges != null) {
                  try {
                     TxIndexedFileImpl.this.prepareWriteMutex.wait();
                  } catch (InterruptedException var4) {
                  }
               }

            }
         }
      }
   }

   private static class MyObjectInputStream extends ObjectInputStream {
      public MyObjectInputStream(InputStream var1) throws IOException, StreamCorruptedException {
         super(var1);
      }

      protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
         ClassLoader var2 = Thread.currentThread().getContextClassLoader();
         return var2.loadClass(var1.getName());
      }
   }
}
