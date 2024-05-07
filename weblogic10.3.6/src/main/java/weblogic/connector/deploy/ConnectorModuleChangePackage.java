package weblogic.connector.deploy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.connector.configuration.DDUtil;
import weblogic.connector.exception.RAException;
import weblogic.connector.external.RAInfo;

class ConnectorModuleChangePackage {
   private ArrayList<ChangePackage> pendingChanges = new ArrayList();

   protected void prepare(RAInfo var1) throws RAException {
      DDUtil.validateRAInfo(var1);
      int var2 = 0;

      try {
         for(int var3 = this.pendingChanges.size(); var2 < var3; ++var2) {
            ChangePackage var8 = (ChangePackage)this.pendingChanges.get(var2);
            var8.prepare();
         }

      } catch (RAException var7) {
         for(int var4 = 0; var4 <= var2; ++var4) {
            try {
               ChangePackage var5 = (ChangePackage)this.pendingChanges.get(var4);
               var5.rollback();
            } catch (RAException var6) {
            }
         }

         throw var7;
      }
   }

   protected void activate() throws RAException {
      RAException var1 = null;
      Iterator var2 = this.pendingChanges.iterator();

      while(var2.hasNext()) {
         ChangePackage var3 = (ChangePackage)var2.next();

         try {
            var3.activate();
         } catch (RAException var5) {
            if (var1 == null) {
               var1 = new RAException();
            }

            var1.add(var5);
         }
      }

      if (var1 != null) {
         throw var1;
      }
   }

   protected void addChange(ChangePackage var1) {
      if (var1 != null) {
         this.pendingChanges.add(var1);
      }

   }

   protected void addChanges(List<? extends ChangePackage> var1) {
      this.pendingChanges.addAll(var1);
   }

   public static enum ChangeType {
      NEW,
      REMOVE,
      UPDATE;
   }
}
