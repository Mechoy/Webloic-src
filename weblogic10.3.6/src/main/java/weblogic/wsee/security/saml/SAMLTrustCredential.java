package weblogic.wsee.security.saml;

import java.util.Calendar;
import org.w3c.dom.Element;
import weblogic.wsee.security.wst.framework.TrustCredential;

public class SAMLTrustCredential implements TrustCredential {
   private String appliesTo;
   private Calendar created;
   private Calendar expires;
   private SAMLCredential cred;
   private Element appliesToElement;

   public SAMLTrustCredential(SAMLCredential var1) {
      this.cred = var1;
   }

   public String getIdentifier() {
      return this.cred.getAssertionID();
   }

   public String getAppliesTo() {
      return this.appliesTo;
   }

   public Element getAppliesToElement() {
      return this.appliesToElement;
   }

   public void setAppliesToElement(Element var1) {
      this.appliesToElement = var1;
   }

   public Calendar getCreated() {
      return this.created;
   }

   public Calendar getExpires() {
      return this.expires;
   }

   public void setAppliesTo(String var1) {
      this.appliesTo = var1;
   }

   public void setCreated(Calendar var1) {
      this.created = var1;
   }

   public void setExpires(Calendar var1) {
      this.expires = var1;
   }

   public SAMLCredential getCredential() {
      return this.cred;
   }

   public boolean hasAttachedSecurityTokenReference() {
      return false;
   }

   public boolean hasUnattachedSecurityTokenReference() {
      return false;
   }
}
