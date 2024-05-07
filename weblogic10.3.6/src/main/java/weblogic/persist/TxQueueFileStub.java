package weblogic.persist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.naming.NamingException;
import javax.transaction.TransactionRolledbackException;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;

public class TxQueueFileStub implements TxQueueFile, Externalizable {
   private TxQueueFileRemote queue;
   private ByteArrayOutputStream baos;

   public TxQueueFileStub() {
   }

   public TxQueueFileStub(String var1, String var2, String var3) throws NamingException {
      try {
         this.queue = new TxQueueFileImpl(var1, var2, var3);
      } catch (IOException var6) {
         NamingException var5 = new NamingException("Can't create impl.");
         var5.setRootCause(var6);
         throw var5;
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.queue = (TxQueueFileRemote)((WLObjectInput)var1).readObjectWL();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ((WLObjectOutput)var1).writeObjectWL(this.queue);
   }

   public String getName() {
      return this.queue.getName();
   }

   public void put(Object var1) throws IOException {
      if (this.queue instanceof TxQueueFileImpl) {
         if (this.baos == null) {
            this.baos = new ByteArrayOutputStream();
         }

         try {
            synchronized(this.baos) {
               this.baos.reset();
               ObjectOutputStream var3 = new ObjectOutputStream(this.baos);
               var3.writeObject(var1);
               var3.flush();
               var3.close();
               byte[] var4 = this.baos.toByteArray();
               ObjectInputStream var5 = new ObjectInputStream(new ByteArrayInputStream(var4));
               var1 = (Serializable)var5.readObject();
               var5.close();
            }
         } catch (Exception var8) {
            throw new IllegalArgumentException("Could not make clone of object: " + var8);
         }
      }

      this.queue.put(var1);
   }

   public Object get() throws TransactionRolledbackException {
      return this.queue.get();
   }

   public Object getW() throws DeadlockException, TransactionRolledbackException {
      return this.queue.getW();
   }

   public Object getW(long var1) throws QueueTimeoutException, TransactionRolledbackException {
      return this.queue.getW(var1);
   }

   public void shutdown() {
      this.queue.shutdown();
   }
}
