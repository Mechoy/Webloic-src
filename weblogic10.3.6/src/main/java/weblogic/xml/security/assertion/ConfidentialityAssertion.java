package weblogic.xml.security.assertion;

import weblogic.xml.security.SecurityAssertion;

/** @deprecated */
public class ConfidentialityAssertion implements SecurityAssertion {
   private static final String type = "http://www.bea.com/servers/xml/security/assertion/Confidentiality";
   private static final int code = 3;
   protected String id;

   public ConfidentialityAssertion(String var1) {
      this.id = var1;
   }

   public String getAssertionType() {
      return "http://www.bea.com/servers/xml/security/assertion/Confidentiality";
   }

   public int getAssertionTypeCode() {
      return 3;
   }

   public String getId() {
      return this.id;
   }

   public boolean isAssertionType(String var1) {
      return var1 == "http://www.bea.com/servers/xml/security/assertion/Confidentiality" || "http://www.bea.com/servers/xml/security/assertion/Confidentiality".equals(var1);
   }

   public boolean repudiable() {
      return true;
   }
}
