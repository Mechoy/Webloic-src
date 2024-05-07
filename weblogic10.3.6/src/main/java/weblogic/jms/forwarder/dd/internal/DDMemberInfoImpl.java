package weblogic.jms.forwarder.dd.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.forwarder.dd.DDMemberInfo;
import weblogic.jms.forwarder.dd.DDMemberRuntimeInformation;

public class DDMemberInfoImpl implements DDMemberInfo {
   private static final byte EXTVERSION1 = 1;
   private static int versionmask = 255;
   private String jmsServerName;
   private String ddMemberConfigName;
   private String destinationType;
   private transient DDMemberRuntimeInformation ddMemberRuntimeInformation;
   private transient DestinationImpl dImpl;

   public DDMemberInfoImpl() {
   }

   public DDMemberInfoImpl(String var1, String var2, String var3, DestinationImpl var4) {
      this(var1, var2, var3, var4, (DDMemberRuntimeInformation)null);
   }

   public DDMemberInfoImpl(String var1, String var2, String var3, DestinationImpl var4, DDMemberRuntimeInformation var5) {
      this.jmsServerName = var1;
      this.ddMemberConfigName = var2;
      this.destinationType = var3;
      this.dImpl = var4;
      this.ddMemberRuntimeInformation = var5;
   }

   public String toString() {
      return "<DDMemnerInoImpl> = { ddMemberConfigName=" + this.ddMemberConfigName + "," + " destinationType=" + this.destinationType + "," + " JmsServerName=" + this.jmsServerName + "}";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DDMemberInfoImpl)) {
         return false;
      } else {
         DDMemberInfoImpl var2;
         label44: {
            var2 = (DDMemberInfoImpl)var1;
            if (this.ddMemberConfigName != null) {
               if (this.ddMemberConfigName.equals(var2.ddMemberConfigName)) {
                  break label44;
               }
            } else if (var2.ddMemberConfigName == null) {
               break label44;
            }

            return false;
         }

         if (this.destinationType != null) {
            if (!this.destinationType.equals(var2.destinationType)) {
               return false;
            }
         } else if (var2.destinationType != null) {
            return false;
         }

         if (this.jmsServerName != null) {
            if (!this.jmsServerName.equals(var2.jmsServerName)) {
               return false;
            }
         } else if (var2.jmsServerName != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = this.jmsServerName != null ? this.jmsServerName.hashCode() : 0;
      var1 = 29 * var1 + (this.ddMemberConfigName != null ? this.ddMemberConfigName.hashCode() : 0);
      var1 = 29 * var1 + (this.destinationType != null ? this.destinationType.hashCode() : 0);
      return var1;
   }

   public String getJMSServerName() {
      return this.jmsServerName;
   }

   public String getType() {
      return this.destinationType;
   }

   public String getDDMemberConfigName() {
      return this.ddMemberConfigName;
   }

   public DDMemberRuntimeInformation getDDMemberRuntimeInformation() {
      return this.ddMemberRuntimeInformation;
   }

   public DestinationImpl getDestination() {
      return this.dImpl;
   }

   public void setDestination(DestinationImpl var1) {
      this.dImpl = var1;
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
      var1.writeInt(var2);
      var1.writeUTF(this.jmsServerName);
      var1.writeUTF(this.ddMemberConfigName);
      var1.writeUTF(this.destinationType);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & versionmask;
      if (var3 < 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         this.jmsServerName = var1.readUTF();
         this.ddMemberConfigName = var1.readUTF();
         this.destinationType = var1.readUTF();
      }
   }
}
