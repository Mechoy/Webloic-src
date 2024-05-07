package weblogic.cache.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BubblingCache extends HashMap implements Externalizable {
   static final long serialVersionUID = -7053192861279007995L;
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private ArrayList bubbler;
   private int maxSize;

   public BubblingCache() {
      this(100);
   }

   public BubblingCache(int var1) {
      super(var1);
      this.maxSize = var1;
      this.bubbler = new ArrayList(var1);
   }

   public synchronized Object get(Object var1) {
      ValuePositionPair var2 = (ValuePositionPair)super.get(var1);
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot get key = null");
      } else if (var2 == null) {
         return null;
      } else {
         if (var2.position != 0) {
            Object var3 = this.bubbler.get(var2.position - 1);
            ValuePositionPair var4 = (ValuePositionPair)super.get(var3);
            var4.position = var2.position--;
            this.bubbler.set(var4.position, var3);
            this.bubbler.set(var2.position, var1);
         }

         return var2.value;
      }
   }

   public synchronized Object put(Object var1, Object var2) {
      ValuePositionPair var3 = (ValuePositionPair)super.get(var1);
      if (var3 != null) {
         Object var7 = var3.value;
         var3.value = var2;
         return var7;
      } else {
         int var4 = this.size();
         ValuePositionPair var5 = new ValuePositionPair();
         var5.value = var2;
         ValuePositionPair var6 = null;
         if (var4 == this.maxSize) {
            var5.position = var4 - 1;
            var6 = (ValuePositionPair)((ValuePositionPair)super.remove(this.bubbler.set(var5.position, var1)));
         } else {
            var5.position = var4;
            this.bubbler.add(var5.position, var1);
         }

         super.put(var1, var5);
         return var6 == null ? null : var6.value;
      }
   }

   public synchronized Object remove(Object var1) {
      ValuePositionPair var2 = (ValuePositionPair)super.get(var1);
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot get key = null");
      } else if (var2 == null) {
         return null;
      } else {
         Object var3 = super.remove(var1);

         for(int var4 = var2.position + 1; var4 < this.bubbler.size(); ++var4) {
            Object var5 = this.bubbler.get(var4);
            ValuePositionPair var6 = (ValuePositionPair)super.get(var5);
            var6.position = var4 - 1;
            this.bubbler.set(var4 - 1, var5);
         }

         int var7 = this.bubbler.size() - 1;
         this.bubbler.remove(var7);
         var2.position = -1;
         var2.value = null;
         return var3;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[");
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append("[");
         var1.append(var3);
         var1.append(": ");
         var1.append(super.get(var3));
         var1.append("]");
      }

      var1.append("]");
      return var1.toString();
   }

   public synchronized void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.maxSize);
      var1.writeInt(this.size());
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.writeObject(var3);
         var1.writeObject(super.get(var3));
      }

   }

   public synchronized void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.maxSize = var1.readInt();
      int var2 = var1.readInt();
      this.bubbler = new ArrayList(this.maxSize);

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         this.bubbler.add("");
      }

      for(var3 = 0; var3 < var2; ++var3) {
         Object var4 = var1.readObject();
         Object var5 = var1.readObject();
         int var6 = ((ValuePositionPair)var5).position;
         this.bubbler.set(var6, var5);
         super.put(var4, var5);
      }

   }

   private static class ValuePositionPair implements Serializable {
      public Object value;
      public int position;

      private ValuePositionPair() {
      }

      public String toString() {
         return "[ " + this.position + ": " + this.value + " ]";
      }

      // $FF: synthetic method
      ValuePositionPair(Object var1) {
         this();
      }
   }
}
