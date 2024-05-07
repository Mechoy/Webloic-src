package weblogic.jms.forwarder.dd.internal;

import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.forwarder.dd.DDForwardStore;
import weblogic.jms.forwarder.dd.DDInfo;
import weblogic.jms.forwarder.dd.DDLBTable;
import weblogic.jms.forwarder.dd.DDMemberInfo;
import weblogic.jms.store.JMSObjectHandler;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.xa.PersistentStoreXA;

public class DDForwardStoreImpl implements DDForwardStore {
   private PersistentStoreXA persistentStore;
   private String name;
   private DDInfo ddInfo;
   private PersistentStoreConnection storeConnection;
   private DDLBTable ddLBTable;
   private PersistentHandle handle;
   private static final int NO_FLAGS = 0;
   private boolean poisoned;

   public DDForwardStoreImpl(String var1, DDInfo var2, PersistentStoreXA var3) throws JMSException {
      this.name = var1;
      this.persistentStore = var3;
      this.ddInfo = var2;

      try {
         this.storeConnection = var3.createConnection(var1, new JMSObjectHandler());
         this.open();
      } catch (PersistentStoreException var6) {
         JMSException var5 = new JMSException(var6.getMessage());
         var5.setLinkedException(var6);
         throw var5;
      }
   }

   private void open() throws JMSException {
      this.recover();
   }

   private void recover() throws JMSException {
      DDLBTable var2 = null;

      PersistentStoreRecord var1;
      try {
         for(PersistentStoreConnection.Cursor var3 = this.storeConnection.createCursor(32); (var1 = var3.next()) != null; var2 = (DDLBTable)var1.getData()) {
            this.handle = var1.getHandle();
         }
      } catch (PersistentStoreException var9) {
         JMSException var4 = new JMSException(var9.getMessage());
         var4.setLinkedException(var9);
         throw var4;
      }

      this.ddLBTable = new DDLBTableImpl(this.name, this.ddInfo);
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("ddLBTableOnBoot = " + var2);
      }

      if (var2 != null) {
         HashMap var10 = var2.getFailedDDMemberInfosBySeqNum();
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("failedDDMemberInfos = " + var10);
         }

         Iterator var11 = var10.values().iterator();

         while(var11.hasNext()) {
            DDMemberInfo var5 = (DDMemberInfo)var11.next();
            DestinationImpl var6 = var5.getDestination();
            if (var6 != null) {
               var6.markStale();
            }
         }

         DDMemberInfo[] var12 = var2.getDDMemberInfos();
         if (var12 != null) {
            for(int var13 = 0; var13 < var12.length; ++var13) {
               DDMemberInfo var7 = var12[var13];
               DestinationImpl var8 = var7.getDestination();
               if (var8 != null) {
                  var8.markStale();
               }
            }
         }

         var2.removeDDMemberInfos();
         var2.removeInDoubtDDMemberInfos();
         this.ddLBTable.setFailedDDMemberInfosBySeqNum(var10);
         this.ddLBTable.addInDoubtDDMemberInfos(var12);
      }

   }

   public PersistentStoreXA getStore() {
      return this.persistentStore;
   }

   public void addOrUpdateDDLBTable(DDLBTable var1) throws PersistentStoreException {
      PersistentStoreTransaction var2 = this.persistentStore.begin();
      if (this.handle == null) {
         this.handle = this.storeConnection.create(var2, var1, 0);
      } else {
         this.storeConnection.update(var2, this.handle, var1, 0);
      }

      var2.commit();
   }

   public void removeDDLBTable() throws PersistentStoreException {
      if (this.handle != null) {
         PersistentStoreTransaction var1 = this.persistentStore.begin();
         this.storeConnection.delete(var1, this.handle, 0);
         var1.commit();
         this.handle = null;
      }
   }

   public void close() {
      this.storeConnection.close();
   }

   public void poisoned() {
      this.poisoned = true;
   }

   public boolean isPoisoned() {
      return this.poisoned;
   }

   public DDLBTable getDDLBTable() {
      return this.ddLBTable;
   }
}
