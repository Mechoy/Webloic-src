package weblogic.ejb.container.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.transaction.Transaction;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.PassivatibleEntityCache;
import weblogic.ejb.container.utils.PartialOrderSet;

public final class EntityPassivator {
   static final String DISABLE_ENTITY_PASSIVATION_IN_TX_PROP = "weblogic.ejb.container.cache.disable_entity_passivation_in_tx";
   private static final boolean disable_entity_passivation_in_tx = System.getProperty("weblogic.ejb.container.cache.disable_entity_passivation_in_tx") != null;
   private static final DebugLogger debugLogger;
   private final PassivatibleEntityCache cache;

   public EntityPassivator(PassivatibleEntityCache var1) {
      this.cache = var1;
   }

   public long passivate(Transaction var1, long var2, int var4) {
      if (disable_entity_passivation_in_tx) {
         if (debugLogger.isDebugEnabled()) {
            debug("Passivation of Entity Beans in a transaction is disabled.  Skipping Entity passivation.");
         }

         return 0L;
      } else {
         assert var4 > 0 : "Bean Size " + var4 + " is <= 0, it must be greater than 0 !!";

         long var5 = (long)((double)(var2 / (long)var4) * 0.01);
         long var7 = (long)(10 * var4);
         long var9 = var5 > var7 ? var5 : var7;
         if (debugLogger.isDebugEnabled()) {
            debug(" passivate in our tx with target: " + var9);
         }

         long var11 = 0L;
         var11 = this.passivateInOurTx(var1, var9);
         if (debugLogger.isDebugEnabled()) {
            debug(" After passivation in our tx we've freed " + var11);
         }

         if (var11 >= var9) {
            return var11;
         } else {
            var11 += this.passivateNotInOurTx(var1, var9 - var11);
            if (debugLogger.isDebugEnabled()) {
               debug(" After passivation NOT in our tx we've freed " + var11);
            }

            return var11;
         }
      }
   }

   private long passivateInOurTx(Transaction var1, long var2) {
      long var4 = 0L;
      CachingManager var6 = null;
      ArrayList var7 = new ArrayList();
      this.setInTxManagers(var1, var7);
      if (debugLogger.isDebugEnabled()) {
         debug("\n\n+++ begin passivation in our Tx of unModified ops complete beans.\n\n");
      }

      LinkedList var8 = new LinkedList();
      Iterator var9 = var7.iterator();

      while(true) {
         PartialOrderSet var10;
         do {
            if (!var9.hasNext()) {
               if (debugLogger.isDebugEnabled()) {
                  debug("\n\n+++ begin passivation in our Tx of unModified non-ops complete beans.\n");
               }

               if (var8.size() > 0) {
                  Iterator var16 = var8.iterator();

                  while(var16.hasNext()) {
                     CacheKey var18 = (CacheKey)var16.next();
                     var4 += (long)var18.getCallback().passivateUnModifiedBean(var1, var18.getPrimaryKey());
                     if (debugLogger.isDebugEnabled()) {
                        debug("passivate target is " + var2 + ", and we have freed = " + var4 + ", after non-opsComplete cm.passivateUnModifiedBean on pk " + var18.getPrimaryKey());
                     }

                     if (var4 >= var2) {
                        return var4;
                     }
                  }
               }

               if (var6 == null) {
                  return var4;
               }

               if (debugLogger.isDebugEnabled()) {
                  debug("\n\n+++ begin passivation in our Tx of modified beans.\n");
               }

               boolean var17 = false;

               try {
                  if (debugLogger.isDebugEnabled()) {
                     debug("\n\n+++ flush modified beans \n");
                  }

                  var6.getTxManager().flushModifiedBeans(var1, true);
                  var17 = true;
               } catch (Throwable var15) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("\n flushModifiedBeans, ourTx " + var1 + "\n resulted in Throwable " + var15.toString() + "\n");
                  }
               }

               if (debugLogger.isDebugEnabled()) {
                  debug("\n flush modified success: " + (var17 ? "true" : "false"));
               }

               var8.clear();
               boolean var19 = false;
               var9 = var7.iterator();

               while(var9.hasNext() && !var19) {
                  var6 = (CachingManager)var9.next();
                  ArrayList var20 = var6.getTxManager().getFlushedKeys(var1);
                  if (var20 != null && var20.size() > 0) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("\n\n+++ processing " + var20.size() + " flushed ops complete beans\n");
                     }

                     Iterator var13 = var20.iterator();

                     while(var13.hasNext()) {
                        Object var14 = var13.next();
                        if (var6.beanIsOpsComplete(var1, var14)) {
                           var4 += (long)var6.passivateModifiedBean(var1, var14, var17);
                           if (debugLogger.isDebugEnabled()) {
                              debug("passivate target is " + var2 + ", and we have freed = " + var4 + ", after flushed bean cm.passivateModifiedBean on pk " + var14);
                           }

                           if (var4 >= var2) {
                              var19 = true;
                              break;
                           }
                        } else {
                           var8.add(new CacheKey(var14, var6));
                        }
                     }
                  }
               }

               if (var19) {
                  return var4;
               }

               if (debugLogger.isDebugEnabled()) {
                  debug("\n\n+++ begin passivation in our Tx of modified non-ops complete beans.\n");
               }

               if (var8.size() > 0) {
                  Iterator var21 = var8.iterator();

                  while(var21.hasNext()) {
                     CacheKey var22 = (CacheKey)var21.next();
                     var4 += (long)var22.getCallback().passivateModifiedBean(var1, var22.getPrimaryKey(), var17);
                     if (debugLogger.isDebugEnabled()) {
                        debug("passivate target is " + var2 + ", and we have freed = " + var4 + ", after flushed bean cm.passivateModifiedBean on pk " + var22.getPrimaryKey());
                     }

                     if (var4 >= var2) {
                        break;
                     }
                  }
               }

               return var4;
            }

            var6 = (CachingManager)var9.next();
            var10 = var6.getTxManager().getEnrolledKeys(var1);
         } while(var10 == null);

         Iterator var11 = var10.iterator();

         while(var11.hasNext()) {
            Object var12 = var11.next();
            if (!var6.isFlushPending(var1, var12)) {
               if (var6.beanIsOpsComplete(var1, var12)) {
                  var4 += (long)var6.passivateUnModifiedBean(var1, var12);
                  if (debugLogger.isDebugEnabled()) {
                     debug("passivate target is " + var2 + ", and we have freed = " + var4 + ", after opsComplete cm.passivateUnModifiedBean on pk " + var12);
                  }

                  if (var4 >= var2) {
                     return var4;
                  }
               } else {
                  var8.add(new CacheKey(var12, var6));
               }
            }
         }
      }
   }

   private long passivateNotInOurTx(Transaction var1, long var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("\n\n +++ passivateNotInOurTx  entered. \n");
      }

      LinkedList var4 = new LinkedList();
      long var5 = 0L;
      Iterator var7 = this.cache.getCachingManagers().iterator();

      while(var7.hasNext()) {
         CachingManager var8 = (CachingManager)var7.next();
         ArrayList var9 = var8.getTxManager().getNotModifiedOtherTxKeys(var1);
         if (debugLogger.isDebugEnabled()) {
            debug("\n passivateNotInOurTx got a list of " + var9.size() + " not modified, not in our Tx Beans ");
         }

         Iterator var10 = var9.iterator();

         while(var10.hasNext()) {
            TxPk var11 = (TxPk)var10.next();
            Transaction var12 = var11.getTx();
            Object var13 = var11.getPk();
            if (var8.beanIsOpsComplete(var12, var13)) {
               var5 += (long)var8.passivateUnModifiedBean(var12, var13);
               if (debugLogger.isDebugEnabled()) {
                  debug("passivate target is " + var2 + ", and we have freed = " + var5 + ", after opsComplete, not in our Tx, cm.passivateUnModifiedBean on pk " + var13);
               }

               if (var5 >= var2) {
                  return var5;
               }
            } else {
               var4.add(new TxKey(var12, new CacheKey(var13, var8)));
            }
         }
      }

      if (var4.size() > 0) {
         var7 = var4.iterator();

         while(var7.hasNext()) {
            TxKey var14 = (TxKey)var7.next();
            var5 += (long)var14.getKey().getCallback().passivateUnModifiedBean(var14.getTx(), var14.getKey().getPrimaryKey());
            if (debugLogger.isDebugEnabled()) {
               debug("passivate target is " + var2 + ", and we have freed = " + var5 + ", after non-opsComplete, not in our Tx, cm.passivateUnModifiedBean on pk " + var14.getKey().getPrimaryKey());
            }

            if (var5 >= var2) {
               return var5;
            }
         }
      }

      return var5;
   }

   private void setInTxManagers(Transaction var1, List var2) {
      if (var1 != null) {
         Iterator var3 = this.cache.getCachingManagers().iterator();

         while(var3.hasNext()) {
            CachingManager var4 = (CachingManager)var3.next();
            if (var4.hasBeansEnrolledInTx(var1)) {
               if (debugLogger.isDebugEnabled()) {
                  debug(" adding CachingManager in inTxManagerList: " + var4);
               }

               var2.add(var4);
            }
         }

      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityPassivator] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cachingLogger;
   }
}
