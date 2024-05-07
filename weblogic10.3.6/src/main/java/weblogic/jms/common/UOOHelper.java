package weblogic.jms.common;

import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDMember;
import weblogic.jms.extensions.JMSOrderException;
import weblogic.jms.frontend.FEDDHandler;

public final class UOOHelper {
   public static boolean cacheUpToDate(FEDDHandler var0, String var1, boolean var2, MessageImpl var3) {
      DDMember var4 = var0.findDDMemberByMemberName(var1);
      if (var4 == null) {
         return false;
      } else if (!var4.hasConsumers() && var2) {
         return false;
      } else if (!var4.isProductionPaused() && !var4.isInsertionPaused()) {
         return var4.isPersistent() || var3.getAdjustedDeliveryMode() != 2;
      } else {
         return false;
      }
   }

   public static boolean hasConsumers(FEDDHandler var0, String var1) {
      DDMember var2 = var0.findDDMemberByMemberName(var1);
      return var2.hasConsumers();
   }

   public static DistributedDestinationImpl getHashBasedDestination(FEDDHandler var0, String var1) throws JMSException {
      DDHandler var2 = var0.getDDHandler();
      if (var2 == null) {
         throw new JMSOrderException("no ddHandler for " + var0.getName());
      } else {
         int var4 = 0;

         while(true) {
            int var5 = var2.getNumberOfMembers();
            if (var5 == 0) {
               throw new JMSOrderException("no known configured members for " + var2.getName());
            }

            int var6 = var1.hashCode() % var5;
            if (var6 < 0) {
               var6 = -var6;
            }

            DDMember var3 = var2.getMemberByIndex(var6);
            ++var4;
            if (var3 != null) {
               if (var3.isUp()) {
                  return var3.getDDImpl();
               }

               throw new JMSOrderException("hashed member of " + var2.getName() + " is " + var3.getName() + " which is not available");
            }

            try {
               Thread.sleep(2L);
            } catch (InterruptedException var8) {
               throw new AssertionError(var8);
            }

            if (var4 >= 20) {
               throw new JMSOrderException("could not get " + var6 + " from " + var3.getName() + " that has size " + var5);
            }

            ++var4;
         }
      }
   }
}
