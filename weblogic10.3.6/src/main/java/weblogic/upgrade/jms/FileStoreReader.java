package weblogic.upgrade.jms;

import java.io.File;
import java.io.IOException;
import javax.jms.JMSException;
import weblogic.management.configuration.GenericFileStoreMBean;
import weblogic.store.PersistentStoreException;
import weblogic.store.StoreWritePolicy;
import weblogic.store.admin.FileAdminHandler;
import weblogic.store.io.file.Heap;

public class FileStoreReader implements StoreReader {
   private String storeName;
   private String directoryName;
   private Heap heap;
   private UpgradeIOBypass ioBypass;
   private boolean open;

   public FileStoreReader(GenericFileStoreMBean var1, UpgradeIOBypass var2) throws JMSException {
      this.storeName = var1.getName();
      this.ioBypass = var2;
      this.directoryName = FileAdminHandler.canonicalizeDirectoryName(var1.getDirectory());
      this.reOpen();
   }

   public void reOpen() throws JMSException {
      this.close();
      if (this.directoryName != null) {
         File var1 = new File(this.directoryName);
         if (var1.exists() && var1.isDirectory()) {
            File[] var2 = var1.listFiles();
            if (var2 != null && var2.length > 0) {
               try {
                  this.heap = new Heap(this.storeName, this.directoryName, false);
                  this.heap.setSynchronousWritePolicy(StoreWritePolicy.DISABLED);
                  this.heap.open();
               } catch (PersistentStoreException var4) {
                  throw new weblogic.jms.common.JMSException(var4);
               }

               this.open = true;
            }
         }

      }
   }

   public boolean requiresUpgrade() {
      return this.open;
   }

   public boolean alreadyUpgraded() {
      return this.open && this.heap.getHeapVersion() == 2;
   }

   public void close() {
      try {
         if (this.open) {
            this.heap.close();
            this.open = false;
         }
      } catch (PersistentStoreException var2) {
      }

   }

   public StoreReader.Record recover() throws JMSException {
      Heap.HeapRecord var1;
      try {
         var1 = this.heap.recover();
      } catch (PersistentStoreException var3) {
         throw new weblogic.jms.common.JMSException(var3);
      }

      return var1 == null ? null : this.makeRecord(var1);
   }

   private StoreReader.Record makeRecord(Heap.HeapRecord var1) throws JMSException {
      try {
         BufferDataInputStream var2 = new BufferDataInputStream(this.ioBypass, var1.getBody());
         Object var3 = var2.readObject();
         return new StoreReader.Record(var1.getHandle(), var1.getOldState(), var3);
      } catch (IOException var4) {
         throw new weblogic.jms.common.JMSException(var4);
      } catch (ClassNotFoundException var5) {
         throw new weblogic.jms.common.JMSException(var5);
      }
   }
}
