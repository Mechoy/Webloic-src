package weblogic.messaging.path;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.runtime.PSAssemblyRuntimeMBean;
import weblogic.messaging.path.helper.KeySerializable;
import weblogic.messaging.path.helper.KeyString;
import weblogic.store.PersistentMapAsyncTX;
import weblogic.store.PersistentStoreException;

public class PSAssemblyRuntimeDelegate extends PSEntryCursorRuntimeDelegate implements PSAssemblyRuntimeMBean {
   private static transient long counter;
   private transient Key sampleKey;
   private transient PathServiceMap pathService;

   public PSAssemblyRuntimeDelegate(Key var1, PathServiceRuntimeDelegate var2, PathServiceMap var3) throws ManagementException {
      super(var1.getAssemblyId() + "." + getNewCounter(), var2);
      this.pathService = var3;
      this.sampleKey = var1;
   }

   private static synchronized long getNewCounter() {
      return (long)(counter++);
   }

   public String getMapEntries() throws ManagementException {
      return this.getMapEntries(0);
   }

   public String getMapEntries(int var1) throws ManagementException {
      Set var2;
      try {
         PersistentMapAsyncTX var3 = this.pathService.mapByKey(this.sampleKey);
         var2 = var3.keySet();
      } catch (PersistentStoreException var7) {
         throw new ManagementException(var7.getMessage());
      }

      Key[] var8 = new Key[var2.size()];
      Iterator var4 = var2.iterator();
      int var5 = 0;

      while(var4.hasNext()) {
         Object var6 = var4.next();
         if (var6 instanceof String) {
            var8[var5++] = new KeyString(this.sampleKey.getSubsystem(), this.sampleKey.getAssemblyId(), (String)var6);
         } else {
            var8[var5++] = new KeySerializable(this.sampleKey.getSubsystem(), this.sampleKey.getAssemblyId(), (Serializable)var6);
         }
      }

      PSEntryCursorDelegate var9 = new PSEntryCursorDelegate(this, new PSEntryOpenDataHelper(), var1, var8, this.pathService);
      this.addCursorDelegate(var9);
      return var9.getHandle();
   }
}
