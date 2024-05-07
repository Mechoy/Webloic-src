package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class IIOPSecurityDescriptorMBeanImpl extends XMLElementMBeanDelegate implements IIOPSecurityDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_identityAssertion = false;
   private String identityAssertion;
   private boolean isSet_transportRequirements = false;
   private TransportRequirementsMBean transportRequirements;
   private boolean isSet_clientAuthentication = false;
   private String clientAuthentication;

   public String getIdentityAssertion() {
      return this.identityAssertion;
   }

   public void setIdentityAssertion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.identityAssertion;
      this.identityAssertion = var1;
      this.isSet_identityAssertion = var1 != null;
      this.checkChange("identityAssertion", var2, this.identityAssertion);
   }

   public TransportRequirementsMBean getTransportRequirements() {
      return this.transportRequirements;
   }

   public void setTransportRequirements(TransportRequirementsMBean var1) {
      TransportRequirementsMBean var2 = this.transportRequirements;
      this.transportRequirements = var1;
      this.isSet_transportRequirements = var1 != null;
      this.checkChange("transportRequirements", var2, this.transportRequirements);
   }

   public String getClientAuthentication() {
      return this.clientAuthentication;
   }

   public void setClientAuthentication(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.clientAuthentication;
      this.clientAuthentication = var1;
      this.isSet_clientAuthentication = var1 != null;
      this.checkChange("clientAuthentication", var2, this.clientAuthentication);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<iiop-security-descriptor");
      var2.append(">\n");
      if (null != this.getTransportRequirements()) {
         var2.append(this.getTransportRequirements().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getClientAuthentication()) {
         var2.append(ToXML.indent(var1 + 2)).append("<client-authentication>").append(this.getClientAuthentication()).append("</client-authentication>\n");
      }

      if (null != this.getIdentityAssertion()) {
         var2.append(ToXML.indent(var1 + 2)).append("<identity-assertion>").append(this.getIdentityAssertion()).append("</identity-assertion>\n");
      }

      var2.append(ToXML.indent(var1)).append("</iiop-security-descriptor>\n");
      return var2.toString();
   }
}
