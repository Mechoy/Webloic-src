package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.backend.BEDurableSubscriptionStore;
import weblogic.jms.backend.BETopicImpl;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.common.JMSVariableBinder;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.SendOptions;
import weblogic.messaging.kernel.internal.MessageElementImpl;
import weblogic.messaging.kernel.internal.MessageHandle;
import weblogic.messaging.kernel.internal.MessageReference;
import weblogic.messaging.kernel.internal.MultiMessageHandle;
import weblogic.messaging.kernel.internal.MultiMessageReference;
import weblogic.messaging.kernel.internal.MultiPersistenceHandle;
import weblogic.messaging.kernel.internal.PersistenceImpl;
import weblogic.messaging.kernel.internal.QueueImpl;
import weblogic.messaging.kernel.internal.QueueMessageReference;
import weblogic.messaging.kernel.internal.TopicImpl;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.gxa.internal.GXAObjectHandler;
import weblogic.store.gxa.internal.GXATwoPhaseRecord;
import weblogic.store.gxa.internal.GXidImpl;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.utils.expressions.Expression;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.expressions.ExpressionParser;
import weblogic.utils.expressions.ExpressionParserException;

public class JMSStoreUpgradeProcessor {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean debug = false;
   private static final boolean debugVerbose = false;
   private StoreReader reader;
   private PersistentStoreXA store;
   private PersistenceImpl kernelPersistence;
   private BEDurableSubscriptionStore beSubscriptionStore;
   private UpgradeIOBypass ioBypass;
   private StoreUpgradePlugIn plugin;
   public static final int STORE_STATE_NONE = 0;
   public static final int STORE_STATE_PREADD = 1;
   public static final int STORE_STATE_REDELIVERED = 2;
   private static final int SEND_PACK_SIZE = 32;
   private String serverName;
   private String xaResourceName;
   private Map subscribersBySlot = new HashMap();
   private DifferentiatedHashMap dtmisByID = new DifferentiatedHashMap();
   private Map renamedQueues = new HashMap();
   private Map renamedTopics = new HashMap();
   private Map destinationsByName = new HashMap();
   private Map queuesByName = new HashMap();
   private Map topicsByName = new HashMap();
   private Map subscribersByName = new HashMap();
   private Map messageListsByName = new HashMap();
   private Set committedXIDs = new HashSet();
   private DifferentiatedHashMap pendingDeleteRecords = new DifferentiatedHashMap();
   private DifferentiatedHashMap preparedMessages = new DifferentiatedHashMap();
   private boolean hasPreparedMessages;
   private List xaRecords = new ArrayList();
   private ExpressionParser expressionParser = new ExpressionParser();
   private long nextDestinationID = 1L;
   private long nextHandleID = 1L;

   public JMSStoreUpgradeProcessor(StoreUpgradePlugIn var1, String var2, String var3, StoreReader var4, PersistentStoreXA var5, PersistenceImpl var6, BEDurableSubscriptionStore var7, UpgradeIOBypass var8) {
      this.plugin = var1;
      this.serverName = var2;
      this.xaResourceName = var3;
      this.reader = var4;
      this.store = var5;
      this.kernelPersistence = var6;
      this.beSubscriptionStore = var7;
      this.ioBypass = var8;
   }

   boolean has2PCRecords() {
      return this.hasPreparedMessages;
   }

   public void upgrade(int var1) throws JMSException {
      int var2 = var1 / 8;
      int var3 = var1 / 4;

      try {
         this.mapDestinationNames();
         this.discoverDestinations();
         this.plugin.incrementProgress(var2);
         this.createKernelDestinations();
         this.plugin.incrementProgress(var3);
         this.reader.reOpen();
         this.upgradeMessages();
         this.plugin.incrementProgress(var3);
         this.createKernelMessages();
         this.plugin.incrementProgress(var3);
         this.createXARecords();
         this.plugin.incrementProgress(var2);
      } catch (KernelException var5) {
         throw new weblogic.jms.common.JMSException(var5);
      } catch (PersistentStoreException var6) {
         throw new weblogic.jms.common.JMSException(var6);
      }
   }

   private void mapDestinationNames() {
      JMSBean var1 = JMSBeanHelper.getInteropJMSBean(this.plugin.getDomainBean());
      if (var1 != null) {
         QueueBean[] var2 = var1.getQueues();

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            QueueBean var4 = var2[var3];
            if (var4.getJMSCreateDestinationIdentifier() != null && var4.getName().endsWith(this.serverName)) {
               this.renamedQueues.put(var4.getJMSCreateDestinationIdentifier(), var4.getName());
            }
         }

         TopicBean[] var6 = var1.getTopics();

         for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
            TopicBean var5 = var6[var7];
            if (var5.getJMSCreateDestinationIdentifier() != null && var5.getName().endsWith(this.serverName)) {
               this.renamedTopics.put(var5.getJMSCreateDestinationIdentifier(), var5.getName());
            }
         }
      }

   }

   private String getNewQueueName(String var1) {
      String var2 = (String)this.renamedQueues.get(var1);
      return var2 == null ? var1 : var2;
   }

   private String getNewTopicName(String var1) {
      String var2 = (String)this.renamedTopics.get(var1);
      return var2 == null ? var1 : var2;
   }

   private void discoverDestinations() throws JMSException {
      HashSet var1 = new HashSet();
      HashSet var2 = new HashSet();
      int var3 = 0;
      int var4 = 0;

      for(StoreReader.Record var5 = this.reader.recover(); var5 != null; var5 = this.reader.recover()) {
         ++var3;
         Object var6 = var5.getObject();
         UpgradeXAXid var7;
         switch (this.ioBypass.getCode(var6)) {
            case 1:
            case 19:
               ++var4;
               UpgradeConsumer var11 = (UpgradeConsumer)var6;
               UpgradeConsumer var12 = (UpgradeConsumer)this.subscribersBySlot.get(var11.getDurableSlot());
               if (var12 == null || var11.getTimestampId().compareTime(var12.getTimestampId()) > 0) {
                  this.subscribersBySlot.put(var11.getDurableSlot(), var11);
               }
               break;
            case 2:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            default:
               throw new AssertionError("Recovered object from invalid class " + var6.getClass().getName());
            case 3:
               ++var4;
               var7 = (UpgradeXAXid)var6;
               this.committedXIDs.add(var7);
               break;
            case 4:
               ++var4;
               UpgradeXATranEntryReceive var15 = (UpgradeXATranEntryReceive)var6;
               if (!this.committedXIDs.contains(var15.getRecoveredBEXAXid())) {
                  this.preparedMessages.put(var15.getRecoveredMessageId(), var15);
                  this.hasPreparedMessages = true;
               }
               break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
               ++var4;
               MessageImpl var8 = (MessageImpl)var6;
               DestinationImpl var9 = (DestinationImpl)((DestinationImpl)var8.getJMSDestination());
               var1.add(new DestinationKey(var9.getName(), var9.getGeneration()));
               if (var8.getBEXAXid() != null && var8.getBEXAXid() instanceof UpgradeMoveMessagePendingDeleteRecord && (var5.getState() & 1) == 0) {
                  this.pendingDeleteRecords.put(var8.getId(), var8.getBEXAXid());
               } else if ((var5.getState() & 1) != 0) {
                  var7 = (UpgradeXAXid)var8.getBEXAXid();
                  if (!this.committedXIDs.contains(var7)) {
                     this.preparedMessages.put(var8.getId(), var7);
                     this.hasPreparedMessages = true;
                  }
               }
               break;
            case 17:
               ++var4;
               UpgradeDurableTopicMessageInfo var13 = (UpgradeDurableTopicMessageInfo)var6;
               UpgradeDurableTopicMessageInfo var14 = (UpgradeDurableTopicMessageInfo)this.dtmisByID.get(var13.getMessageId());
               if (var14 == null || var13.getGeneration() > var14.getGeneration()) {
                  this.dtmisByID.put(var13.getMessageId(), var13);
               }
               break;
            case 20:
               ++var4;
               UpgradeMoveMessagePendingDeleteRecord var16 = (UpgradeMoveMessagePendingDeleteRecord)var6;
               this.pendingDeleteRecords.put(var16.getMessageId(), var16);
               break;
            case 21:
               ++var4;
               UpgradeDestinationDeleteRecord var10 = (UpgradeDestinationDeleteRecord)var6;
               var2.add(new DestinationKey(var10.getDestinationName(), var10.getCreationTime()));
         }
      }

      Iterator var17 = var2.iterator();

      while(var17.hasNext()) {
         var1.remove(var17.next());
      }

      var17 = var1.iterator();

      while(true) {
         DestinationKey var18;
         DestinationRec var19;
         do {
            if (!var17.hasNext()) {
               return;
            }

            var18 = (DestinationKey)var17.next();
            var19 = (DestinationRec)this.destinationsByName.get(var18.getName());
         } while(var19 != null && var18.getGeneration() <= var19.getGeneration());

         this.destinationsByName.put(var18.getName(), new DestinationRec(var18.getGeneration()));
      }
   }

   private void createKernelDestinations() throws JMSException, KernelException {
      HashSet var1 = new HashSet(this.destinationsByName.keySet());
      Iterator var2 = this.subscribersBySlot.values().iterator();

      while(var2.hasNext()) {
         UpgradeConsumer var3 = (UpgradeConsumer)var2.next();
         var3.parseName();
         String var4;
         if (var3 instanceof UpgradeDistConsumer) {
            var4 = var3.getSubscriptionName();
         } else {
            var4 = BETopicImpl.getSubscriptionQueueName((JMSID)null, var3.getClientId(), var3.getSubscriptionName());
         }

         var3.setQueueName(var4);
         this.createKernelQueue(var4);
         DestinationImpl var5 = var3.getStoredTopic();
         var1.remove(var5.getName());
         if (!this.topicsByName.containsKey(var5.getName())) {
            this.createKernelTopic(var5.getName());
         }

         Object var6 = (List)this.subscribersByName.get(var5.getName());
         if (var6 == null) {
            var6 = new ArrayList();
            this.subscribersByName.put(var5.getName(), var6);
         }

         ((List)var6).add(new SubscriberRec(var3, this.expressionParser));
         if (!(var3 instanceof UpgradeDistConsumer)) {
            JMSSQLExpression var7 = new JMSSQLExpression(var3.getSelector(), var3.getNoLocal(), (JMSID)null, false);
            this.beSubscriptionStore.createSubscription(this.getNewTopicName(var5.getName()), var3.getClientId(), var3.getSubscriptionName(), var7);
         }
      }

      var2 = var1.iterator();

      while(var2.hasNext()) {
         this.createKernelQueue((String)var2.next());
      }

   }

   private void createKernelQueue(String var1) throws KernelException {
      QueueImpl var2 = new QueueImpl(this.getNewQueueName(var1));
      var2.setSerialNumber((long)(this.nextDestinationID++));
      this.queuesByName.put(var1, var2);
      this.kernelPersistence.createDestination(var2);
      DestinationRec var3 = (DestinationRec)this.destinationsByName.get(var1);
      if (var3 == null) {
         var3 = new DestinationRec(0L, var2);
         this.destinationsByName.put(var1, var3);
      } else {
         var3.setDestination(var2);
      }

   }

   private void createKernelTopic(String var1) throws KernelException {
      TopicImpl var2 = new TopicImpl(this.getNewTopicName(var1));
      var2.setSerialNumber((long)(this.nextDestinationID++));
      this.topicsByName.put(var1, var2);
      this.kernelPersistence.createDestination(var2);
      DestinationRec var3 = (DestinationRec)this.destinationsByName.get(var1);
      if (var3 == null) {
         var3 = new DestinationRec(0L, var2);
         this.destinationsByName.put(var1, var3);
      } else {
         var3.setDestination(var2);
      }

   }

   private void upgradeMessages() throws JMSException, PersistentStoreException {
      JMSBean var1 = JMSBeanHelper.getInteropJMSBean(this.plugin.getDomainBean());
      HashMap var2 = new HashMap();
      if (var1 != null) {
         DistributedQueueBean[] var3 = var1.getDistributedQueues();
         DistributedTopicBean[] var4 = var1.getDistributedTopics();

         int var5;
         DistributedDestinationMemberBean[] var6;
         int var7;
         for(var5 = 0; var5 < var3.length; ++var5) {
            var6 = var3[var5].getDistributedQueueMembers();

            for(var7 = 0; var7 < var6.length; ++var7) {
               var2.put(var6[var7].getPhysicalDestinationName(), var3[var5].getName());
            }
         }

         for(var5 = 0; var5 < var4.length; ++var5) {
            var6 = var4[var5].getDistributedTopicMembers();

            for(var7 = 0; var7 < var6.length; ++var7) {
               var2.put(var6[var7].getPhysicalDestinationName(), var4[var5].getName());
            }
         }
      }

      int var12 = 0;
      int var13 = 0;
      StoreReader.Record var14 = this.reader.recover();

      while(var14 != null) {
         ++var12;
         Object var15 = var14.getObject();
         switch (this.ioBypass.getCode(var15)) {
            case 2:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            default:
               throw new AssertionError("Recovered object from invalid class " + var15.getClass().getName());
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
               ++var13;
               MessageImpl var16 = (MessageImpl)var15;
               DestinationImpl var8 = (DestinationImpl)var16.getJMSDestination();
               DestinationRec var9 = (DestinationRec)this.destinationsByName.get(var8.getName());
               var16.setOldMessage(true);
               DestinationImpl var10 = (DestinationImpl)var16.getJMSReplyTo();
               if (var10 != null) {
                  String var11 = (String)var2.get(var10.getName());
                  if (var11 != null) {
                     var16.setJMSReplyTo(new DestinationImpl(var10.getType(), var10.getServerName(), var11, var10.getApplicationName(), var10.getModuleName(), var10.getBackEndId(), var10.getDestinationId()));
                  }
               }

               if (var9 != null && var8.getGeneration() == var9.getGeneration()) {
                  MessageHandle var17;
                  if (var9.getDestination() instanceof QueueImpl) {
                     var17 = this.createQueueMessage(var14, var16, var8, var9);
                  } else {
                     var17 = this.createTopicMessage(var16, var8);
                  }

                  var17.setPagedOut();
               }
            case 1:
            case 3:
            case 4:
            case 17:
            case 19:
            case 20:
            case 21:
               var14 = this.reader.recover();
         }
      }

   }

   private SendOptions createSendOptions(MessageImpl var1) {
      SendOptions var2 = new SendOptions();
      var2.setPersistent(true);
      var2.setDeliveryTime(var1.getDeliveryTime());
      var2.setExpirationTime(var1.getExpirationTime());
      var2.setRedeliveryLimit(var1.getRedeliveryLimit());
      return var2;
   }

   private MessageHandle createQueueMessage(StoreReader.Record var1, MessageImpl var2, DestinationImpl var3, DestinationRec var4) throws PersistentStoreException {
      MessageHandle var5 = new MessageHandle((long)(this.nextHandleID++), var2, this.createSendOptions(var2));
      UpgradeMoveMessagePendingDeleteRecord var6 = (UpgradeMoveMessagePendingDeleteRecord)this.pendingDeleteRecords.get(var2.getId());
      if (var6 != null && var6.isQueueMessage()) {
         return var5;
      } else {
         QueueImpl var7 = (QueueImpl)var4.getDestination();
         QueueMessageReference var8 = new QueueMessageReference(var7, var5);
         if ((var1.getState() & 2) != 0) {
            var8.setDeliveryCount(1);
         }

         Object var9 = this.preparedMessages.get(var2.getId());
         if (var9 != null) {
            if (var9 instanceof UpgradeXAXid) {
               UpgradeXAXid var10 = (UpgradeXAXid)var9;
               if (!this.committedXIDs.contains(var10)) {
                  this.xaRecords.add(new PreparedTransactionRec(var8, var7, var10, 1));
               }
            } else {
               if (!(var9 instanceof UpgradeXATranEntryReceive)) {
                  throw new AssertionError();
               }

               UpgradeXATranEntryReceive var11 = (UpgradeXATranEntryReceive)var9;
               if (!this.committedXIDs.contains(var11.getRecoveredBEXAXid())) {
                  this.xaRecords.add(new PreparedTransactionRec(var8, var7, var11.getRecoveredBEXAXid(), 2));
               }
            }
         }

         this.addMessageToList(var3.getName(), var8, var2);
         var2.setBEXAXid((Externalizable)null);
         this.persistMessageBody(var5);
         return var5;
      }
   }

   private MessageHandle createTopicMessage(MessageImpl var1, DestinationImpl var2) throws PersistentStoreException {
      MultiMessageHandle var3 = new MultiMessageHandle((long)(this.nextHandleID++), var1, this.createSendOptions(var1));
      UpgradeMoveMessagePendingDeleteRecord var4 = (UpgradeMoveMessagePendingDeleteRecord)this.pendingDeleteRecords.get(var1.getId());
      if (var4 != null && var4.appliesToAllConsumers()) {
         return var3;
      } else {
         UpgradeXAXid var5 = null;
         Object var6 = this.preparedMessages.get(var1.getId());
         if (var6 != null && var6 instanceof UpgradeXAXid && !this.committedXIDs.contains(var6)) {
            var5 = (UpgradeXAXid)var6;
         }

         MultiPersistenceHandle var7 = null;
         int var8 = 0;
         boolean var9 = false;
         UpgradeDurableTopicMessageInfo var10 = (UpgradeDurableTopicMessageInfo)this.dtmisByID.get(var1.getId());
         if (var10 == null) {
            List var11 = (List)this.subscribersByName.get(var2.getName());
            if (var11 != null) {
               Iterator var12 = var11.iterator();

               label91:
               while(true) {
                  SubscriberRec var13;
                  do {
                     if (!var12.hasNext()) {
                        break label91;
                     }

                     var13 = (SubscriberRec)var12.next();
                  } while(!var13.match(var1));

                  QueueImpl var14 = (QueueImpl)this.queuesByName.get(var13.getConsumer().getQueueName());
                  var9 = true;
                  if (var7 == null || var8 >= 32) {
                     var7 = new MultiPersistenceHandle(var3);
                  }

                  MultiMessageReference var15 = new MultiMessageReference(var14, var3, var7);
                  ++var8;
                  this.addMessageToList(var14.getName(), var15, var1);
                  if (var5 != null) {
                     this.xaRecords.add(new PreparedTransactionRec(var15, var14, var5, 6));
                  }
               }
            }
         } else {
            int var16 = -1;

            label77:
            while(true) {
               UpgradeConsumer var17;
               do {
                  do {
                     do {
                        if ((var16 = var10.getNextOccupiedSlot(var16)) < 0) {
                           break label77;
                        }

                        var17 = (UpgradeConsumer)this.subscribersBySlot.get(new Integer(var16));
                     } while(var17 == null);
                  } while(var10.isOlderThanConsumer(var17));
               } while(var4 != null && var4.getSlot() == var16);

               QueueImpl var18 = (QueueImpl)this.queuesByName.get(var17.getQueueName());
               var9 = true;
               if (var7 == null || var8 >= 32) {
                  var7 = new MultiPersistenceHandle(var3);
               }

               MultiMessageReference var19 = new MultiMessageReference(var18, var3, var7);
               ++var8;
               if (var10.isRedelivered(var17)) {
                  var19.setDeliveryCount(1);
               }

               this.addMessageToList(var18.getName(), var19, var1);
               if (var5 != null) {
                  this.xaRecords.add(new PreparedTransactionRec(var19, var18, var5, 6));
               } else if (var10.getReceiveTran(var17) != null) {
                  UpgradeXAXid var20 = var10.getReceiveTran(var17);
                  if (!this.committedXIDs.contains(var20)) {
                     this.xaRecords.add(new PreparedTransactionRec(var19, var18, var20, 2));
                  }
               }
            }
         }

         if (var9) {
            var1.setBEXAXid((Externalizable)null);
            this.persistMessageBody(var3);
         }

         return var3;
      }
   }

   private void addMessageToList(String var1, MessageReference var2, MessageImpl var3) {
      Object var4 = (List)this.messageListsByName.get(var1);
      if (var4 == null) {
         var4 = new ArrayList();
         this.messageListsByName.put(var1, var4);
      }

      ((List)var4).add(new MessageElementRec(var2, var3.getId()));
   }

   private void persistMessageBody(MessageHandle var1) throws PersistentStoreException {
      PersistentStoreTransaction var2 = this.kernelPersistence.startStoreTransaction();
      this.kernelPersistence.createMessageBody(var2, var1);
      var2.commit();
   }

   private void persistMessageElement(MessageReference var1) throws PersistentStoreException {
      PersistentStoreTransaction var2 = this.kernelPersistence.startStoreTransaction();
      if (var1 instanceof QueueMessageReference) {
         QueueMessageReference var3 = (QueueMessageReference)var1;
         this.kernelPersistence.createQueueMessageReference(var2, var3);
      } else {
         if (!(var1 instanceof MultiMessageReference)) {
            throw new AssertionError();
         }

         MultiMessageReference var5 = (MultiMessageReference)var1;
         MultiPersistenceHandle var4 = var5.getPersistenceHandle();
         if (var4.getPersistentHandle() == null) {
            this.kernelPersistence.createMultiMessageReference(var2, var4);
         }
      }

      var2.commit();
   }

   private void createKernelMessages() throws PersistentStoreException {
      Iterator var1 = this.messageListsByName.entrySet().iterator();
      ArrayList var2 = new ArrayList();

      while(true) {
         List var4;
         do {
            if (!var1.hasNext()) {
               var1 = var2.iterator();

               while(var1.hasNext()) {
                  MessageElementRec[] var8 = (MessageElementRec[])((MessageElementRec[])var1.next());

                  for(int var9 = 0; var9 < var8.length; ++var9) {
                     MessageReference var10 = var8[var9].getElement();
                     this.persistMessageElement(var10);
                  }
               }

               return;
            }

            Map.Entry var3 = (Map.Entry)var1.next();
            var4 = (List)var3.getValue();
         } while(var4.isEmpty());

         MessageElementRec[] var5 = new MessageElementRec[var4.size()];
         var5 = (MessageElementRec[])((MessageElementRec[])var4.toArray(var5));
         Arrays.sort(var5);

         for(int var6 = 0; var6 < var5.length; ++var6) {
            MessageReference var7 = var5[var6].getElement();
            var7.setSequenceNumber((long)var6);
         }

         var2.add(var5);
      }
   }

   private void createXARecords() throws PersistentStoreException {
      if (!this.xaRecords.isEmpty()) {
         String var1 = SubjectUtils.getUsername(SecurityServiceManager.getCurrentSubject(KERNEL_ID).getSubject());
         HashSet var2 = new HashSet();
         Iterator var3 = this.xaRecords.iterator();

         while(var3.hasNext()) {
            PreparedTransactionRec var4 = (PreparedTransactionRec)var3.next();
            var2.add(var4.getXid());
            PersistentStoreTransaction var5 = this.kernelPersistence.startStoreTransaction();
            this.kernelPersistence.create2PCRecord(var5, var4.getType(), new GXidImpl(var4.getXid()), var4.getQueue(), var4.getElement(), var1);
            var5.commit();
         }

         PersistentStoreConnection var9 = this.store.createConnection(this.xaResourceName, new GXAObjectHandler());
         Iterator var10 = var2.iterator();

         while(var10.hasNext()) {
            UpgradeXAXid var6 = (UpgradeXAXid)var10.next();
            GXATwoPhaseRecord var7 = new GXATwoPhaseRecord(new GXidImpl(var6), false);
            PersistentStoreTransaction var8 = this.kernelPersistence.startStoreTransaction();
            var9.create(var8, var7, 0);
            var8.commit();
         }

      }
   }

   private static final class SubscriberRec {
      private UpgradeConsumer consumer;
      private Expression expression;

      SubscriberRec(UpgradeConsumer var1, ExpressionParser var2) throws JMSException {
         this.consumer = var1;
         if (var1.getSelector() != null) {
            try {
               this.expression = var2.parse(var1.getSelector(), JMSVariableBinder.THE_ONE);
            } catch (ExpressionParserException var4) {
               throw new weblogic.jms.common.JMSException("Error parsing selector \"" + var1.getSelector() + "\"", var4);
            }
         }

      }

      UpgradeConsumer getConsumer() {
         return this.consumer;
      }

      boolean match(MessageImpl var1) {
         if (var1.getId().compareTime(this.consumer.getTimestampId()) < 0) {
            return false;
         } else if (this.consumer instanceof UpgradeDistConsumer && var1.getDDForwarded()) {
            return false;
         } else if (this.expression == null) {
            return true;
         } else {
            try {
               return this.expression.evaluate(new MessageElementImpl(var1));
            } catch (ExpressionEvaluationException var3) {
               return false;
            }
         }
      }
   }

   private static final class PreparedTransactionRec {
      private MessageReference element;
      private QueueImpl queue;
      private UpgradeXAXid xid;
      private int type;

      PreparedTransactionRec(MessageReference var1, QueueImpl var2, UpgradeXAXid var3, int var4) {
         this.element = var1;
         this.queue = var2;
         this.xid = var3;
         this.type = var4;
      }

      MessageReference getElement() {
         return this.element;
      }

      QueueImpl getQueue() {
         return this.queue;
      }

      UpgradeXAXid getXid() {
         return this.xid;
      }

      int getType() {
         return this.type;
      }
   }

   private static final class MessageElementRec implements Comparable {
      private MessageReference element;
      private JMSMessageId id;

      MessageElementRec(MessageReference var1, JMSMessageId var2) {
         this.element = var1;
         this.id = var2;
      }

      MessageReference getElement() {
         return this.element;
      }

      JMSMessageId getId() {
         return this.id;
      }

      public boolean equals(Object var1) {
         MessageElementRec var2 = (MessageElementRec)var1;
         return this.id.equals(var2.id);
      }

      public int hashCode() {
         return this.id.hashCode();
      }

      public int compareTo(Object var1) {
         MessageElementRec var2 = (MessageElementRec)var1;
         return this.id.compare(var2.id);
      }
   }

   private static final class DestinationKey {
      private String name;
      private long generation;

      DestinationKey(String var1, long var2) {
         this.name = var1;
         this.generation = var2;
      }

      String getName() {
         return this.name;
      }

      long getGeneration() {
         return this.generation;
      }

      public boolean equals(Object var1) {
         try {
            DestinationKey var2 = (DestinationKey)var1;
            return this.name.equals(var2.name) && this.generation == var2.generation;
         } catch (ClassCastException var3) {
            return false;
         }
      }

      public int hashCode() {
         return this.name.hashCode() + (int)this.generation;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("[ name=");
         var1.append(this.name);
         var1.append(" generation=");
         var1.append(this.generation);
         var1.append(" ]");
         return var1.toString();
      }
   }

   private static final class DestinationRec {
      private long generation;
      private weblogic.messaging.kernel.internal.DestinationImpl dest;

      DestinationRec(long var1) {
         this.generation = var1;
      }

      DestinationRec(long var1, weblogic.messaging.kernel.internal.DestinationImpl var3) {
         this.generation = var1;
         this.dest = var3;
      }

      long getGeneration() {
         return this.generation;
      }

      void setDestination(weblogic.messaging.kernel.internal.DestinationImpl var1) {
         this.dest = var1;
      }

      weblogic.messaging.kernel.internal.DestinationImpl getDestination() {
         return this.dest;
      }
   }

   private static class DifferentiatedHashMap {
      private HashMap map;

      private DifferentiatedHashMap() {
         this.map = new HashMap();
      }

      Object get(JMSMessageId var1) {
         return this.map.get(new DifferentiatedJMSMessageId(var1));
      }

      void put(JMSMessageId var1, Object var2) {
         this.map.put(new DifferentiatedJMSMessageId(var1), var2);
      }

      Object remove(JMSMessageId var1) {
         return this.map.remove(new DifferentiatedJMSMessageId(var1));
      }

      Collection values() {
         return this.map.values();
      }

      // $FF: synthetic method
      DifferentiatedHashMap(Object var1) {
         this();
      }
   }

   private static class DifferentiatedJMSMessageId {
      private JMSMessageId mid;

      DifferentiatedJMSMessageId(JMSMessageId var1) {
         this.mid = var1;
      }

      public boolean equals(Object var1) {
         DifferentiatedJMSMessageId var2 = (DifferentiatedJMSMessageId)var1;
         return this.mid.differentiatedEquals(var2.mid);
      }

      public int hashCode() {
         return this.mid.differentiatedHashCode();
      }
   }
}
