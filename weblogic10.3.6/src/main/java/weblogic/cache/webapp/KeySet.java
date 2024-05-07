package weblogic.cache.webapp;

import java.io.Serializable;
import java.util.NoSuchElementException;
import weblogic.cache.CacheException;

public class KeySet implements Serializable, Comparable {
   private transient CacheSystem cs;
   private transient StringBuffer sb = new StringBuffer();
   private String key;
   public static final char KEY_SEPARATOR = '\u0000';

   public KeySet(CacheSystem var1) {
      this.cs = var1;
   }

   public Object addKey(String var1, String var2) throws CacheException {
      if (this.cs == null) {
         throw new CacheException("You cannot add more keys once you have called getKey()");
      } else {
         try {
            Object var3 = this.cs.getValueFromScope(var1, var2);
            this.sb.append('\u0000');
            this.sb.append(var3 == null ? "" : var3.toString());
            return var3;
         } catch (NoSuchElementException var4) {
            throw new CacheException("Invalid key/keyScope attribute, key = " + var2);
         }
      }
   }

   public String getKey() {
      String var1;
      try {
         var1 = this.key == null ? (this.key = this.sb.toString()) : this.key;
      } finally {
         this.cs = null;
         this.sb = null;
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof KeySet) {
         KeySet var2 = (KeySet)var1;
         return var2.getKey().equals(this.getKey());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.key.hashCode();
   }

   public int compareTo(Object var1) {
      if (var1 instanceof KeySet) {
         KeySet var2 = (KeySet)var1;
         return this.getKey().compareTo(var2.getKey());
      } else {
         return -1;
      }
   }
}
