package weblogic.jms.common;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import weblogic.jms.dd.DDConstants;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDMember;
import weblogic.jms.frontend.FESession;
import weblogic.rmi.extensions.RemoteHelper;

public final class JMSLoadBalancer implements DDConstants {
   private int size;
   private boolean doInit = true;
   private boolean isTopic;
   private boolean isPartitionedDistributedTopic;
   private LoadBalancer durableSubscriberLB;
   private LoadBalancer connectionConsumerLB;
   private LoadBalancer consumerLB;
   private LoadBalancer npProducerLB;
   private LoadBalancer pProducerLB;
   private LoadBalancer consumerAllLB;
   private LoadBalancer npProducerAllLB;
   private LoadBalancer pProducerAllLB;
   private DDHandler ddHandler;

   public JMSLoadBalancer(DDHandler var1) {
      this.ddHandler = var1;
      this.isTopic = !var1.isQueue();
      this.isPartitionedDistributedTopic = var1.getForwardingPolicy() == 0;
      this.refresh();
   }

   public void refresh() {
      synchronized(this.ddHandler) {
         if (this.isTopic && !this.isPartitionedDistributedTopic) {
            this.refreshTopic();
         } else {
            this.refreshQueue();
         }

      }
   }

   private void makeOrder(DistributedDestinationImpl[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].setOrder(var2);
      }

   }

   private DistributedDestinationImpl[] getUpMembers() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.ddHandler.memberCloneIterator();

      while(var2.hasNext()) {
         DDMember var3 = (DDMember)var2.next();
         if (var3.isUp() && var3.getDDImpl() != null) {
            var1.add(var3.getDDImpl());
         }

         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug(this.ddHandler.getName() + " getUpMembers() " + var3 + " is up? " + var3.isUp() + " DDImpl " + var3.getDDImpl());
            if (var3.getDDImpl() == null) {
               JMSDebug.JMSCommon.debug(this.ddHandler.getName() + " getUpMembers() UNEXPECTED... getDDImpl is null for member " + var3);
            }
         }
      }

      return (DistributedDestinationImpl[])((DistributedDestinationImpl[])var1.toArray(new DistributedDestinationImpl[0]));
   }

   private void consumptionPauseFilter(DistributedDestinationImpl[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < this.size && var1[var3] != null; ++var3) {
         if (!var1[var3].isConsumptionPaused()) {
            var1[var2++] = var1[var3];
         }
      }

      if (var2 != 0 && var2 < this.size) {
         var1[var2] = null;
      }

   }

   private void productionPauseFilter(DistributedDestinationImpl[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < this.size && var1[var3] != null; ++var3) {
         if (!var1[var3].isProductionPaused() && !var1[var3].isInsertionPaused()) {
            var1[var2++] = var1[var3];
         }
      }

      if (var2 != 0 && var2 < this.size) {
         var1[var2] = null;
      }

   }

   private void refreshQueue() {
      DistributedDestinationImpl[] var1 = this.getUpMembers();
      this.makeOrder(var1);
      this.size = var1.length;
      if (this.size > 0) {
         Arrays.sort(var1, new JMSComparator(2, false));
         int var25 = 0;
         int var24 = 0;
         int var23 = 0;
         int var22 = 0;
         int var21 = 0;
         int var20 = 0;
         int var19 = 0;
         int var18 = 0;
         int var17 = 0;
         int var16 = 0;
         DistributedDestinationImpl[] var7 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var3 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var8 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var9 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var10 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var4 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var5 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var6 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var11 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var12 = new DistributedDestinationImpl[this.size];
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug(this.ddHandler.getName() + " with load balancing policy " + this.loadBalanceString(this.ddHandler.getLoadBalancingPolicyAsInt()) + " has the following members: ");
         }

         int var26 = 0;
         int var27 = 0;

         for(int var28 = 0; var28 < this.size; ++var28) {
            DistributedDestinationImpl var2 = var1[var28];
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug(" " + var2.debugString());
            }

            if (var2.isProductionPaused() || var2.isInsertionPaused()) {
               ++var26;
            }

            if (var2.isConsumptionPaused()) {
               ++var27;
            }

            var12[var25++] = var2;
            if (var2.isLocal()) {
               var11[var24++] = var2;
               if (var2.isPersistent()) {
                  var10[var20++] = var2;
                  if (var2.hasConsumer()) {
                     var7[var16++] = var2;
                  }
               }

               if (var2.hasConsumer()) {
                  var8[var18++] = var2;
               } else {
                  var9[var19++] = var2;
               }
            }

            if (var2.isPersistent()) {
               var4[var21++] = var2;
               if (var2.hasConsumer()) {
                  var3[var17++] = var2;
               }
            }

            if (var2.hasConsumer()) {
               var5[var22++] = var2;
            } else {
               var6[var23++] = var2;
            }
         }

         if (var24 < 1) {
            var11 = var12;
         }

         DistributedDestinationImpl[] var14 = var5;
         if (var22 < 1) {
            var14 = var12;
            var5 = var11;
         }

         DistributedDestinationImpl[] var15 = var6;
         if (var23 < 1) {
            var15 = var12;
         }

         if (var16 < 1) {
            var7 = var3;
            var16 = var17;
         }

         if (var18 < 1) {
            var8 = var5;
         }

         if (var19 < 1) {
            var9 = var11;
         }

         if (var20 < 1) {
            var10 = var4;
            var20 = var21;
         }

         if (var16 == 0) {
            if (var20 == 0) {
               var7 = var8;
            } else {
               var7 = var10;
            }
         }

         if (var17 == 0) {
            if (var21 == 0) {
               var3 = var14;
            } else {
               var3 = var4;
            }
         }

         if (var26 != 0 && var26 != this.size) {
            this.productionPauseFilter(var8);
            this.productionPauseFilter(var14);
            this.productionPauseFilter(var7);
            this.productionPauseFilter(var3);
         }

         if (var27 != 0 && var26 != this.size) {
            this.consumptionPauseFilter(var11);
            this.consumptionPauseFilter(var9);
            this.consumptionPauseFilter(var15);
         }

         if (this.ddHandler.getLoadBalancingPolicyAsInt() == 1) {
            this.connectionConsumerLB = new RandomLoadBalancer(var11);
            this.consumerLB = new RandomLoadBalancer(var9);
            this.consumerAllLB = new RandomLoadBalancer(var15);
            this.npProducerLB = new RandomLoadBalancer(var8);
            this.npProducerAllLB = new RandomLoadBalancer(var14);
            this.pProducerLB = new RandomLoadBalancer(var7);
            this.pProducerAllLB = new RandomLoadBalancer(var3);
         } else if (this.doInit) {
            this.connectionConsumerLB = new RRLoadBalancer(var11);
            this.consumerLB = new RRLoadBalancer(var9);
            this.consumerAllLB = new RRLoadBalancer(var15);
            this.npProducerLB = new RRLoadBalancer(var8);
            this.npProducerAllLB = new RRLoadBalancer(var14);
            this.pProducerLB = new RRLoadBalancer(var7);
            this.pProducerAllLB = new RRLoadBalancer(var3);
         } else {
            this.connectionConsumerLB.refresh(var11);
            this.consumerLB.refresh(var9);
            this.consumerAllLB.refresh(var15);
            this.npProducerLB.refresh(var8);
            this.npProducerAllLB.refresh(var14);
            this.pProducerLB.refresh(var7);
            this.pProducerAllLB.refresh(var3);
         }

         this.doInit = false;
      }
   }

   private void refreshTopic() {
      DistributedDestinationImpl[] var1 = this.getUpMembers();
      this.makeOrder(var1);
      this.size = var1.length;
      if (this.size > 0) {
         Arrays.sort(var1, new JMSComparator(2, false));
         int var12 = 0;
         int var11 = 0;
         int var10 = 0;
         int var9 = 0;
         DistributedDestinationImpl[] var4 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var3 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var5 = new DistributedDestinationImpl[this.size];
         DistributedDestinationImpl[] var6 = new DistributedDestinationImpl[this.size];
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug(this.ddHandler.getName() + " with load balancing policy " + this.loadBalanceString(this.ddHandler.getLoadBalancingPolicyAsInt()) + " has the following members: ");
         }

         int var13 = 0;
         int var14 = 0;

         for(int var15 = 0; var15 < this.size; ++var15) {
            DistributedDestinationImpl var2 = var1[var15];
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug(" " + var2.debugString());
            }

            if (var2.isProductionPaused() || var2.isInsertionPaused()) {
               ++var13;
            }

            if (var2.isConsumptionPaused()) {
               ++var14;
            }

            var6[var12++] = var2;
            if (var2.isLocal()) {
               var5[var11++] = var2;
               if (var2.isPersistent()) {
                  var4[var9++] = var2;
               }
            }

            if (var2.isPersistent()) {
               var3[var10++] = var2;
            }
         }

         DistributedDestinationImpl[] var8 = var5;
         if (var11 < 1) {
            var5 = var6;
         }

         if (var9 < 1) {
            var4 = var3;
            var9 = var10;
         }

         if (var9 == 0) {
            var4 = var5;
         }

         DistributedDestinationImpl[] var16 = (DistributedDestinationImpl[])((DistributedDestinationImpl[])var5.clone());
         if (var13 != 0 && var13 != this.size) {
            this.productionPauseFilter(var16);
            this.productionPauseFilter(var4);
         }

         if (var14 != 0 && var13 != this.size) {
            this.consumptionPauseFilter(var8);
            this.consumptionPauseFilter(var4);
            this.consumptionPauseFilter(var5);
         }

         if (this.ddHandler.getLoadBalancingPolicyAsInt() == 1) {
            this.connectionConsumerLB = new RandomLoadBalancer(var8);
            this.durableSubscriberLB = new RandomLoadBalancer(var4);
            this.consumerLB = new RandomLoadBalancer(var5);
            this.npProducerLB = new RandomLoadBalancer(var16);
            this.pProducerLB = new RandomLoadBalancer(var4);
         } else if (this.doInit) {
            this.connectionConsumerLB = new RRLoadBalancer(var8);
            this.durableSubscriberLB = new RRLoadBalancer(var4);
            this.consumerLB = new RRLoadBalancer(var5);
            this.npProducerLB = new RRLoadBalancer(var16);
            this.pProducerLB = new RRLoadBalancer(var4);
         } else {
            this.connectionConsumerLB.refresh(var8);
            this.durableSubscriberLB.refresh(var4);
            this.consumerLB.refresh(var5);
            this.npProducerLB.refresh(var16);
            this.pProducerLB.refresh(var4);
         }

         this.doInit = false;
      }
   }

   public DestinationImpl durableSubscriberLoadBalance() {
      synchronized(this.ddHandler) {
         return this.size <= 0 ? null : this.durableSubscriberLB.getNext((DDTxLoadBalancingOptimizer)null);
      }
   }

   public DestinationImpl connectionConsumerLoadBalance() {
      synchronized(this.ddHandler) {
         return this.size <= 0 ? null : this.connectionConsumerLB.getNext((DDTxLoadBalancingOptimizer)null);
      }
   }

   public DestinationImpl consumerLoadBalance(FESession var1) {
      synchronized(this.ddHandler) {
         if (this.size <= 0) {
            return null;
         } else {
            DistributedDestinationImpl var3;
            if (var1 != null && var1.isTransacted()) {
               var3 = var1.getCachedDest(this.ddHandler.getName(), false);
               if (var3 != null) {
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("Tx Pick " + var3.debugString());
                  }

                  return var3;
               }

               if (!var1.isServerAffinityEnabled()) {
                  var3 = this.consumerAllLB.getNext(var1);
               } else {
                  var3 = this.consumerLB.getNext(var1);
               }

               var1.addCachedDest(var3);
            } else if (var1 != null && !var1.isServerAffinityEnabled()) {
               var3 = this.consumerAllLB.getNext((DDTxLoadBalancingOptimizer)null);
            } else {
               var3 = this.consumerLB.getNext((DDTxLoadBalancingOptimizer)null);
            }

            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("Returning destination: " + var3.getInstanceName());
            }

            return var3;
         }
      }
   }

   public DestinationImpl producerLoadBalance(boolean var1, FESession var2) {
      synchronized(this.ddHandler) {
         if (this.size <= 0) {
            return null;
         } else {
            DistributedDestinationImpl var4;
            if (var2 != null && var2.isTransacted()) {
               var4 = var2.getCachedDest(this.ddHandler.getName(), var1);
               if (var4 != null) {
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("Tx Pick " + var4.debugString());
                  }

                  return var4;
               } else {
                  var4 = this.getDestination(var1, var2, var2);
                  var2.addCachedDest(var4);
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("Returning destination: " + var4.getInstanceName());
                  }

                  return var4;
               }
            } else {
               var4 = this.getDestination(var1, var2, (FESession)null);
               if (JMSDebug.JMSCommon.isDebugEnabled()) {
                  JMSDebug.JMSCommon.debug("Returning destination: " + (var4 == null ? "null" : var4.getInstanceName()));
               }

               return var4;
            }
         }
      }
   }

   public JMSFailover getProducerFailover(DistributedDestinationImpl var1, Throwable var2, boolean var3, FESession var4) {
      Exception var5 = null;
      if (var2 instanceof JMSException) {
         var5 = ((JMSException)var2).getLinkedException();
      }

      if (var2 instanceof RemoteException && !RemoteHelper.isRecoverablePreInvokeFailure((RemoteException)var2)) {
         return null;
      } else if (var5 instanceof RemoteException && !RemoteHelper.isRecoverablePreInvokeFailure((RemoteException)var5)) {
         return null;
      } else {
         DistributedDestinationImpl[] var6 = this.getUpMembers();
         this.makeOrder(var6);
         if (!this.isTopic) {
            Arrays.sort(var6, new JMSComparator(0, var3));
         } else {
            Arrays.sort(var6, new JMSComparator(1, var3));
         }

         return new JMSFailover(var6, var1);
      }
   }

   private DistributedDestinationImpl getDestination(boolean var1, FESession var2, FESession var3) {
      DistributedDestinationImpl var4;
      if (this.isTopic && !this.isPartitionedDistributedTopic) {
         if (var1) {
            var4 = this.pProducerLB.getNext(var3);
         } else {
            var4 = this.npProducerLB.getNext(var3);
         }
      } else if (var1) {
         if (var2 != null && !var2.isServerAffinityEnabled()) {
            var4 = this.pProducerAllLB.getNext(var3);
         } else {
            var4 = this.pProducerLB.getNext(var3);
         }
      } else if (var2 != null && !var2.isServerAffinityEnabled()) {
         var4 = this.npProducerAllLB.getNext(var3);
      } else {
         var4 = this.npProducerLB.getNext(var3);
      }

      return var4;
   }

   private String loadBalanceString(int var1) {
      switch (var1) {
         case 0:
            return "Round Robin";
         case 1:
            return "Random";
         case 2:
            return "Sticky Random";
         default:
            return "Unknown Policy";
      }
   }

   public boolean isPartitionedDistributedTopic() {
      return this.isPartitionedDistributedTopic;
   }

   public final class JMSComparator implements Comparator {
      public static final int SENDER = 0;
      public static final int PUBLISHER = 1;
      public static final int ORDER = 2;
      final int type;
      final boolean isPersistent;

      public JMSComparator(int var2, boolean var3) {
         this.type = var2;
         this.isPersistent = var3;
      }

      public int compare(Object var1, Object var2) {
         switch (this.type) {
            case 0:
               return this.senderCompare(var1, var2);
            case 1:
               return this.publisherCompare(var1, var2);
            case 2:
               return this.orderCompare(var1, var2);
            default:
               return this.senderCompare(var1, var2);
         }
      }

      public int orderCompare(Object var1, Object var2) {
         try {
            DistributedDestinationImpl var3 = (DistributedDestinationImpl)var1;
            DistributedDestinationImpl var4 = (DistributedDestinationImpl)var2;
            return var3.getOrder() < var4.getOrder() ? -1 : 1;
         } catch (Exception var6) {
            return 0;
         }
      }

      public int senderCompare(Object var1, Object var2) {
         try {
            DistributedDestinationImpl var3 = (DistributedDestinationImpl)var1;
            DistributedDestinationImpl var4 = (DistributedDestinationImpl)var2;
            if (this.isPersistent && var3.isPersistent() != var4.isPersistent()) {
               if (var4.isPersistent()) {
                  return 1;
               }
            } else if (var3.hasConsumer() == var4.hasConsumer()) {
               if (var3.isLocal() == var4.isLocal()) {
                  if (var3.getOrder() < var4.getOrder()) {
                     return -1;
                  }

                  return 1;
               }

               if (var4.isLocal()) {
                  return 1;
               }
            } else if (var4.hasConsumer()) {
               return 1;
            }

            return -1;
         } catch (Exception var6) {
            return 0;
         }
      }

      public int publisherCompare(Object var1, Object var2) {
         try {
            DistributedDestinationImpl var3 = (DistributedDestinationImpl)var1;
            DistributedDestinationImpl var4 = (DistributedDestinationImpl)var2;
            if (this.isPersistent && var3.isPersistent() != var4.isPersistent()) {
               if (var4.isPersistent()) {
                  return 1;
               }
            } else {
               if (var3.isLocal() == var4.isLocal()) {
                  if (var3.getOrder() < var4.getOrder()) {
                     return -1;
                  }

                  return 1;
               }

               if (var4.isLocal()) {
                  return 1;
               }
            }

            return -1;
         } catch (Exception var6) {
            return 0;
         }
      }
   }
}
