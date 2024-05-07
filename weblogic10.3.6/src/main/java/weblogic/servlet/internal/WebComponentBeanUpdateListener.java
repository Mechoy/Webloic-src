package weblogic.servlet.internal;

import java.util.List;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;

public abstract class WebComponentBeanUpdateListener implements BeanUpdateListener {
   private PersistenceUnitRegistry persistenceUnitRegistry;

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      DescriptorBean var3 = var1.getProposedBean();
      BeanUpdateEvent.PropertyUpdate[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BeanUpdateEvent.PropertyUpdate var7 = var4[var6];
         Object var8;
         switch (var7.getUpdateType()) {
            case 1:
               this.handlePropertyChange(var7, var3);
               break;
            case 2:
               var8 = var7.getAddedObject();
               if (var8 instanceof DescriptorBean) {
                  this.handleBeanAdd(var7, (DescriptorBean)var8);
               } else {
                  this.handlePropertyAdd(var7, var3);
               }
               break;
            case 3:
               var8 = var7.getRemovedObject();
               if (var8 instanceof DescriptorBean) {
                  this.handleBeanRemove(var7);
               } else {
                  this.handlePropertyRemove(var7);
               }
         }
      }

   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      BeanUpdateEvent.PropertyUpdate[] var3 = var2;
      int var4 = var2.length;
      int var5 = 0;

      while(var5 < var4) {
         BeanUpdateEvent.PropertyUpdate var6 = var3[var5];
         switch (var6.getUpdateType()) {
            case 2:
               Object var7 = var6.getAddedObject();
               if (var7 instanceof DescriptorBean) {
                  this.prepareBeanAdd(var6, (DescriptorBean)var7);
               }
            case 1:
            case 3:
            default:
               ++var5;
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   protected abstract void prepareBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) throws BeanUpdateRejectedException;

   protected void handlePropertyAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
      this.handlePropertyChange(var1, var2);
   }

   protected abstract void handlePropertyChange(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2);

   protected abstract void handlePropertyRemove(BeanUpdateEvent.PropertyUpdate var1);

   protected abstract void handleBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2);

   protected abstract void handleBeanRemove(BeanUpdateEvent.PropertyUpdate var1);

   protected static boolean computeChange(String var0, boolean var1, boolean var2, List<String> var3) {
      boolean var4 = var2 != var1;
      if (var4) {
         var3.add(var0);
      }

      return var4;
   }

   protected static boolean computeChange(String var0, int var1, int var2, List<String> var3) {
      boolean var4 = var2 != var1;
      if (var4) {
         var3.add(var0);
      }

      return var4;
   }

   protected static boolean computeChange(String var0, Object var1, Object var2, List<String> var3) {
      boolean var4 = var2 == null && var1 != null || var2 != null && !var2.equals(var1);
      if (var4) {
         var3.add(var0);
      }

      return var4;
   }

   protected static String getChangedPropertyNames(List<String> var0) {
      if (var0 == null && var0.isEmpty()) {
         return "No change is found.";
      } else {
         StringBuilder var1 = new StringBuilder();
         int var2 = 0;

         for(int var3 = var0.size(); var2 < var3; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            var1.append((String)var0.get(var2));
         }

         return var1.toString();
      }
   }
}
