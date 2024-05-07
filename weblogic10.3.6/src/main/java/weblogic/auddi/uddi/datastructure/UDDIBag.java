package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class UDDIBag implements Serializable {
   private Map m_map = new HashMap();
   private Collection m_collection;
   private Iterator m_iter;

   public void add(UDDIBagObject var1, Object var2) {
      this.m_map.put(var2, var1);
   }

   public void removeAll() {
      this.m_map = new HashMap();
   }

   public UDDIBagObject remove(Object var1) {
      return (UDDIBagObject)this.m_map.remove(var1);
   }

   public UDDIBagObject getMFirst() {
      if (this.m_map.size() == 0) {
         return null;
      } else {
         this.m_collection = this.m_map.values();
         this.m_iter = this.m_collection.iterator();
         return (UDDIBagObject)this.m_iter.next();
      }
   }

   public boolean contains(Object var1) {
      return this.m_map.get(var1) != null;
   }

   public UDDIBagObject getMNext() {
      return this.m_iter != null && this.m_iter.hasNext() ? (UDDIBagObject)this.m_iter.next() : null;
   }

   public int getCount() {
      return this.m_map.size();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof UDDIBag)) {
         return false;
      } else {
         UDDIBag var2 = (UDDIBag)var1;
         if (var2.getCount() != this.getCount()) {
            return false;
         } else {
            UDDIBagObject var3;
            UDDIBagObject var4;
            label53:
            for(var3 = this.getMFirst(); var3 != null; var3 = this.getMNext()) {
               for(var4 = var2.getMFirst(); var4 != null; var4 = var2.getMNext()) {
                  if (var3.equals(var4)) {
                     continue label53;
                  }
               }

               return false;
            }

            label42:
            for(var3 = var2.getMFirst(); var3 != null; var3 = var2.getMNext()) {
               for(var4 = this.getMFirst(); var4 != null; var4 = this.getMNext()) {
                  if (var4.equals(var3)) {
                     continue label42;
                  }
               }

               return false;
            }

            return true;
         }
      }
   }

   public String toXML(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (this.getCount() == 0) {
         var2.append("<").append(var1).append(" />");
         return var2.toString();
      } else {
         if (var1 != null && !var1.equals("")) {
            var2.append("<").append(var1).append(">");
         }

         UDDIBagObject var3 = this.getMFirst();
         if (var3 != null) {
            var2.append(var3.toXML());

            for(var3 = this.getMNext(); var3 != null; var3 = this.getMNext()) {
               var2.append(var3.toXML());
            }
         }

         if (var1 != null && !var1.equals("")) {
            var2.append("</").append(var1).append(">");
         }

         return var2.toString();
      }
   }
}
