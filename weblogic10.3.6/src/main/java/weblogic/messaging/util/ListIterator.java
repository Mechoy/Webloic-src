package weblogic.messaging.util;

public final class ListIterator {
   private List list;
   private Element cursor;
   private Element element;

   ListIterator(List var1, Element var2) {
      this.list = var1;
      this.initialize(var2);
   }

   public void initialize(Element var1) {
      if (var1 != null && !this.list.contains(var1)) {
         throw new IllegalArgumentException("List does not contain the specified element");
      } else {
         this.cursor = var1;
         this.element = null;
      }
   }

   public boolean hasNext() {
      return this.cursor != null;
   }

   public boolean hasPrevious() {
      if (this.cursor == null) {
         return this.list.getLast() != null;
      } else {
         return this.cursor.getPrev() != null;
      }
   }

   public Element next() {
      if ((this.element = this.cursor) != null) {
         this.cursor = this.cursor.getNext();
      }

      return this.element;
   }

   public Element previous() {
      if (this.cursor != null) {
         if (this.cursor.getPrev() != null) {
            return this.element = this.cursor = this.cursor.getPrev();
         }
      } else if (this.list.getLast() != null) {
         return this.element = this.cursor = this.list.getLast();
      }

      return this.element;
   }

   public Element remove() {
      if (this.element == null) {
         throw new IllegalStateException();
      } else {
         if (this.element == this.cursor) {
            this.cursor = this.cursor.getNext();
         }

         this.list.remove(this.element);
         Element var1 = this.element;
         this.element = null;
         return var1;
      }
   }

   public void set(Object var1) {
      if (this.element == null) {
         throw new IllegalStateException();
      } else {
         Element var2 = (Element)var1;
         this.remove();
         this.add(var2);
         if (this.cursor == this.element) {
            this.cursor = var2;
         }

         this.element = var2;
      }
   }

   public void add(Element var1) {
      if (var1.getList() != null) {
         throw new IllegalArgumentException();
      } else {
         if (this.cursor != null) {
            var1.setNext(this.cursor);
            var1.setPrev(this.cursor.getPrev());
            this.cursor.setPrev(var1);
         } else {
            var1.setNext((Element)null);
            var1.setPrev(this.list.getLast());
            this.list.last = var1;
         }

         if (var1.getPrev() == null) {
            this.list.first = var1;
         } else {
            var1.getPrev().setNext(var1);
         }

         this.element = null;
         var1.setList(this.list);
         ++this.list.size;
      }
   }

   public int nextIndex() {
      if (this.cursor == null) {
         return this.list.size;
      } else {
         Element var1 = this.cursor.getPrev();

         int var2;
         for(var2 = 0; var1 != null; var1 = var1.getPrev()) {
            ++var2;
         }

         return var2;
      }
   }

   public int previousIndex() {
      if (this.cursor == null) {
         return this.list.size - 1;
      } else {
         Element var1 = this.cursor.getPrev();

         int var2;
         for(var2 = -1; var1 != null; ++var2) {
            var1 = var1.getPrev();
         }

         return var2;
      }
   }
}
