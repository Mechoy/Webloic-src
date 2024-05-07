package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class TransportRequirementsMBeanImpl extends XMLElementMBeanDelegate implements TransportRequirementsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_integrity = false;
   private String integrity;
   private boolean isSet_confidentiality = false;
   private String confidentiality;
   private boolean isSet_clientCertAuthentication = false;
   private String clientCertAuthentication;

   public String getIntegrity() {
      return this.integrity;
   }

   public void setIntegrity(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.integrity;
      this.integrity = var1;
      this.isSet_integrity = var1 != null;
      this.checkChange("integrity", var2, this.integrity);
   }

   public String getConfidentiality() {
      return this.confidentiality;
   }

   public void setConfidentiality(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.confidentiality;
      this.confidentiality = var1;
      this.isSet_confidentiality = var1 != null;
      this.checkChange("confidentiality", var2, this.confidentiality);
   }

   public String getClientCertAuthentication() {
      return this.clientCertAuthentication;
   }

   public void setClientCertAuthentication(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.clientCertAuthentication;
      this.clientCertAuthentication = var1;
      this.isSet_clientCertAuthentication = var1 != null;
      this.checkChange("clientCertAuthentication", var2, this.clientCertAuthentication);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<transport-requirements");
      var2.append(">\n");
      if (null != this.getIntegrity()) {
         var2.append(ToXML.indent(var1 + 2)).append("<integrity>").append(this.getIntegrity()).append("</integrity>\n");
      }

      if (null != this.getConfidentiality()) {
         var2.append(ToXML.indent(var1 + 2)).append("<confidentiality>").append(this.getConfidentiality()).append("</confidentiality>\n");
      }

      if (null != this.getClientCertAuthentication()) {
         var2.append(ToXML.indent(var1 + 2)).append("<client-cert-authentication>").append(this.getClientCertAuthentication()).append("</client-cert-authentication>\n");
      }

      var2.append(ToXML.indent(var1)).append("</transport-requirements>\n");
      return var2.toString();
   }
}
