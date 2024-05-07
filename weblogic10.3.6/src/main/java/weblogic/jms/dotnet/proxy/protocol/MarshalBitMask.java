package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class MarshalBitMask {
   private static final int VERSION_MASK = 255;
   private static final int HAS_EXTENSION = Integer.MIN_VALUE;
   private int[] masks;
   private int version;
   static final boolean debug = false;

   public MarshalBitMask(int var1) {
      this.version = var1;
      this.masks = new int[1];
      this.masks[0] = var1;
   }

   public final int getVersion() {
      return this.version;
   }

   public final boolean isSet(int var1) {
      int var2 = this.getIndex(var1);
      int var3 = this.getPosition(var1, var2);
      if (var2 >= this.masks.length) {
         return false;
      } else {
         int var4 = this.masks[var2];
         return (var4 & 1 << var3) != 0;
      }
   }

   public final void setBit(int var1) {
      int var2 = this.getIndex(var1);
      if (var2 >= this.masks.length) {
         this.expand(var2);
      }

      int var3 = this.getPosition(var1, var2);
      int[] var10000 = this.masks;
      var10000[var2] |= 1 << var3;
   }

   private int getIndex(int var1) {
      return (var1 + 7) / 31;
   }

   private int getPosition(int var1, int var2) {
      return var1 + 7 - var2 * 31;
   }

   private void expand(int var1) {
      int[] var2 = new int[var1 + 1];

      int var3;
      for(var3 = 0; var3 < this.masks.length - 1; ++var3) {
         var2[var3] = this.masks[var3];
      }

      var2[this.masks.length - 1] = this.masks[this.masks.length - 1] | Integer.MIN_VALUE;

      for(var3 = this.masks.length; var3 < var1 - 1; ++var3) {
         var2[var3] = Integer.MIN_VALUE;
      }

      this.masks = var2;
   }

   public MarshalBitMask() {
   }

   public void marshal(MarshalWriter var1) {
      int[] var2 = this.masks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var1.writeInt(var5);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.masks = new int[1];
      int var2 = 0;

      while(((this.masks[var2++] = var1.readInt()) & Integer.MIN_VALUE) != 0) {
         this.expand(var2);
      }

      this.version = this.masks[0] & 255;
   }

   private void debug(String var1) {
      System.out.println("[MarshalBitMask]: " + var1);
   }
}
