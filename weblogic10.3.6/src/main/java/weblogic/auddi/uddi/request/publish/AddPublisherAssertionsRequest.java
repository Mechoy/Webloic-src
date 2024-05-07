package weblogic.auddi.uddi.request.publish;

import weblogic.auddi.uddi.datastructure.PublisherAssertion;
import weblogic.auddi.uddi.datastructure.PublisherAssertions;

public class AddPublisherAssertionsRequest extends UDDIPublishRequest {
   private PublisherAssertions publisherAssertions = null;

   public void addPublisherAssertion(PublisherAssertion var1) {
      if (this.publisherAssertions == null) {
         this.publisherAssertions = new PublisherAssertions();
      }

      this.publisherAssertions.add(var1);
   }

   public void setPublisherAssertions(PublisherAssertions var1) {
      this.publisherAssertions = var1;
   }

   public PublisherAssertions getPublisherAssertions() {
      return this.publisherAssertions;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<add_publisherAssertions");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      if (this.publisherAssertions != null) {
         var1.append(this.publisherAssertions.toXML(""));
      }

      var1.append("</add_publisherAssertions>");
      return var1.toString();
   }
}
