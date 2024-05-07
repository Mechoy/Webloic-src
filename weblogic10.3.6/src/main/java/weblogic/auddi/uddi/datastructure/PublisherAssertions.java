package weblogic.auddi.uddi.datastructure;

public class PublisherAssertions extends UDDIList {
   public void add(PublisherAssertion var1) {
      super.add(var1);
   }

   public PublisherAssertion getFirst() {
      return (PublisherAssertion)super.getVFirst();
   }

   public PublisherAssertion getNext() {
      return (PublisherAssertion)super.getVNext();
   }

   public String toXML() {
      return super.toXML((String)null);
   }

   public boolean equals(Object var1) {
      return this.hasEqualContent(var1);
   }
}
