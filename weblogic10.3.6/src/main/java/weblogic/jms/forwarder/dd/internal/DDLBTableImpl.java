package weblogic.jms.forwarder.dd.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.forwarder.dd.DDInfo;
import weblogic.jms.forwarder.dd.DDLBTable;
import weblogic.jms.forwarder.dd.DDMemberInfo;

public class DDLBTableImpl implements DDLBTable {
   static final long serialVersionUID = 3958010586618335512L;
   private static final byte EXTVERSION1 = 1;
   private static int versionmask = 255;
   private static final int _HASDDLBMembers = 256;
   private static final int _HASFAILEDDESTINATIONS = 512;
   private String name;
   private DDInfo ddInfo;
   private ArrayList ddMemberInfoArrayList = new ArrayList();
   private DDMemberInfo[] ddMemberInfos;
   private ArrayList ddMemberInfoInDoubtArrayList = new ArrayList();
   private DDMemberInfo[] ddMemberInfosInDoubt;
   private HashMap failedDDMemberInfosbySeqNum = new HashMap();
   private List failedDestinations = new ArrayList();

   public DDLBTableImpl() {
   }

   public DDLBTableImpl(String var1, DDInfo var2) {
      this.ddInfo = var2;
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public DDInfo getDDInfo() {
      return this.ddInfo;
   }

   public void addFailedDDMemberInfo(MessageImpl var1, DDMemberInfo var2) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("DDLBTableImpl: addFailedDDMemberInfo");
      }

      long var3 = var1.getSAFSeqNumber();
      this.addFailedDDMemberInfo(var3, var2);
   }

   public void addFailedDDMemberInfo(long var1, DDMemberInfo var3) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("DDLBTableImpl: addFailedDDMemberInfo():  sequenceNumber= " + var1 + " ddMemberInfo =" + var3);
      }

      synchronized(this) {
         Long var5 = new Long(var1);
         this.failedDDMemberInfosbySeqNum.put(var5, var3);
         this.failedDestinations.add(var3);
      }
   }

   public synchronized DDMemberInfo getFailedDDMemberInfo(long var1) {
      return (DDMemberInfo)this.failedDDMemberInfosbySeqNum.get(new Long(var1));
   }

   public void removeFailedDDMemberInfo(long var1) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("DDLBTableImpl: removeFailedDDMemberInfo():  sequenceNumber= " + var1);
      }

      synchronized(this) {
         DDMemberInfo var4 = (DDMemberInfo)this.failedDDMemberInfosbySeqNum.remove(new Long(var1));
         this.failedDestinations.remove(var4);
      }
   }

   public void addDDMemberInfo(DDMemberInfo var1) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("DDLBTableImpl: addDDMemberInfo(): ddMemberInfo= " + var1);
      }

      synchronized(this) {
         if (this.ddMemberInfoArrayList.contains(var1)) {
            this.ddMemberInfoArrayList.remove(var1);
         }

         this.ddMemberInfoArrayList.add(var1);
         DDMemberInfo[] var3 = new DDMemberInfo[this.ddMemberInfoArrayList.size()];
         this.ddMemberInfos = (DDMemberInfo[])((DDMemberInfo[])this.ddMemberInfoArrayList.toArray(var3));
      }
   }

   public void removeDDMemberInfo(DDMemberInfo var1) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("DDLBTableImpl: removeDDMemberInfo():   ddMemberInfo= " + var1);
      }

      synchronized(this) {
         this.ddMemberInfoArrayList.remove(var1);
         DDMemberInfo[] var3 = new DDMemberInfo[this.ddMemberInfoArrayList.size()];
         this.ddMemberInfos = (DDMemberInfo[])((DDMemberInfo[])this.ddMemberInfoArrayList.toArray(var3));
      }
   }

   public synchronized void addDDMemberInfos(DDMemberInfo[] var1) {
      this.ddMemberInfos = var1;
   }

   public synchronized DDMemberInfo[] getDDMemberInfos() {
      return this.ddMemberInfos;
   }

   public synchronized void removeDDMemberInfos() {
      this.ddMemberInfos = null;
   }

   public synchronized void addInDoubtDDMemberInfos(DDMemberInfo[] var1) {
      this.ddMemberInfosInDoubt = var1;
   }

   public synchronized DDMemberInfo[] getInDoubtDDMemberInfos() {
      return this.ddMemberInfosInDoubt;
   }

   public synchronized void removeInDoubtDDMemberInfos() {
      this.ddMemberInfosInDoubt = null;
   }

   public synchronized HashMap getFailedDDMemberInfosBySeqNum() {
      return this.failedDDMemberInfosbySeqNum;
   }

   public synchronized void setFailedDDMemberInfosBySeqNum(HashMap var1) {
      this.failedDDMemberInfosbySeqNum = var1;
   }

   public synchronized List getFailedDDMemberInfos() {
      return this.failedDestinations;
   }

   public String toString() {
      String var1 = "<DDLBTableImpl> = { name=" + this.name + "," + " ddInfo=" + this.ddInfo + "," + " ddMemberInfos=";
      if (this.ddMemberInfos != null && this.ddMemberInfos.length != 0) {
         for(int var2 = 0; var2 < this.ddMemberInfos.length; ++var2) {
            var1 = var1 + this.ddMemberInfos[var2] + " ";
         }
      } else {
         var1 = var1 + "null";
      }

      var1 = var1 + " ddMemberInfosInDoubt=" + this.ddMemberInfosInDoubt + "}";
      return var1;
   }

   protected int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         int var3 = var2.getMajor();
         if (var3 < 9) {
            throw new IOException(JMSClientExceptionLogger.logIncompatibleVersion9Loggable((byte)1, (byte)1, (byte)1, (byte)1, var2.toString()).getMessage());
         }
      }

      return 1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.getVersion(var1);
      synchronized(this) {
         if (this.ddMemberInfos.length > 0) {
            var2 |= 256;
         }

         int var4 = this.failedDestinations.size();
         if (var4 > 0) {
            var2 |= 512;
         }

         var1.writeInt(var2);
         var1.writeObject(this.failedDDMemberInfosbySeqNum);
         if (var4 > 0) {
            var1.writeObject(this.failedDestinations);
         }

         if (this.ddMemberInfos.length > 0) {
            var1.writeInt(this.ddMemberInfos.length);

            for(int var5 = 0; var5 < this.ddMemberInfos.length; ++var5) {
               this.ddMemberInfos[var5].writeExternal(var1);
            }
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & versionmask;
      synchronized(this) {
         if (var3 < 1) {
            throw JMSUtilities.versionIOException(var3, 1, 1);
         } else {
            this.failedDDMemberInfosbySeqNum = (HashMap)var1.readObject();
            if ((var2 & 512) != 0) {
               this.failedDestinations = (List)var1.readObject();
            }

            if ((var2 & 256) != 0) {
               int var5 = var1.readInt();

               for(int var6 = 0; var6 < var5; ++var6) {
                  DDMemberInfoImpl var7 = new DDMemberInfoImpl();
                  var7.readExternal(var1);
                  this.ddMemberInfoArrayList.add(var7);
               }

               DDMemberInfo[] var10 = new DDMemberInfo[this.ddMemberInfoArrayList.size()];
               this.ddMemberInfos = (DDMemberInfo[])((DDMemberInfo[])this.ddMemberInfoArrayList.toArray(var10));
            }

         }
      }
   }
}
