package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import com.bea.xml.XmlBoolean;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.AlgorithmType;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.IntegrityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.SupportedTokensType;

public class IntegrityDocumentImpl extends XmlComplexContentImpl implements IntegrityDocument {
   private static final long serialVersionUID = 1L;
   private static final QName INTEGRITY$0 = new QName("http://www.bea.com/wls90/security/policy", "Integrity");

   public IntegrityDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public IntegrityDocument.Integrity getIntegrity() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         IntegrityDocument.Integrity var2 = null;
         var2 = (IntegrityDocument.Integrity)this.get_store().find_element_user(INTEGRITY$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setIntegrity(IntegrityDocument.Integrity var1) {
      this.generatedSetterHelperImpl(var1, INTEGRITY$0, 0, (short)1);
   }

   public IntegrityDocument.Integrity addNewIntegrity() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         IntegrityDocument.Integrity var2 = null;
         var2 = (IntegrityDocument.Integrity)this.get_store().add_element_user(INTEGRITY$0);
         return var2;
      }
   }

   public static class IntegrityImpl extends XmlComplexContentImpl implements IntegrityDocument.Integrity {
      private static final long serialVersionUID = 1L;
      private static final QName SIGNATUREALGORITHM$0 = new QName("http://www.bea.com/wls90/security/policy", "SignatureAlgorithm");
      private static final QName CANONICALIZATIONALGORITHM$2 = new QName("http://www.bea.com/wls90/security/policy", "CanonicalizationAlgorithm");
      private static final QName TARGET$4 = new QName("http://www.bea.com/wls90/security/policy", "Target");
      private static final QName SUPPORTEDTOKENS$6 = new QName("http://www.bea.com/wls90/security/policy", "SupportedTokens");
      private static final QName SIGNTOKEN$8 = new QName("", "SignToken");
      private static final QName SUPPORTTRUST10$10 = new QName("", "SupportTrust10");
      private static final QName X509AUTHCONDITIONAL$12 = new QName("", "X509AuthConditional");

      public IntegrityImpl(SchemaType var1) {
         super(var1);
      }

      public AlgorithmType getSignatureAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            AlgorithmType var2 = null;
            var2 = (AlgorithmType)this.get_store().find_element_user(SIGNATUREALGORITHM$0, 0);
            return var2 == null ? null : var2;
         }
      }

      public void setSignatureAlgorithm(AlgorithmType var1) {
         this.generatedSetterHelperImpl(var1, SIGNATUREALGORITHM$0, 0, (short)1);
      }

      public AlgorithmType addNewSignatureAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            AlgorithmType var2 = null;
            var2 = (AlgorithmType)this.get_store().add_element_user(SIGNATUREALGORITHM$0);
            return var2;
         }
      }

      public AlgorithmType getCanonicalizationAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            AlgorithmType var2 = null;
            var2 = (AlgorithmType)this.get_store().find_element_user(CANONICALIZATIONALGORITHM$2, 0);
            return var2 == null ? null : var2;
         }
      }

      public void setCanonicalizationAlgorithm(AlgorithmType var1) {
         this.generatedSetterHelperImpl(var1, CANONICALIZATIONALGORITHM$2, 0, (short)1);
      }

      public AlgorithmType addNewCanonicalizationAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            AlgorithmType var2 = null;
            var2 = (AlgorithmType)this.get_store().add_element_user(CANONICALIZATIONALGORITHM$2);
            return var2;
         }
      }

      public IntegrityTargetType[] getTargetArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ArrayList var2 = new ArrayList();
            this.get_store().find_all_element_users(TARGET$4, var2);
            IntegrityTargetType[] var3 = new IntegrityTargetType[var2.size()];
            var2.toArray(var3);
            return var3;
         }
      }

      public IntegrityTargetType getTargetArray(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            IntegrityTargetType var3 = null;
            var3 = (IntegrityTargetType)this.get_store().find_element_user(TARGET$4, var1);
            if (var3 == null) {
               throw new IndexOutOfBoundsException();
            } else {
               return var3;
            }
         }
      }

      public int sizeOfTargetArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(TARGET$4);
         }
      }

      public void setTargetArray(IntegrityTargetType[] var1) {
         this.check_orphaned();
         this.arraySetterHelper(var1, TARGET$4);
      }

      public void setTargetArray(int var1, IntegrityTargetType var2) {
         this.generatedSetterHelperImpl(var2, TARGET$4, var1, (short)2);
      }

      public IntegrityTargetType insertNewTarget(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            IntegrityTargetType var3 = null;
            var3 = (IntegrityTargetType)this.get_store().insert_element_user(TARGET$4, var1);
            return var3;
         }
      }

      public IntegrityTargetType addNewTarget() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            IntegrityTargetType var2 = null;
            var2 = (IntegrityTargetType)this.get_store().add_element_user(TARGET$4);
            return var2;
         }
      }

      public void removeTarget(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(TARGET$4, var1);
         }
      }

      public SupportedTokensType getSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SupportedTokensType var2 = null;
            var2 = (SupportedTokensType)this.get_store().find_element_user(SUPPORTEDTOKENS$6, 0);
            return var2 == null ? null : var2;
         }
      }

      public boolean isSetSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(SUPPORTEDTOKENS$6) != 0;
         }
      }

      public void setSupportedTokens(SupportedTokensType var1) {
         this.generatedSetterHelperImpl(var1, SUPPORTEDTOKENS$6, 0, (short)1);
      }

      public SupportedTokensType addNewSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SupportedTokensType var2 = null;
            var2 = (SupportedTokensType)this.get_store().add_element_user(SUPPORTEDTOKENS$6);
            return var2;
         }
      }

      public void unsetSupportedTokens() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(SUPPORTEDTOKENS$6, 0);
         }
      }

      public boolean getSignToken() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var2 = null;
            var2 = (SimpleValue)this.get_store().find_attribute_user(SIGNTOKEN$8);
            if (var2 == null) {
               var2 = (SimpleValue)this.get_default_attribute_value(SIGNTOKEN$8);
            }

            return var2 == null ? false : var2.getBooleanValue();
         }
      }

      public XmlBoolean xgetSignToken() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var2 = null;
            var2 = (XmlBoolean)this.get_store().find_attribute_user(SIGNTOKEN$8);
            if (var2 == null) {
               var2 = (XmlBoolean)this.get_default_attribute_value(SIGNTOKEN$8);
            }

            return var2;
         }
      }

      public boolean isSetSignToken() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(SIGNTOKEN$8) != null;
         }
      }

      public void setSignToken(boolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var3 = null;
            var3 = (SimpleValue)this.get_store().find_attribute_user(SIGNTOKEN$8);
            if (var3 == null) {
               var3 = (SimpleValue)this.get_store().add_attribute_user(SIGNTOKEN$8);
            }

            var3.setBooleanValue(var1);
         }
      }

      public void xsetSignToken(XmlBoolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var3 = null;
            var3 = (XmlBoolean)this.get_store().find_attribute_user(SIGNTOKEN$8);
            if (var3 == null) {
               var3 = (XmlBoolean)this.get_store().add_attribute_user(SIGNTOKEN$8);
            }

            var3.set(var1);
         }
      }

      public void unsetSignToken() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(SIGNTOKEN$8);
         }
      }

      public boolean getSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var2 = null;
            var2 = (SimpleValue)this.get_store().find_attribute_user(SUPPORTTRUST10$10);
            if (var2 == null) {
               var2 = (SimpleValue)this.get_default_attribute_value(SUPPORTTRUST10$10);
            }

            return var2 == null ? false : var2.getBooleanValue();
         }
      }

      public XmlBoolean xgetSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var2 = null;
            var2 = (XmlBoolean)this.get_store().find_attribute_user(SUPPORTTRUST10$10);
            if (var2 == null) {
               var2 = (XmlBoolean)this.get_default_attribute_value(SUPPORTTRUST10$10);
            }

            return var2;
         }
      }

      public boolean isSetSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(SUPPORTTRUST10$10) != null;
         }
      }

      public void setSupportTrust10(boolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var3 = null;
            var3 = (SimpleValue)this.get_store().find_attribute_user(SUPPORTTRUST10$10);
            if (var3 == null) {
               var3 = (SimpleValue)this.get_store().add_attribute_user(SUPPORTTRUST10$10);
            }

            var3.setBooleanValue(var1);
         }
      }

      public void xsetSupportTrust10(XmlBoolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var3 = null;
            var3 = (XmlBoolean)this.get_store().find_attribute_user(SUPPORTTRUST10$10);
            if (var3 == null) {
               var3 = (XmlBoolean)this.get_store().add_attribute_user(SUPPORTTRUST10$10);
            }

            var3.set(var1);
         }
      }

      public void unsetSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(SUPPORTTRUST10$10);
         }
      }

      public boolean getX509AuthConditional() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var2 = null;
            var2 = (SimpleValue)this.get_store().find_attribute_user(X509AUTHCONDITIONAL$12);
            if (var2 == null) {
               var2 = (SimpleValue)this.get_default_attribute_value(X509AUTHCONDITIONAL$12);
            }

            return var2 == null ? false : var2.getBooleanValue();
         }
      }

      public XmlBoolean xgetX509AuthConditional() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var2 = null;
            var2 = (XmlBoolean)this.get_store().find_attribute_user(X509AUTHCONDITIONAL$12);
            if (var2 == null) {
               var2 = (XmlBoolean)this.get_default_attribute_value(X509AUTHCONDITIONAL$12);
            }

            return var2;
         }
      }

      public boolean isSetX509AuthConditional() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(X509AUTHCONDITIONAL$12) != null;
         }
      }

      public void setX509AuthConditional(boolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var3 = null;
            var3 = (SimpleValue)this.get_store().find_attribute_user(X509AUTHCONDITIONAL$12);
            if (var3 == null) {
               var3 = (SimpleValue)this.get_store().add_attribute_user(X509AUTHCONDITIONAL$12);
            }

            var3.setBooleanValue(var1);
         }
      }

      public void xsetX509AuthConditional(XmlBoolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var3 = null;
            var3 = (XmlBoolean)this.get_store().find_attribute_user(X509AUTHCONDITIONAL$12);
            if (var3 == null) {
               var3 = (XmlBoolean)this.get_store().add_attribute_user(X509AUTHCONDITIONAL$12);
            }

            var3.set(var1);
         }
      }

      public void unsetX509AuthConditional() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(X509AUTHCONDITIONAL$12);
         }
      }
   }
}
