package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.IdentityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.SupportedTokensType;

public class IdentityDocumentImpl extends XmlComplexContentImpl implements IdentityDocument {
   private static final long serialVersionUID = 1L;
   private static final QName IDENTITY$0 = new QName("http://www.bea.com/wls90/security/policy", "Identity");

   public IdentityDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public IdentityDocument.Identity getIdentity() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         IdentityDocument.Identity var2 = null;
         var2 = (IdentityDocument.Identity)this.get_store().find_element_user(IDENTITY$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setIdentity(IdentityDocument.Identity var1) {
      this.generatedSetterHelperImpl(var1, IDENTITY$0, 0, (short)1);
   }

   public IdentityDocument.Identity addNewIdentity() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         IdentityDocument.Identity var2 = null;
         var2 = (IdentityDocument.Identity)this.get_store().add_element_user(IDENTITY$0);
         return var2;
      }
   }

   public static class IdentityImpl extends XmlComplexContentImpl implements IdentityDocument.Identity {
      private static final long serialVersionUID = 1L;
      private static final QName SUPPORTEDTOKENS$0 = new QName("http://www.bea.com/wls90/security/policy", "SupportedTokens");

      public IdentityImpl(SchemaType var1) {
         super(var1);
      }

      public SupportedTokensType getSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SupportedTokensType var2 = null;
            var2 = (SupportedTokensType)this.get_store().find_element_user(SUPPORTEDTOKENS$0, 0);
            return var2 == null ? null : var2;
         }
      }

      public boolean isSetSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(SUPPORTEDTOKENS$0) != 0;
         }
      }

      public void setSupportedTokens(SupportedTokensType var1) {
         this.generatedSetterHelperImpl(var1, SUPPORTEDTOKENS$0, 0, (short)1);
      }

      public SupportedTokensType addNewSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SupportedTokensType var2 = null;
            var2 = (SupportedTokensType)this.get_store().add_element_user(SUPPORTEDTOKENS$0);
            return var2;
         }
      }

      public void unsetSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(SUPPORTEDTOKENS$0, 0);
         }
      }
   }
}
