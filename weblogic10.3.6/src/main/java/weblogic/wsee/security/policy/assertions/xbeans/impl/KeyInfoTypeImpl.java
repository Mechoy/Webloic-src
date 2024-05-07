package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.KeyInfoType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenReferenceType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;

public class KeyInfoTypeImpl extends XmlComplexContentImpl implements KeyInfoType {
   private static final long serialVersionUID = 1L;
   private static final QName SECURITYTOKEN$0 = new QName("http://www.bea.com/wls90/security/policy", "SecurityToken");
   private static final QName SECURITYTOKENREFERENCE$2 = new QName("http://www.bea.com/wls90/security/policy", "SecurityTokenReference");

   public KeyInfoTypeImpl(SchemaType var1) {
      super(var1);
   }

   public SecurityTokenType[] getSecurityTokenArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(SECURITYTOKEN$0, var2);
         SecurityTokenType[] var3 = new SecurityTokenType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public SecurityTokenType getSecurityTokenArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SecurityTokenType var3 = null;
         var3 = (SecurityTokenType)this.get_store().find_element_user(SECURITYTOKEN$0, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfSecurityTokenArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(SECURITYTOKEN$0);
      }
   }

   public void setSecurityTokenArray(SecurityTokenType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, SECURITYTOKEN$0);
   }

   public void setSecurityTokenArray(int var1, SecurityTokenType var2) {
      this.generatedSetterHelperImpl(var2, SECURITYTOKEN$0, var1, (short)2);
   }

   public SecurityTokenType insertNewSecurityToken(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SecurityTokenType var3 = null;
         var3 = (SecurityTokenType)this.get_store().insert_element_user(SECURITYTOKEN$0, var1);
         return var3;
      }
   }

   public SecurityTokenType addNewSecurityToken() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SecurityTokenType var2 = null;
         var2 = (SecurityTokenType)this.get_store().add_element_user(SECURITYTOKEN$0);
         return var2;
      }
   }

   public void removeSecurityToken(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(SECURITYTOKEN$0, var1);
      }
   }

   public SecurityTokenReferenceType[] getSecurityTokenReferenceArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(SECURITYTOKENREFERENCE$2, var2);
         SecurityTokenReferenceType[] var3 = new SecurityTokenReferenceType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public SecurityTokenReferenceType getSecurityTokenReferenceArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SecurityTokenReferenceType var3 = null;
         var3 = (SecurityTokenReferenceType)this.get_store().find_element_user(SECURITYTOKENREFERENCE$2, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfSecurityTokenReferenceArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(SECURITYTOKENREFERENCE$2);
      }
   }

   public void setSecurityTokenReferenceArray(SecurityTokenReferenceType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, SECURITYTOKENREFERENCE$2);
   }

   public void setSecurityTokenReferenceArray(int var1, SecurityTokenReferenceType var2) {
      this.generatedSetterHelperImpl(var2, SECURITYTOKENREFERENCE$2, var1, (short)2);
   }

   public SecurityTokenReferenceType insertNewSecurityTokenReference(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SecurityTokenReferenceType var3 = null;
         var3 = (SecurityTokenReferenceType)this.get_store().insert_element_user(SECURITYTOKENREFERENCE$2, var1);
         return var3;
      }
   }

   public SecurityTokenReferenceType addNewSecurityTokenReference() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SecurityTokenReferenceType var2 = null;
         var2 = (SecurityTokenReferenceType)this.get_store().add_element_user(SECURITYTOKENREFERENCE$2);
         return var2;
      }
   }

   public void removeSecurityTokenReference(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(SECURITYTOKENREFERENCE$2, var1);
      }
   }
}
