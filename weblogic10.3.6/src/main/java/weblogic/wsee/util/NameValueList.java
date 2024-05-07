package weblogic.wsee.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.xml.xmlnode.EmptyIterator;

public class NameValueList {
   private List list = new ArrayList();

   public void put(String var1, Object var2) {
      if (this.list == null) {
         this.list = new ArrayList();
      }

      this.list.add(new Entry(var1, var2));
   }

   public Object get(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("name can not be null");
      } else if (this.list == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < this.list.size(); ++var2) {
            Entry var3 = (Entry)this.list.get(var2);
            if (var1.equals(var3.name)) {
               return var3.value;
            }
         }

         return null;
      }
   }

   public List getList(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("name can not be null");
      } else {
         LinkedList var2 = new LinkedList();
         if (this.list == null) {
            return var2;
         } else {
            for(int var3 = 0; var3 < this.list.size(); ++var3) {
               Entry var4 = (Entry)this.list.get(var3);
               if (var1.equals(var4.name)) {
                  var2.add(var4.value);
               }
            }

            return var2;
         }
      }
   }

   public void remove(String var1) {
      if (this.list != null) {
         for(int var2 = 0; var2 < this.list.size(); ++var2) {
            Entry var3 = (Entry)this.list.get(var2);
            if (var1.equals(var3.name)) {
               this.list.remove(var2);
            }
         }
      }

   }

   public Iterator iterator() {
      return this.list.iterator();
   }

   public Iterator names() {
      return (Iterator)(this.list == null ? EmptyIterator.iterator : new NameIterator());
   }

   public int size() {
      return this.list == null ? 0 : this.list.size();
   }

   public Entry get(int var1) {
      return this.list == null ? null : (Entry)this.list.get(var1);
   }

   public Iterator values() {
      return (Iterator)(this.list == null ? EmptyIterator.iterator : new ValueIterator());
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeArray("list", this.values());
      var1.end();
   }

   private class ValueIterator implements Iterator {
      int index;

      private ValueIterator() {
         this.index = 0;
      }

      public boolean hasNext() {
         return NameValueList.this.list.size() > this.index;
      }

      public Object next() {
         Object var1 = ((Entry)NameValueList.this.list.get(this.index)).value;
         ++this.index;
         return var1;
      }

      public void remove() {
         NameValueList.this.list.remove(this.index);
      }

      // $FF: synthetic method
      ValueIterator(Object var2) {
         this();
      }
   }

   private class NameIterator implements Iterator {
      int index;

      private NameIterator() {
         this.index = 0;
      }

      public boolean hasNext() {
         return NameValueList.this.list.size() > this.index;
      }

      public Object next() {
         String var1 = ((Entry)NameValueList.this.list.get(this.index)).name;
         ++this.index;
         return var1;
      }

      public void remove() {
         NameValueList.this.list.remove(this.index);
      }

      // $FF: synthetic method
      NameIterator(Object var2) {
         this();
      }
   }

   public static class Entry {
      String name;
      Object value;

      public Entry(String var1, Object var2) {
         this.name = var1;
         this.value = var2;
      }

      public String name() {
         return this.name;
      }

      public Object value() {
         return this.value;
      }
   }
}
