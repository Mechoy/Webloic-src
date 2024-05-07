package weblogic.messaging.util;

public abstract class AbstractElement implements Element {
   private Element next;
   private Element prev;
   private List list;

   public void setNext(Element var1) {
      this.next = var1;
   }

   public Element getNext() {
      return this.next;
   }

   public void setPrev(Element var1) {
      this.prev = var1;
   }

   public Element getPrev() {
      return this.prev;
   }

   public void setList(List var1) {
      this.list = var1;
   }

   public List getList() {
      return this.list;
   }
}
