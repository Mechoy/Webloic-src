package weblogic.wsee.policy.runtime.schema.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.runtime.schema.BuiltinPolicyDocument;
import weblogic.wsee.policy.runtime.schema.BuiltinPolicyType;

public class BuiltinPolicyDocumentImpl extends XmlComplexContentImpl implements BuiltinPolicyDocument {
   private static final long serialVersionUID = 1L;
   private static final QName BUILTINPOLICY$0 = new QName("http://www.oracle.com/weblogic/wsee/policy/runtime/schema", "BuiltinPolicy");

   public BuiltinPolicyDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public BuiltinPolicyType getBuiltinPolicy() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         BuiltinPolicyType var2 = null;
         var2 = (BuiltinPolicyType)this.get_store().find_element_user(BUILTINPOLICY$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setBuiltinPolicy(BuiltinPolicyType var1) {
      this.generatedSetterHelperImpl(var1, BUILTINPOLICY$0, 0, (short)1);
   }

   public BuiltinPolicyType addNewBuiltinPolicy() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         BuiltinPolicyType var2 = null;
         var2 = (BuiltinPolicyType)this.get_store().add_element_user(BUILTINPOLICY$0);
         return var2;
      }
   }
}
