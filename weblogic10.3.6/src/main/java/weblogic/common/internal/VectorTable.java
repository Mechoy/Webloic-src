package weblogic.common.internal;

import java.io.Serializable;
import weblogic.common.ParamValue;

public final class VectorTable implements Cloneable, Serializable {
   private static final long serialVersionUID = 4749091629973897117L;
   ParamValue[] pvs;
   int[] hints;
   int used;
   boolean verbose;
   public static final int NOTFOUND = -1;
   private boolean nohashing;

   public static void main(String[] var0) {
      System.out.println("testing the VectorTable");
      VectorTable var1 = new VectorTable();
      var1.setVerbose(true);
      ParamValue var2 = new ParamValue("foo", 1, 1, "testdescr foo");
      ParamValue var3 = new ParamValue("bar", 1, 1, "testdescr bar");
      if (var1.get("FOO") != null) {
         System.out.println("Failed #1");
      } else {
         System.out.println("OK#1");
      }

      if (var1.put(var2) != var2) {
         System.out.println("Failed #2");
      } else {
         System.out.println("OK#2");
      }

      if (var1.get("FOO") != var2) {
         System.out.println("Failed #3");
      } else {
         System.out.println("OK#3");
      }

      if (var1.put(var3) != var3) {
         System.out.println("Failed #4");
      } else {
         System.out.println("OK#4");
      }

      if (var1.get("FOO") != var2) {
         System.out.println("Failed #5");
      } else {
         System.out.println("OK#5");
      }

      if (var1.get("bAr") != var3) {
         System.out.println("Failed #6");
      } else {
         System.out.println("OK#6");
      }

      var1.clear();
      if (var1.get("bAr") != null) {
         System.out.println("Failed #7");
      } else {
         System.out.println("OK#7");
      }

   }

   public final void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public final int used() {
      return this.used;
   }

   public final int size() {
      return this.pvs.length;
   }

   public VectorTable() {
      this(1);
   }

   public VectorTable(int var1) {
      this.nohashing = true;
      this.pvs = new ParamValue[var1];
      this.hints = new int[var1];
      this.used = 0;
   }

   private VectorTable(VectorTable var1) {
      this.nohashing = true;
      this.pvs = new ParamValue[var1.pvs.length];
      this.hints = new int[var1.hints.length];
      this.used = var1.used;
      this.verbose = var1.verbose;
      System.arraycopy(var1.hints, 0, this.hints, 0, var1.hints.length);

      for(int var2 = 0; var2 < var1.pvs.length; ++var2) {
         if (var1.pvs[var2] != null) {
            this.pvs[var2] = (ParamValue)var1.pvs[var2].clone();
         }
      }

   }

   public synchronized Object clone() {
      return new VectorTable(this);
   }

   private void realloc() {
      if (this.used() >= this.size()) {
         int var1 = this.size();
         int var2 = 2 * var1;
         ParamValue[] var3 = this.pvs;
         this.pvs = new ParamValue[var2];
         System.arraycopy(var3, 0, this.pvs, 0, var1);
         int[] var4 = this.hints;
         this.hints = new int[var2];
         System.arraycopy(var4, 0, this.hints, 0, var1);
         if (this.verbose) {
            System.out.println(this.toString() + "Expanded from " + var1 + " to " + var2 + "; size=" + this.size());
         }

      }
   }

   private int hashhint(String var1) {
      if (this.nohashing) {
         return 0;
      } else if (var1 == null) {
         return 0;
      } else {
         return var1.length() == 0 ? 0 : var1.charAt(0);
      }
   }

   private void clearOne(int var1) {
      if (this.verbose) {
         System.out.println(this.toString() + "cleared item " + var1);
      }

      if (this.pvs[var1] != null) {
         this.pvs[var1] = null;
         this.hints[var1] = 0;
         --this.used;
      }

   }

   private ParamValue replaceOne(int var1, ParamValue var2) {
      if (this.verbose) {
         System.out.println(this.toString() + " replaced item " + var1);
      }

      this.hints[var1] = this.hashhint(var2.name());
      this.pvs[var1] = var2;
      return var2;
   }

   private int getEmpty() {
      int var2 = this.size();
      this.realloc();

      int var1;
      for(var1 = var2; var1 < this.size(); ++var1) {
         if (this.pvs[var1] == null) {
            if (this.verbose) {
               System.out.println(this.toString() + " gotEmpty#1 " + var1);
            }

            return var1;
         }
      }

      for(var1 = 0; var1 < var2; ++var1) {
         if (this.pvs[var1] == null) {
            if (this.verbose) {
               System.out.println(this.toString() + " gotEmpty#2 " + var1);
            }

            return var1;
         }
      }

      if (this.verbose) {
         System.out.println(this.toString() + "failed miserably ");
      }

      var1 = this.size() * 2;
      ParamValue var10000 = this.pvs[var1];
      return -1;
   }

   private int getOne(String var1) {
      int var2 = this.hashhint(var1);
      boolean var3 = false;
      int var4 = this.size();

      int var6;
      for(var6 = 0; var6 < var4 - 1 && this.hints[var6] != var2; ++var6) {
      }

      int var5;
      for(var5 = var6; var5 < var4; ++var5) {
         if (this.pvs[var5] != null && var1.equalsIgnoreCase(this.pvs[var5].name())) {
            if (this.verbose) {
               System.out.println(this.toString() + " gotOne#1 " + var5);
            }

            return var5;
         }
      }

      for(var5 = 0; var5 < var6; ++var5) {
         if (this.pvs[var5] != null && var1.equalsIgnoreCase(this.pvs[var5].name())) {
            if (this.verbose) {
               System.out.println(this.toString() + " gotOne#1 " + var5);
            }

            return var5;
         }
      }

      return -1;
   }

   public String toString() {
      return "[" + super.toString() + " used: " + this.used() + " size: " + this.size() + "]";
   }

   public synchronized ParamValue get(int var1) {
      return var1 >= this.size() ? null : this.pvs[var1];
   }

   public synchronized ParamValue get(String var1) {
      int var2 = this.getOne(var1);
      return var2 != -1 ? this.pvs[var2] : null;
   }

   public synchronized ParamValue put(ParamValue var1) {
      int var2 = this.getOne(var1.name());
      if (var2 != -1) {
         return this.replaceOne(var2, var1);
      } else {
         ++this.used;
         return this.replaceOne(this.getEmpty(), var1);
      }
   }

   public synchronized void remove(String var1) {
      int var2 = this.getOne(var1);
      if (var2 != -1) {
         this.clearOne(var2);
      }

   }

   public boolean isEmpty() {
      return this.used() == 0;
   }

   public synchronized void clear() {
      for(int var1 = 0; var1 < this.size(); ++var1) {
         this.clearOne(var1);
      }

   }
}
