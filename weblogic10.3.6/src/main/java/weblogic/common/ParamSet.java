package weblogic.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.util.Date;
import weblogic.common.internal.VectorTable;

public final class ParamSet implements Cloneable, Externalizable {
   private VectorTable ht;
   private boolean readonly;
   private boolean verbose = false;
   public boolean trap = false;
   private boolean loose = true;

   public void initialize() {
   }

   public void destroy() {
      this.ht = null;
   }

   public int size() {
      return this.ht.size();
   }

   public int used() {
      return this.ht.used();
   }

   public boolean isEmpty() {
      return this.ht.isEmpty();
   }

   public final void private_setReadonly(boolean var1) {
      this.readonly = var1;
   }

   public final boolean private_isReadonly() {
      return this.readonly;
   }

   public final void private_setLoose(boolean var1) {
      this.loose = var1;
   }

   public final boolean private_isLoose() {
      return this.loose;
   }

   public ParamValue getValue(String var1) {
      return this.ht.get(var1);
   }

   public ParamValue get(int var1) {
      return this.ht.get(var1);
   }

   public ParamSet(int var1) {
      if (var1 < 1) {
         var1 = 1;
      }

      this.ht = new VectorTable(var1);
   }

   public ParamSet() {
      this.ht = new VectorTable();
   }

   private ParamSet(ParamSet var1) {
      this.ht = (VectorTable)var1.ht.clone();
      this.readonly = var1.readonly;
      this.verbose = var1.verbose;
      this.trap = var1.trap;
   }

   public ParamValue declareParam(String var1, int var2, String var3) throws ParamSetException {
      ParamValue var4 = new ParamValue(var1, var2, 42, var3);
      return this.ht.put(var4);
   }

   public ParamValue declareParam(String var1, int var2) throws ParamSetException {
      return this.declareParam(var1, var2, "");
   }

   public ParamValue getParam(String var1) throws ParamSetException {
      ParamValue var2 = this.getValue(var1);
      if (var2 == null) {
         if (!this.loose) {
            throw new ParamSetException("No such parameter: " + var1);
         }

         var2 = this.declareParam(var1, 43, "");
      }

      return var2;
   }

   public ParamValue getParam(String var1, int var2) throws ParamSetException {
      return this.getParam(var1).elementAt(var2);
   }

   public void setParams(ParamSet var1) throws ParamSetException {
      ParamValue var2 = null;

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2 = var1.get(var3);
         if (var2 != null) {
            this.setParam(var2.name(), var2);
         }
      }

   }

   public void setParam(String var1, String var2, ParamSet var3) throws ParamSetException {
      ParamValue var4 = null;
      int var6 = 0;

      for(int var5 = 0; var5 < var3.size(); ++var5) {
         var4 = var3.get(var5);
         if (var4 != null) {
            this.setParam(var1, var6, var4.name());
            this.setParam(var2, var6, var4);
            ++var6;
         }
      }

   }

   public ParamValue setParam(String var1, ParamValue var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, double var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, float var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, long var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, int var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, short var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, byte var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, boolean var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, char var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, String var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, Date var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, Object var2) throws ParamSetException {
      return this.getParam(var1).set(var2);
   }

   public ParamValue setParam(String var1, int var2, ParamValue var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, double var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, float var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, long var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, int var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, short var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, byte var2, int var3) throws ParamSetException {
      return this.getParam(var1).set((int)var3, var2);
   }

   public ParamValue setParam(String var1, int var2, boolean var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, char var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, String var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, Date var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   public ParamValue setParam(String var1, int var2, Object var3) throws ParamSetException {
      return this.getParam(var1).set(var3, var2);
   }

   private void remove(String var1) throws ParamSetException {
      if (this.ht.get(var1) == null) {
         throw new ParamSetException("Cannot remove non-existent item: " + var1 + " .");
      } else {
         this.ht.remove(var1);
      }
   }

   private void clear() throws ParamSetException {
      if (this.private_isReadonly()) {
         throw new ParamSetException("ParamSet is read only.");
      } else {
         this.ht.clear();
      }
   }

   public Object clone() {
      return new ParamSet(this);
   }

   public String toString() {
      return this.display();
   }

   public String dump() {
      String var1 = "";

      for(int var2 = 0; var2 < this.size(); ++var2) {
         ParamValue var3 = this.get(var2);
         if (var3 != null) {
            var1 = var1 + "\n" + var3.dump();
         }
      }

      return var1;
   }

   public String display() {
      String var2 = "";

      for(int var3 = 0; var3 < this.size(); ++var3) {
         ParamValue var1;
         if ((var1 = this.get(var3)) != null) {
            var2 = var2 + var1.name() + " = " + var1 + "\n";
         }
      }

      return var2;
   }

   public void print(PrintStream var1) {
      var1.println(this.display());
   }

   public String[] getNames() throws ParamSetException {
      String[] var1 = new String[this.used()];
      int var3 = 0;

      for(int var4 = 0; var3 < this.size(); ++var3) {
         ParamValue var2;
         if ((var2 = this.get(var3)) != null) {
            var1[var4++] = var2.name();
         }
      }

      return var1;
   }

   public void readExternal(ObjectInput var1) throws IOException {
      int var2 = var1.readInt();
      if (this.verbose) {
         System.out.println("PS: readcount: " + var2);
      }

      this.ht = new VectorTable(var2 + 1);

      for(int var3 = 0; var3 < var2; ++var3) {
         ParamValue var4 = new ParamValue();
         var4.readExternal(var1);

         try {
            this.setParam(var4.name(), var4);
         } catch (ParamSetException var6) {
            throw new IOException("" + var6);
         }
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.used();
      if (this.verbose) {
         System.out.println("PS: write count/size: " + var2 + "/" + this.size());
      }

      var1.writeInt(var2);

      try {
         int var3 = 0;

         for(int var4 = 0; var3 < this.size() && var4 < var2; ++var3) {
            ParamValue var5 = this.get(var3);
            if (this.verbose) {
               System.out.print("PS #" + var3 + ": writing:");
            }

            if (var5 != null) {
               if (this.verbose) {
                  System.out.println(var5.toString());
               }

               var5.writeExternal(var1);
               ++var4;
            } else if (this.verbose) {
               System.out.println("null");
            }
         }
      } finally {
         if (this.verbose) {
            System.out.println("This.size() now " + this.size());
         }

      }

   }
}
