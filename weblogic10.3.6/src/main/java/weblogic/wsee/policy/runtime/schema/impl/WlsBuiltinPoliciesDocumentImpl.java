package weblogic.wsee.policy.runtime.schema.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.runtime.schema.BuiltinPolicyType;
import weblogic.wsee.policy.runtime.schema.WlsBuiltinPoliciesDocument;

public class WlsBuiltinPoliciesDocumentImpl extends XmlComplexContentImpl implements WlsBuiltinPoliciesDocument {
   private static final long serialVersionUID = 1L;
   private static final QName WLSBUILTINPOLICIES$0 = new QName("http://www.oracle.com/weblogic/wsee/policy/runtime/schema", "WlsBuiltinPolicies");

   public WlsBuiltinPoliciesDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public WlsBuiltinPoliciesDocument.WlsBuiltinPolicies getWlsBuiltinPolicies() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         WlsBuiltinPoliciesDocument.WlsBuiltinPolicies var2 = null;
         var2 = (WlsBuiltinPoliciesDocument.WlsBuiltinPolicies)this.get_store().find_element_user(WLSBUILTINPOLICIES$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setWlsBuiltinPolicies(WlsBuiltinPoliciesDocument.WlsBuiltinPolicies var1) {
      this.generatedSetterHelperImpl(var1, WLSBUILTINPOLICIES$0, 0, (short)1);
   }

   public WlsBuiltinPoliciesDocument.WlsBuiltinPolicies addNewWlsBuiltinPolicies() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         WlsBuiltinPoliciesDocument.WlsBuiltinPolicies var2 = null;
         var2 = (WlsBuiltinPoliciesDocument.WlsBuiltinPolicies)this.get_store().add_element_user(WLSBUILTINPOLICIES$0);
         return var2;
      }
   }

   public static class WlsBuiltinPoliciesImpl extends XmlComplexContentImpl implements WlsBuiltinPoliciesDocument.WlsBuiltinPolicies {
      private static final long serialVersionUID = 1L;
      private static final QName BUILTINPOLICY$0 = new QName("http://www.oracle.com/weblogic/wsee/policy/runtime/schema", "BuiltinPolicy");

      public WlsBuiltinPoliciesImpl(SchemaType var1) {
         super(var1);
      }

      public BuiltinPolicyType[] getBuiltinPolicyArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ArrayList var2 = new ArrayList();
            this.get_store().find_all_element_users(BUILTINPOLICY$0, var2);
            BuiltinPolicyType[] var3 = new BuiltinPolicyType[var2.size()];
            var2.toArray(var3);
            return var3;
         }
      }

      public BuiltinPolicyType getBuiltinPolicyArray(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            BuiltinPolicyType var3 = null;
            var3 = (BuiltinPolicyType)this.get_store().find_element_user(BUILTINPOLICY$0, var1);
            if (var3 == null) {
               throw new IndexOutOfBoundsException();
            } else {
               return var3;
            }
         }
      }

      public int sizeOfBuiltinPolicyArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(BUILTINPOLICY$0);
         }
      }

      public void setBuiltinPolicyArray(BuiltinPolicyType[] var1) {
         this.check_orphaned();
         this.arraySetterHelper(var1, BUILTINPOLICY$0);
      }

      public void setBuiltinPolicyArray(int var1, BuiltinPolicyType var2) {
         this.generatedSetterHelperImpl(var2, BUILTINPOLICY$0, var1, (short)2);
      }

      public BuiltinPolicyType insertNewBuiltinPolicy(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            BuiltinPolicyType var3 = null;
            var3 = (BuiltinPolicyType)this.get_store().insert_element_user(BUILTINPOLICY$0, var1);
            return var3;
         }
      }

      public BuiltinPolicyType addNewBuiltinPolicy() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            BuiltinPolicyType var2 = null;
            var2 = (BuiltinPolicyType)this.get_store().add_element_user(BUILTINPOLICY$0);
            return var2;
         }
      }

      public void removeBuiltinPolicy(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(BUILTINPOLICY$0, var1);
         }
      }
   }
}
