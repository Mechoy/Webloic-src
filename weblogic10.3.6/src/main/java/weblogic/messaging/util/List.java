package weblogic.messaging.util;

import java.util.NoSuchElementException;

public class List {
   int size;
   Element first;
   Element last;

   public void add(Element var1) {
      if (var1.getList() != null) {
         throw new IllegalArgumentException();
      } else {
         var1.setNext((Element)null);
         if (this.first == null) {
            this.first = var1;
         } else {
            this.last.setNext(var1);
         }

         var1.setPrev(this.last);
         this.last = var1;
         var1.setList(this);
         ++this.size;
      }
   }

   public void add(List var1) {
      if (var1 == this) {
         throw new IllegalArgumentException();
      } else if (!var1.isEmpty()) {
         if (this.first == null) {
            this.first = var1.getFirst();
         } else {
            this.last.setNext(var1.getFirst());
            var1.getFirst().setPrev(this.last);
         }

         this.last = var1.getLast();
         this.size += var1.size();

         for(Element var2 = var1.getFirst(); var2 != null; var2 = var2.getNext()) {
            var2.setList(this);
         }

         var1.first = var1.last = null;
         var1.size = 0;
      }
   }

   public void clear() {
      while(this.first != null) {
         this.remove(this.first);
      }

   }

   public boolean contains(Element var1) {
      return var1.getList() == this;
   }

   public Element remove(Element var1) {
      if (!this.contains(var1)) {
         throw new NoSuchElementException();
      } else {
         if (var1.getNext() != null) {
            var1.getNext().setPrev(var1.getPrev());
         } else {
            this.last = var1.getPrev();
         }

         if (var1.getPrev() == null) {
            this.first = var1.getNext();
         } else {
            var1.getPrev().setNext(var1.getNext());
         }

         var1.setList((List)null);
         var1.setPrev((Element)null);
         var1.setNext((Element)null);
         --this.size;
         return var1;
      }
   }

   public List split(Element var1, boolean var2) {
      if (!this.contains(var1)) {
         throw new NoSuchElementException();
      } else {
         List var3 = new List();
         if (var2) {
            if (var1 == this.last) {
               return var3;
            }

            var3.first = var1.getNext();
            var3.last = this.last;
            this.last = var1;
            var3.first.setPrev((Element)null);
            this.last.setNext((Element)null);
         } else {
            var3.first = var1;
            var3.last = this.last;
            if (var1 == this.first) {
               this.first = this.last = null;
            } else {
               this.last = var1.getPrev();
               this.last.setNext((Element)null);
               var1.setPrev((Element)null);
            }
         }

         for(var1 = var3.getFirst(); var1 != null; --this.size) {
            var1.setList(var3);
            var1 = var1.getNext();
            ++var3.size;
         }

         return var3;
      }
   }

   public Element getFirst() {
      return this.first;
   }

   public Element getLast() {
      return this.last;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public ListIterator iterator(Element var1) {
      return new ListIterator(this, var1);
   }

   public ListIterator iterator() {
      return this.iterator(this.first);
   }

   public Element[] toArray(Element[] var1) {
      Element[] var2;
      if (var1 != null && var1.length >= this.size) {
         var2 = var1;
      } else {
         var2 = new Element[this.size];
      }

      int var3 = 0;

      for(Element var4 = this.first; var4 != null; ++var3) {
         var2[var3] = var4;
         var4 = var4.getNext();
      }

      return var2;
   }
}
