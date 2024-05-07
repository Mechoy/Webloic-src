package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.internal.NamingNode;

public abstract class SingularAggregatable implements Aggregatable, Externalizable {
   static final long serialVersionUID = 3833976158056390134L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int HASLEADERJMSID = 256;
   private transient JMSDispatcher leaderDispatcher;
   private transient String jndiName;
   private transient int aggregatableCount = 0;
   private JMSID leaderJMSID;
   private long leaderSequenceNumber;

   public abstract void hadConflict(boolean var1);

   public final void setJNDIName(String var1) {
      this.jndiName = var1;
   }

   public final String getJNDIName() {
      return this.jndiName;
   }

   public final JMSDispatcher getLeaderDispatcher() {
      return this.leaderDispatcher;
   }

   public final void setLeaderDispatcher(JMSDispatcher var1) {
      this.leaderDispatcher = var1;
   }

   public final void setLeaderJMSID(JMSID var1) {
      this.leaderJMSID = var1;
   }

   public final JMSID getLeaderJMSID() {
      return this.leaderJMSID;
   }

   public final void setLeaderSequenceNumber(long var1) {
      this.leaderSequenceNumber = var1;
   }

   public final long getLeaderSequenceNumber() {
      return this.leaderSequenceNumber;
   }

   protected boolean hasAggregatable() {
      synchronized(this) {
         return this.aggregatableCount > 0;
      }
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) {
      String var6 = null;
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatable.onBind(" + var6 + ":" + this.aggregatableCount + ")");
      }

      try {
         var6 = var1.getNameInNamespace(var2);
      } catch (NamingException var10) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatble.onBind failed with naming excption: " + var10);
         }

         return;
      } catch (RemoteException var11) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatble.onBind failed with remote excption: " + var11);
         }

         return;
      }

      SingularAggregatable var4;
      if (var3 == null) {
         var4 = this;
      } else {
         var4 = (SingularAggregatable)var3;
      }

      SingularAggregatableManager var5 = SingularAggregatableManager.findOrCreate();
      var5.aggregatableDidBind(var6, var4);
      synchronized(this) {
         ++this.aggregatableCount;
      }
   }

   public final void onRebind(NamingNode var1, String var2, Aggregatable var3) {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         try {
            JMSDebug.JMSCommon.debug("SingularAggregatable:onRebind(" + var1.getNameInNamespace(var2) + ":" + this.aggregatableCount + ")");
         } catch (NamingException var5) {
         } catch (RemoteException var6) {
         }
      }

   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      boolean var4 = false;
      synchronized(this) {
         if (--this.aggregatableCount <= 0) {
            this.aggregatableCount = 0;
            var4 = true;
         }
      }

      String var5 = null;

      try {
         var5 = var1.getNameInNamespace(var2);
      } catch (NamingException var11) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatble.onUnBind failed with naming excption: " + var11);
         }

         return var4;
      } catch (RemoteException var12) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatble.onUnBind failed with remote excption: " + var12);
         }

         return var4;
      }

      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatable:onUnbind(" + var5 + ":" + this.aggregatableCount + ")");
      }

      SingularAggregatable var6;
      if (var3 == null) {
         var6 = this;
      } else {
         var6 = (SingularAggregatable)var3;
      }

      LeaderManager var7 = LeaderManager.getLeaderManager();
      var7.aggregatableDidBind(var5, var6.getLeaderJMSID(), var6.getLeaderSequenceNumber());
      synchronized(this) {
         if (--this.aggregatableCount <= 0) {
            this.aggregatableCount = 0;
            return true;
         } else {
            return false;
         }
      }
   }

   public String toString() {
      return "SingularAggregatable(" + this.leaderJMSID + ":" + this.leaderSequenceNumber + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.leaderJMSID != null) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      if (this.leaderJMSID != null) {
         this.leaderJMSID.writeExternal(var1);
      }

      var1.writeLong(this.leaderSequenceNumber);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         if ((var2 & 256) != 0) {
            this.leaderJMSID = new JMSID();
            this.leaderJMSID.readExternal(var1);
         }

         this.leaderSequenceNumber = var1.readLong();
      }
   }
}
