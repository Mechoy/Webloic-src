package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.transaction.xa.Xid;
import weblogic.jms.common.JMSUtilities;

public final class UpgradeXAXid implements Xid, Externalizable {
   private static final byte EXTVERSION = 1;
   static final long serialVersionUID = 68757062319679355L;
   private byte[] gtrid;
   private byte[] bqual;
   private transient int hashCode;
   private int formatId;
   private transient Xid originalXid;

   public byte[] getGlobalTransactionId() {
      return this.gtrid;
   }

   public byte[] getBranchQualifier() {
      return this.bqual;
   }

   public int getFormatId() {
      return this.formatId;
   }

   Xid getOriginalXid() {
      return this.originalXid;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof UpgradeXAXid) {
         UpgradeXAXid var2 = (UpgradeXAXid)var1;
         if (var2.gtrid.length == this.gtrid.length) {
            for(int var3 = 0; var3 < this.gtrid.length; ++var3) {
               if (var2.gtrid[var3] != this.gtrid[var3]) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      if (this.hashCode != 0) {
         return this.hashCode;
      } else {
         for(int var1 = this.gtrid.length - 1; var1 >= 0; --var1) {
            this.hashCode += this.gtrid[var1] & 255;
         }

         if (this.hashCode == 0) {
            this.hashCode = 1;
         }

         return this.hashCode;
      }
   }

   public void writeExternal(ObjectOutput var1) {
      throw new AssertionError("Cannot call writeExternal on an upgrade object");
   }

   public void readExternal(ObjectInput var1) throws IOException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw JMSUtilities.versionIOException(var2, 1, 1);
      } else {
         this.formatId = var1.readInt();
         byte var3 = var1.readByte();
         if (var3 < 0) {
            throw new IOException("Stream corrupted.");
         } else {
            this.gtrid = new byte[var3];
            var1.readFully(this.gtrid);
            var3 = var1.readByte();
            if (var3 < -1) {
               throw new IOException("Stream corrupted.");
            } else {
               if (var3 > -1) {
                  this.bqual = new byte[var3];
                  var1.readFully(this.bqual);
               }

            }
         }
      }
   }
}
