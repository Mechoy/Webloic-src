package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.xa.PersistentStoreXA;

public class BEDurableSubscriptionStore {
   private static final String CONN_PREFIX = "weblogic.messaging.";
   private static final String CONN_SUFFIX = ".durablesubs";
   private PersistentStoreXA store;
   private PersistentStoreConnection storeConnection;
   private Map topicMap;

   public BEDurableSubscriptionStore(String var1, PersistentStoreXA var2) throws JMSException {
      this.store = var2;
      this.topicMap = new HashMap();

      try {
         this.storeConnection = var2.createConnection("weblogic.messaging." + var1 + ".durablesubs");
      } catch (PersistentStoreException var4) {
         throw new weblogic.jms.common.JMSException("Error opening persistent store for durable subscriptions", var4);
      }
   }

   public void close() {
      this.storeConnection.close();
   }

   public PersistentHandle createSubscription(String var1, String var2, String var3, JMSSQLExpression var4) throws JMSException {
      return this.createSubscription(var1, var2, 0, var3, var4);
   }

   public PersistentHandle createSubscription(String var1, String var2, int var3, String var4, JMSSQLExpression var5) throws JMSException {
      SubscriptionRecord var6 = new SubscriptionRecord(var1, var2, var3, var4, var5);
      PersistentStoreTransaction var7 = this.store.begin();
      PersistentHandle var8 = this.storeConnection.create(var7, var6, 0);

      try {
         var7.commit();
      } catch (PersistentStoreException var10) {
         throw new weblogic.jms.common.JMSException("Error persisting a durable subscriber record", var10);
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Persisted a record for durable subscription " + var6);
      }

      return var8;
   }

   void deleteSubscription(PersistentHandle var1) throws JMSException {
      PersistentStoreTransaction var2 = this.store.begin();
      this.storeConnection.delete(var2, var1, 0);

      try {
         var2.commit();
      } catch (PersistentStoreException var4) {
         throw new weblogic.jms.common.JMSException("Error deleting a durable subscriber record", var4);
      }

      JMSDebug.JMSBackEnd.debug("Deleted a persistent durable subscription record");
   }

   void recover() throws JMSException {
      try {
         PersistentStoreRecord var2;
         SubscriptionRecord var3;
         Object var4;
         for(PersistentStoreConnection.Cursor var1 = this.storeConnection.createCursor(0); (var2 = var1.next()) != null; ((List)var4).add(var3)) {
            var3 = (SubscriptionRecord)var2.getData();
            var3.setHandle(var2.getHandle());
            var4 = (List)this.topicMap.get(var3.getDestinationName());
            if (var4 == null) {
               var4 = new ArrayList();
               this.topicMap.put(var3.getDestinationName(), var4);
            }
         }

      } catch (PersistentStoreException var5) {
         throw new weblogic.jms.common.JMSException("Error recovering durable subscriber records", var5);
      }
   }

   synchronized void restoreSubscriptions(BETopicImpl var1) throws JMSException {
      List var2 = (List)this.topicMap.get(var1.getName());
      if (var2 != null) {
         SubscriptionRecord var4;
         for(Iterator var3 = var2.iterator(); var3.hasNext(); var1.recoverDurableSubscription(var4.getHandle(), var4.getClientId(), var4.getClientIdPolicy(), var4.getSubscriptionName(), var4.getExpression())) {
            var4 = (SubscriptionRecord)var3.next();
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Restoring durable subscription " + var4);
            }
         }

         this.topicMap.remove(var1.getName());
      }

   }

   synchronized void deleteOrphanedSubscriptions() throws JMSException {
      JMSException var1 = null;
      Iterator var2 = this.topicMap.values().iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            SubscriptionRecord var5 = (SubscriptionRecord)var4.next();

            try {
               this.deleteSubscription(var5.getHandle());
            } catch (JMSException var7) {
               var1 = var7;
            }
         }
      }

      this.topicMap.clear();
      if (var1 != null) {
         throw var1;
      }
   }

   public static final class SubscriptionRecord implements Externalizable {
      public static final long serialVersionUID = 5570891599555644794L;
      private static final int EXTERNAL_VERSION1 = 1;
      private static final int EXTERNAL_VERSION2 = 2;
      private static final int EXTERNAL_VERSION = 2;
      private String destinationName;
      private String clientId;
      private int clientIdPolicy = 0;
      private String subscriptionName;
      private JMSSQLExpression selector;
      private transient PersistentHandle handle;

      SubscriptionRecord(String var1, String var2, int var3, String var4, JMSSQLExpression var5) {
         this.destinationName = var1;
         this.clientId = var2;
         this.clientIdPolicy = var3;
         this.subscriptionName = var4;
         this.selector = var5;
      }

      public SubscriptionRecord() {
      }

      String getDestinationName() {
         return this.destinationName;
      }

      String getClientId() {
         return this.clientId;
      }

      int getClientIdPolicy() {
         return this.clientIdPolicy;
      }

      String getSubscriptionName() {
         return this.subscriptionName;
      }

      JMSSQLExpression getExpression() {
         return this.selector;
      }

      PersistentHandle getHandle() {
         return this.handle;
      }

      void setHandle(PersistentHandle var1) {
         this.handle = var1;
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeInt(2);
         var1.writeUTF(this.destinationName);
         var1.writeUTF(this.clientId);
         var1.writeUTF(this.subscriptionName);
         this.selector.writeExternal(var1);
         var1.writeInt(this.clientIdPolicy);
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         int var2 = var1.readInt();
         if (var2 != 1 && var2 != 2) {
            throw new IOException("External version mismatch");
         } else {
            this.destinationName = var1.readUTF();
            this.clientId = var1.readUTF();
            this.subscriptionName = var1.readUTF();
            this.selector = new JMSSQLExpression();
            this.selector.readExternal(var1);
            if (var2 == 2) {
               this.clientIdPolicy = var1.readInt();
            }

         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("[ name = ");
         var1.append(this.subscriptionName);
         var1.append(" topic = ");
         var1.append(this.destinationName);
         var1.append(" client ID = ");
         var1.append(this.clientId);
         if (this.selector != null) {
            var1.append(" selector = ");
            var1.append(this.selector);
         }

         var1.append(" ]");
         return var1.toString();
      }
   }
}
