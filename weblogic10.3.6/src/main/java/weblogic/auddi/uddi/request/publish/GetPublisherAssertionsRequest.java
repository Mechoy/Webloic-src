package weblogic.auddi.uddi.request.publish;

public class GetPublisherAssertionsRequest extends UDDIPublishRequest {
   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_publisherAssertions");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      var1.append("</get_publisherAssertions>");
      return var1.toString();
   }
}
