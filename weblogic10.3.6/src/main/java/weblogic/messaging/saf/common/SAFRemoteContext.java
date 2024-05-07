package weblogic.messaging.saf.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.messaging.saf.utils.Util;

public final class SAFRemoteContext implements Externalizable {
   static final long serialVersionUID = 8077095725140746031L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private long retryDelayBase = -1L;
   private long retryDelayMaximum = -1L;
   private long retryDelayMultiplier = -1L;
   private static final int _HASRETRYDELAYPARAMATERS = 256;
   private static final int _HASPROPERTIES = 512;
   private static final int _HASDEFAULTCOMPRESSIONTHRESHOLD = 1024;

   public SAFRemoteContext(long var1, long var3, long var5) {
      this.retryDelayBase = var1;
      this.retryDelayMaximum = var3;
      this.retryDelayMultiplier = var5;
   }

   public SAFRemoteContext() {
   }

   public long getRetryDelayBase() {
      return this.retryDelayBase;
   }

   public long getRetryDelayMaximum() {
      return this.retryDelayMaximum;
   }

   public long getRetryDelayMultiplier() {
      return this.retryDelayMultiplier;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.retryDelayBase != 0L) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      var1.writeInt(2);
      var1.writeUTF("");
      if (this.retryDelayBase != 0L) {
         var1.writeLong(this.retryDelayBase);
         var1.writeLong(this.retryDelayMaximum);
         var1.writeLong(this.retryDelayMultiplier);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw Util.versionIOException(var3, 1, 1);
      } else {
         var1.readInt();
         var1.readUTF();
         if ((var2 & 256) != 0) {
            this.retryDelayBase = var1.readLong();
            this.retryDelayMaximum = var1.readLong();
            this.retryDelayMultiplier = var1.readLong();
         }

         if ((var2 & 1024) != 0) {
            var1.readInt();
         }

      }
   }
}
