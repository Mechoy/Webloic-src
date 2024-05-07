package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import com.bea.xml.XmlBoolean;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.AlgorithmType;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityDocument;
import weblogic.wsee.security.policy.assertions.xbeans.ConfidentialityTargetType;
import weblogic.wsee.security.policy.assertions.xbeans.KeyInfoType;

public class ConfidentialityDocumentImpl extends XmlComplexContentImpl implements ConfidentialityDocument {
   private static final long serialVersionUID = 1L;
   private static final QName CONFIDENTIALITY$0 = new QName("http://www.bea.com/wls90/security/policy", "Confidentiality");

   public ConfidentialityDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public ConfidentialityDocument.Confidentiality getConfidentiality() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ConfidentialityDocument.Confidentiality var2 = null;
         var2 = (ConfidentialityDocument.Confidentiality)this.get_store().find_element_user(CONFIDENTIALITY$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setConfidentiality(ConfidentialityDocument.Confidentiality var1) {
      this.generatedSetterHelperImpl(var1, CONFIDENTIALITY$0, 0, (short)1);
   }

   public ConfidentialityDocument.Confidentiality addNewConfidentiality() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ConfidentialityDocument.Confidentiality var2 = null;
         var2 = (ConfidentialityDocument.Confidentiality)this.get_store().add_element_user(CONFIDENTIALITY$0);
         return var2;
      }
   }

   public static class ConfidentialityImpl extends XmlComplexContentImpl implements ConfidentialityDocument.Confidentiality {
      private static final long serialVersionUID = 1L;
      private static final QName KEYWRAPPINGALGORITHM$0 = new QName("http://www.bea.com/wls90/security/policy", "KeyWrappingAlgorithm");
      private static final QName TARGET$2 = new QName("http://www.bea.com/wls90/security/policy", "Target");
      private static final QName KEYINFO$4 = new QName("http://www.bea.com/wls90/security/policy", "KeyInfo");
      private static final QName SUPPORTTRUST10$6 = new QName("", "SupportTrust10");

      public ConfidentialityImpl(SchemaType var1) {
         super(var1);
      }

      public AlgorithmType getKeyWrappingAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            AlgorithmType var2 = null;
            var2 = (AlgorithmType)this.get_store().find_element_user(KEYWRAPPINGALGORITHM$0, 0);
            return var2 == null ? null : var2;
         }
      }

      public boolean isSetKeyWrappingAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(KEYWRAPPINGALGORITHM$0) != 0;
         }
      }

      public void setKeyWrappingAlgorithm(AlgorithmType var1) {
         this.generatedSetterHelperImpl(var1, KEYWRAPPINGALGORITHM$0, 0, (short)1);
      }

      public AlgorithmType addNewKeyWrappingAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            AlgorithmType var2 = null;
            var2 = (AlgorithmType)this.get_store().add_element_user(KEYWRAPPINGALGORITHM$0);
            return var2;
         }
      }

      public void unsetKeyWrappingAlgorithm() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(KEYWRAPPINGALGORITHM$0, 0);
         }
      }

      public ConfidentialityTargetType[] getTargetArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ArrayList var2 = new ArrayList();
            this.get_store().find_all_element_users(TARGET$2, var2);
            ConfidentialityTargetType[] var3 = new ConfidentialityTargetType[var2.size()];
            var2.toArray(var3);
            return var3;
         }
      }

      public ConfidentialityTargetType getTargetArray(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ConfidentialityTargetType var3 = null;
            var3 = (ConfidentialityTargetType)this.get_store().find_element_user(TARGET$2, var1);
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
            return this.get_store().count_elements(TARGET$2);
         }
      }

      public void setTargetArray(ConfidentialityTargetType[] var1) {
         this.check_orphaned();
         this.arraySetterHelper(var1, TARGET$2);
      }

      public void setTargetArray(int var1, ConfidentialityTargetType var2) {
         this.generatedSetterHelperImpl(var2, TARGET$2, var1, (short)2);
      }

      public ConfidentialityTargetType insertNewTarget(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ConfidentialityTargetType var3 = null;
            var3 = (ConfidentialityTargetType)this.get_store().insert_element_user(TARGET$2, var1);
            return var3;
         }
      }

      public ConfidentialityTargetType addNewTarget() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ConfidentialityTargetType var2 = null;
            var2 = (ConfidentialityTargetType)this.get_store().add_element_user(TARGET$2);
            return var2;
         }
      }

      public void removeTarget(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(TARGET$2, var1);
         }
      }

      public KeyInfoType getKeyInfo() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            KeyInfoType var2 = null;
            var2 = (KeyInfoType)this.get_store().find_element_user(KEYINFO$4, 0);
            return var2 == null ? null : var2;
         }
      }

      public void setKeyInfo(KeyInfoType var1) {
         this.generatedSetterHelperImpl(var1, KEYINFO$4, 0, (short)1);
      }

      public KeyInfoType addNewKeyInfo() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            KeyInfoType var2 = null;
            var2 = (KeyInfoType)this.get_store().add_element_user(KEYINFO$4);
            return var2;
         }
      }

      public boolean getSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var2 = null;
            var2 = (SimpleValue)this.get_store().find_attribute_user(SUPPORTTRUST10$6);
            if (var2 == null) {
               var2 = (SimpleValue)this.get_default_attribute_value(SUPPORTTRUST10$6);
            }

            return var2 == null ? false : var2.getBooleanValue();
         }
      }

      public XmlBoolean xgetSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var2 = null;
            var2 = (XmlBoolean)this.get_store().find_attribute_user(SUPPORTTRUST10$6);
            if (var2 == null) {
               var2 = (XmlBoolean)this.get_default_attribute_value(SUPPORTTRUST10$6);
            }

            return var2;
         }
      }

      public boolean isSetSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(SUPPORTTRUST10$6) != null;
         }
      }

      public void setSupportTrust10(boolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var3 = null;
            var3 = (SimpleValue)this.get_store().find_attribute_user(SUPPORTTRUST10$6);
            if (var3 == null) {
               var3 = (SimpleValue)this.get_store().add_attribute_user(SUPPORTTRUST10$6);
            }

            var3.setBooleanValue(var1);
         }
      }

      public void xsetSupportTrust10(XmlBoolean var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlBoolean var3 = null;
            var3 = (XmlBoolean)this.get_store().find_attribute_user(SUPPORTTRUST10$6);
            if (var3 == null) {
               var3 = (XmlBoolean)this.get_store().add_attribute_user(SUPPORTTRUST10$6);
            }

            var3.set(var1);
         }
      }

      public void unsetSupportTrust10() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(SUPPORTTRUST10$6);
         }
      }
   }
}
