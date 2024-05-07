package weblogic.jms.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.naming.NamingException;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.internal.NamingNode;
import weblogic.management.ManagementException;
import weblogic.store.common.PersistentStoreOutputStream;

public final class DurableSubscription extends SingularAggregatable implements Subscription {
   private static final byte EXTVERSION = 1;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   static final long serialVersionUID = 4168599304001594429L;
   private DestinationImpl destinationImpl;
   private String selector;
   private boolean noLocal;
   private Vector dsVector = new Vector();
   private JMSID dsId;
   private transient BEConsumerImpl myConsumer;
   private transient Map<JMSID, BEConsumerImpl> subscribers = new HashMap();
   private transient String jndiName;
   private transient String consumerName;
   private transient int clientIdPolicy;
   private transient int subscriptionSharingPolicy;
   private transient boolean pending = true;
   private transient boolean stale;
   private transient int waits;
   private transient int subscribersTotalCount;
   private transient int subscribersHighCount;
   private static final int _HASDESTIMPL = 1;
   private static final int _HASSELECTOR = 2;
   private static final int _NOLOCAL = 4;
   private static final int _HASID = 8;
   private static final int _HASNAME = 16;

   public synchronized int getSubscribersTotalCount() {
      return this.subscribersTotalCount;
   }

   public synchronized int getSubscribersHighCount() {
      return this.subscribersHighCount;
   }

   public synchronized void resetSubscribersCount() {
      this.subscribersHighCount = 0;
      this.subscribersTotalCount = 0;
   }

   public DurableSubscription(String var1, DestinationImpl var2, String var3, boolean var4, int var5, int var6) {
      this.destinationImpl = var2;
      if (var3 != null && var3.trim().length() > 0) {
         this.selector = var3;
      }

      this.noLocal = var4;
      this.clientIdPolicy = var5;
      this.subscriptionSharingPolicy = var6;
      this.consumerName = var1;

      try {
         this.dsId = JMSService.getService().getNextId();
      } catch (ManagementException var8) {
      }

   }

   public DurableSubscription() {
   }

   public synchronized BEConsumerImpl getConsumer() {
      if (this.myConsumer != null) {
         return this.myConsumer;
      } else {
         Iterator var1 = this.subscribers.values().iterator();
         if (var1.hasNext()) {
            this.myConsumer = (BEConsumerImpl)var1.next();
            return this.myConsumer;
         } else {
            return null;
         }
      }
   }

   public synchronized void addSubscriber(BEConsumerImpl var1) {
      if (this.myConsumer == null || this.subscribers.size() == 0 || this.myConsumer.getJMSID() == null) {
         this.myConsumer = var1;
      }

      this.subscriptionSharingPolicy = var1.getSubscriptionSharingPolicy();
      if (var1.getJMSID() != null) {
         this.subscribers.put(var1.getJMSID(), var1);
      }

      ++this.subscribersTotalCount;
      if (this.getSubscribersCount() > this.subscribersHighCount) {
         this.subscribersHighCount = this.getSubscribersCount();
      }

   }

   public synchronized void removeSubscriber(JMSID var1) {
      BEConsumerImpl var2 = (BEConsumerImpl)this.subscribers.remove(var1);
      if (this.myConsumer == var2) {
         this.myConsumer = null;
         Iterator var3 = this.subscribers.values().iterator();
         if (var3.hasNext()) {
            this.myConsumer = (BEConsumerImpl)var3.next();
         }
      }

      if (this.myConsumer == null && var2 != null) {
         this.myConsumer = var2;
      }

   }

   public synchronized int getSubscribersCount() {
      return this.subscribers.size();
   }

   public boolean isNoLocal() {
      return this.noLocal;
   }

   public String getSelector() {
      return this.selector;
   }

   public DestinationImpl getDestinationImpl() {
      return this.destinationImpl;
   }

   public JMSServerId getBackEndId() {
      return this.destinationImpl.getBackEndId();
   }

   public synchronized int getSubscriptionSharingPolicy() {
      return this.subscriptionSharingPolicy;
   }

   public static boolean noLocalAndSelectorMatch(Subscription var0, boolean var1, String var2) {
      if (var1 != var0.isNoLocal()) {
         return false;
      } else {
         String var3;
         if (var0.getSelector() == null || (var3 = var0.getSelector().trim()).length() == 0 || var3.equals("TRUE")) {
            var3 = null;
         }

         if (var2 == null || (var2 = var2.trim()).length() == 0 || var2.equals("TRUE")) {
            var2 = null;
         }

         if (var3 == null && var2 != null || var3 != null && var2 == null) {
            return false;
         } else {
            return var3 == null || var3.equals(var2);
         }
      }
   }

   public boolean equalsForSerialized(Object var1) {
      if (!(var1 instanceof DurableSubscription)) {
         return false;
      } else {
         DurableSubscription var2 = (DurableSubscription)var1;
         return noLocalAndSelectorMatch(this, var2.noLocal, var2.selector) && (this.destinationImpl != null || var2.destinationImpl == null) && (this.destinationImpl == null || var2.destinationImpl != null) ? Destination.equalsForDS(this.destinationImpl, var2.destinationImpl) : false;
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DurableSubscription)) {
         return false;
      } else {
         DurableSubscription var2 = (DurableSubscription)var1;
         return noLocalAndSelectorMatch(this, var2.noLocal, var2.selector) && (this.destinationImpl != null || var2.destinationImpl == null) && (this.destinationImpl == null || var2.destinationImpl != null) && (this.consumerName == null || this.consumerName.equals(var2.consumerName)) && this.clientIdPolicy == var2.clientIdPolicy ? Destination.equalsForDS(this.destinationImpl, var2.destinationImpl) : false;
      }
   }

   public int hashCode() {
      return this.jndiName.hashCode();
   }

   public void hadConflict(boolean var1) {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("durableSubscription.hadConflict jndiName=" + this.jndiName + " didWin=" + var1);
      }

      if (!var1) {
         if (this.getSubscribersCount() != 0) {
            try {
               this.getConsumer().delete(false, false);
            } catch (JMSException var3) {
            } catch (javax.jms.JMSException var4) {
            }

         }
      }
   }

   public synchronized boolean hasWaits() {
      return this.waits > 0;
   }

   public synchronized void incrementWaits() {
      ++this.waits;
   }

   public synchronized void decrementWaits() {
      --this.waits;
   }

   public synchronized void setPending(boolean var1) {
      this.pending = var1;
   }

   public synchronized boolean isPending() {
      return this.pending;
   }

   public synchronized void setStale(boolean var1) {
      this.stale = var1;
   }

   public synchronized boolean isStale() {
      return this.stale;
   }

   public String toString() {
      return "DurableSubscription((" + super.toString() + ") " + this.destinationImpl + ":" + this.selector + ":" + this.noLocal + ")";
   }

   private synchronized void add(DurableSubscription var1) {
      if (var1 != this) {
         var1.dsVector = this.dsVector;
      }

      this.dsVector.add(var1);
   }

   private synchronized void remove(DurableSubscription var1) {
      for(int var2 = 0; var2 < this.dsVector.size(); ++var2) {
         DurableSubscription var3 = (DurableSubscription)this.dsVector.elementAt(var2);
         if (var3.dsId.equals(var1.dsId)) {
            this.dsVector.remove(var2);
         }
      }

   }

   public synchronized Vector getDSVector() {
      return (Vector)this.dsVector.clone();
   }

   public String getName() {
      return this.jndiName;
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) {
      super.onBind(var1, var2, var3);
      if (this.hasAggregatable()) {
         DurableSubscription var4 = (DurableSubscription)(var3 == null ? this : var3);
         this.add(var4);
         if (var3 == null) {
            DSManager.manager().add(var4);
         }
      }

   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      boolean var4 = super.onUnbind(var1, var2, var3);
      DurableSubscription var5;
      if (var4) {
         var5 = null;
         synchronized(this) {
            var5 = (DurableSubscription)this.dsVector.firstElement();
         }

         if (var5 != null) {
            DSManager.manager().remove(var5);
         }
      }

      var5 = (DurableSubscription)(var3 == null ? this : var3);
      this.remove(var5);
      return var4;
   }

   public void delete() throws javax.jms.JMSException {
      Throwable var1 = null;
      BEConsumerImpl var2;
      Iterator var3;
      synchronized(this) {
         var3 = ((HashMap)((HashMap)this.subscribers).clone()).values().iterator();
         var2 = this.myConsumer;
      }

      if (!var3.hasNext() && var2 != null) {
         var2.delete(false, true);
      }

      while(var3.hasNext()) {
         var2 = (BEConsumerImpl)var3.next();

         try {
            if (var2 != null) {
               var2.delete(false, true);
            }
         } catch (Throwable var6) {
            var1 = var6;
         }
      }

      if (var1 instanceof JMSException) {
         throw (JMSException)var1;
      } else if (var1 instanceof RuntimeException) {
         throw (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      }
   }

   public void close() throws javax.jms.JMSException {
      Throwable var1 = null;
      BEConsumerImpl var2;
      Iterator var3;
      synchronized(this) {
         var3 = ((HashMap)((HashMap)this.subscribers).clone()).values().iterator();
         var2 = this.myConsumer;
      }

      if (!var3.hasNext() && var2 != null) {
         try {
            var2.close(0L);
         } catch (Throwable var7) {
            var1 = var7;
         }
      }

      while(var3.hasNext()) {
         var2 = (BEConsumerImpl)var3.next();

         try {
            var2.close(0L);
         } catch (Throwable var6) {
            var1 = var6;
         }
      }

      if (var2 != null) {
         var2.cleanupDurableSubscription(true, false, false, false, true);
      }

      if (var1 instanceof JMSException) {
         throw (JMSException)var1;
      } else if (var1 instanceof RuntimeException) {
         throw (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      }
   }

   private int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         int var3 = var2.getMajor();
         int var4 = var2.getMinor();
         if (var3 < 6) {
            throw new IOException("Peer neither compatible with 1 or 2 .  PeerInfo is " + var2);
         }

         if (var3 == 6 && var4 < 2) {
            return 1;
         }
      }

      return 2;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      boolean var2 = false;
      int var3 = this.getVersion(var1);
      if (var3 == 1) {
         this.writeExternal1(var1);
      } else {
         if (var3 >= 2) {
            if (var1 instanceof PersistentStoreOutputStream) {
               var2 = true;
            }

            int var4 = 0;
            var1.writeByte(2);
            if (this.destinationImpl != null) {
               var4 |= 1;
            }

            if (this.selector != null) {
               var4 |= 2;
            }

            if (this.noLocal) {
               var4 |= 4;
            }

            if (!var2) {
               if (this.dsId != null) {
                  var4 |= 8;
               }

               if (this.consumerName != null) {
                  var4 |= 16;
               }
            }

            var1.writeByte((byte)var4);
         }

         super.writeExternal(var1);
         if (this.destinationImpl != null) {
            this.destinationImpl.writeExternal(var1);
         }

         if (this.selector != null) {
            var1.writeUTF(this.selector);
         }

         if (!var2 && var3 >= 2) {
            if (this.dsId != null) {
               this.dsId.writeExternal(var1);
            }

            if (this.getConsumer() != null) {
               this.consumerName = this.getConsumer().getName();
            }

            if (this.consumerName != null) {
               if (var1 instanceof WLObjectOutput) {
                  ((WLObjectOutput)var1).writeAbbrevString(this.consumerName);
               } else {
                  var1.writeUTF(this.consumerName);
               }
            }
         }

      }
   }

   private void writeExternal1(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      if (this.destinationImpl == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.destinationImpl.writeExternal(var1);
      }

      if (this.selector == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeUTF(this.selector);
      }

      var1.writeBoolean(this.noLocal);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 == 1) {
         this.readExternal1(var1);
      } else if (var2 != 2) {
         throw JMSUtilities.versionIOException(var2, 1, 2);
      } else {
         byte var3 = var1.readByte();
         super.readExternal(var1);
         if ((var3 & 1) != 0) {
            this.destinationImpl = new DestinationImpl();
            this.destinationImpl.readExternal(var1);
         }

         if ((var3 & 2) != 0) {
            this.selector = var1.readUTF();
         }

         this.noLocal = (var3 & 4) != 0;
         int var4 = this.getVersion(var1);
         if (var4 >= 2) {
            if ((var3 & 8) != 0) {
               this.dsId = new JMSID();
               this.dsId.readExternal(var1);
            }

            if ((var3 & 16) != 0) {
               if (var1 instanceof WLObjectInput) {
                  this.consumerName = ((WLObjectInput)var1).readAbbrevString();
               } else {
                  this.consumerName = var1.readUTF();
               }

               this.jndiName = BEConsumerImpl.JNDINameForSubscription(this.consumerName);
            }
         }

      }
   }

   private void readExternal1(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         super.readExternal(var1);
         if (var1.readBoolean()) {
            this.destinationImpl = new DestinationImpl();
            this.destinationImpl.readExternal(var1);
         }

         if (var1.readBoolean()) {
            this.selector = var1.readUTF();
         }

         this.noLocal = var1.readBoolean();
      }
   }
}
