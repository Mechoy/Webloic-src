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
import java.util.Enumeration;
import javax.naming.NamingException;
import weblogic.utils.StackTraceUtils;

public class TxIndexedFileStub implements Externalizable, TxIndexedFile {
   static final long serialVersionUID = -1563396532888782603L;
   private TxIndexedFileRemote ph;
   private ByteArrayOutputStream baos;

   public TxIndexedFileStub() {
   }

   public TxIndexedFileStub(String var1, String var2, String var3) throws NamingException {
      try {
         this.ph = new TxIndexedFileImpl(var1, var2, var3);
      } catch (IOException var6) {
         NamingException var5 = new NamingException("Can't create impl.");
         var5.setRootCause(var6);
         throw var5;
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.ph = (TxIndexedFileRemote)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.ph);
   }

   public String getName() {
      return this.ph.getName();
   }

   public void store(String var1, Object var2) throws IOException {
      if (this.ph instanceof TxIndexedFileImpl) {
         if (this.baos == null) {
            this.baos = new ByteArrayOutputStream() {
               public void close() {
               }
            };
         }

         try {
            synchronized(this.baos) {
               this.baos.reset();
               ObjectOutputStream var4 = new ObjectOutputStream(this.baos);
               var4.writeObject(var2);
               var4.flush();
               var4.close();
               byte[] var5 = this.baos.toByteArray();
               ObjectInputStream var6 = new ObjectInputStream(new ByteArrayInputStream(var5));
               var2 = (Serializable)var6.readObject();
               var6.close();
            }
         } catch (Exception var9) {
            throw new IOException("Could not make clone of object: " + StackTraceUtils.throwable2StackTrace(var9));
         }
      }

      this.ph.store(var1, var2);
   }

   public Object retrieve(String var1) {
      return this.ph.retrieve(var1);
   }

   public Enumeration keys() {
      return this.ph.keys();
   }

   public void shutdown() {
      this.ph.shutdown();
   }
}
