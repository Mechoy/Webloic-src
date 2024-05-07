package weblogic.security.ntrealm;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

class NTUserGroupEnum implements Enumeration {
   private String[] list;
   private int index;

   static native void initFields();

   public NTUserGroupEnum(String var1, String[] var2) {
      if (var2 != null) {
         Vector var3 = new Vector();
         this.list = null;

         int var4;
         for(var4 = 0; var4 < var2.length; ++var4) {
            this.list = this.populate(var1, var2[var4]);

            for(int var5 = 0; var5 < this.list.length; ++var5) {
               if (!this.list[var5].endsWith("$") && !var3.contains(this.list[var5]) && !this.list[var5].equals("None")) {
                  var3.addElement(this.list[var5]);
               }
            }
         }

         this.list = new String[var3.size()];
         var4 = 0;

         while(!var3.isEmpty()) {
            this.list[var4++] = (String)var3.firstElement();
            var3.removeElementAt(0);
         }
      } else {
         this.list = this.populate(var1, (String)null);
      }

      this.index = 0;
   }

   public boolean hasMoreElements() {
      if (this.list == null) {
         return false;
      } else {
         return this.index < this.list.length;
      }
   }

   public Object nextElement() {
      if (!this.hasMoreElements()) {
         throw new NoSuchElementException();
      } else {
         return this.list[this.index++];
      }
   }

   private native String[] populate(String var1, String var2);
}
