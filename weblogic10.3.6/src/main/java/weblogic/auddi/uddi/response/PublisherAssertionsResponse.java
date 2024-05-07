package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AuthorizedName;
import weblogic.auddi.uddi.datastructure.PublisherAssertion;
import weblogic.auddi.uddi.datastructure.PublisherAssertions;
import weblogic.auddi.util.Util;

public class PublisherAssertionsResponse extends UDDIResponse {
   private PublisherAssertions publisherAssertions;
   private AuthorizedName authorizedName = null;

   public void addPublisherAssertion(PublisherAssertion var1) throws UDDIException {
      if (this.publisherAssertions == null) {
         this.publisherAssertions = new PublisherAssertions();
      }

      this.publisherAssertions.add(var1);
   }

   public PublisherAssertions getPublisherAssertions() {
      return this.publisherAssertions;
   }

   public void setPublisherAssertions(PublisherAssertions var1) {
      this.publisherAssertions = var1;
   }

   public void setAuthorizedName(String var1) {
      this.authorizedName = new AuthorizedName(var1);
   }

   public AuthorizedName getAuthorizedName() {
      return this.authorizedName;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof PublisherAssertionsResponse)) {
         return false;
      } else {
         PublisherAssertionsResponse var2 = (PublisherAssertionsResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.publisherAssertions, (Object)var2.publisherAssertions);
         var3 &= Util.isEqual((Object)this.authorizedName, (Object)var2.authorizedName);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      if (this.authorizedName == null) {
         var1.append("<publisherAssertions" + super.toXML() + ">");
      } else {
         var1.append("<publisherAssertions" + super.toXML() + " authorizedName=\"" + this.authorizedName.getName() + "\">");
      }

      if (this.publisherAssertions != null) {
         var1.append(this.publisherAssertions.toXML());
      }

      var1.append("</publisherAssertions>");
      return var1.toString();
   }
}
