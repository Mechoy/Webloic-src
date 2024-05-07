package weblogic.wsee.policy.runtime.schema.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import com.bea.xml.XmlString;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.runtime.schema.BuiltinPolicyType;
import weblogic.wsee.policy.runtime.schema.CategoryEnum;

public class BuiltinPolicyTypeImpl extends XmlComplexContentImpl implements BuiltinPolicyType {
   private static final long serialVersionUID = 1L;
   private static final QName CATEGORY$0 = new QName("http://www.oracle.com/weblogic/wsee/policy/runtime/schema", "Category");
   private static final QName POLICYNAME$2 = new QName("", "PolicyName");

   public BuiltinPolicyTypeImpl(SchemaType var1) {
      super(var1);
   }

   public CategoryEnum.Enum[] getCategoryArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(CATEGORY$0, var2);
         CategoryEnum.Enum[] var3 = new CategoryEnum.Enum[var2.size()];
         int var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            var3[var4] = (CategoryEnum.Enum)((SimpleValue)var2.get(var4)).getEnumValue();
         }

         return var3;
      }
   }

   public CategoryEnum.Enum getCategoryArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_element_user(CATEGORY$0, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return (CategoryEnum.Enum)var3.getEnumValue();
         }
      }
   }

   public CategoryEnum[] xgetCategoryArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(CATEGORY$0, var2);
         CategoryEnum[] var3 = new CategoryEnum[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public CategoryEnum xgetCategoryArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         CategoryEnum var3 = null;
         var3 = (CategoryEnum)this.get_store().find_element_user(CATEGORY$0, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfCategoryArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(CATEGORY$0);
      }
   }

   public void setCategoryArray(CategoryEnum.Enum[] var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.arraySetterHelper(var1, CATEGORY$0);
      }
   }

   public void setCategoryArray(int var1, CategoryEnum.Enum var2) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var4 = null;
         var4 = (SimpleValue)this.get_store().find_element_user(CATEGORY$0, var1);
         if (var4 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            var4.setEnumValue(var2);
         }
      }
   }

   public void xsetCategoryArray(CategoryEnum[] var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.arraySetterHelper(var1, CATEGORY$0);
      }
   }

   public void xsetCategoryArray(int var1, CategoryEnum var2) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         CategoryEnum var4 = null;
         var4 = (CategoryEnum)this.get_store().find_element_user(CATEGORY$0, var1);
         if (var4 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            var4.set(var2);
         }
      }
   }

   public void insertCategory(int var1, CategoryEnum.Enum var2) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var4 = (SimpleValue)this.get_store().insert_element_user(CATEGORY$0, var1);
         var4.setEnumValue(var2);
      }
   }

   public void addCategory(CategoryEnum.Enum var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().add_element_user(CATEGORY$0);
         var3.setEnumValue(var1);
      }
   }

   public CategoryEnum insertNewCategory(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         CategoryEnum var3 = null;
         var3 = (CategoryEnum)this.get_store().insert_element_user(CATEGORY$0, var1);
         return var3;
      }
   }

   public CategoryEnum addNewCategory() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         CategoryEnum var2 = null;
         var2 = (CategoryEnum)this.get_store().add_element_user(CATEGORY$0);
         return var2;
      }
   }

   public void removeCategory(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(CATEGORY$0, var1);
      }
   }

   public String getPolicyName() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var2 = null;
         var2 = (SimpleValue)this.get_store().find_attribute_user(POLICYNAME$2);
         return var2 == null ? null : var2.getStringValue();
      }
   }

   public XmlString xgetPolicyName() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlString var2 = null;
         var2 = (XmlString)this.get_store().find_attribute_user(POLICYNAME$2);
         return var2;
      }
   }

   public void setPolicyName(String var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_attribute_user(POLICYNAME$2);
         if (var3 == null) {
            var3 = (SimpleValue)this.get_store().add_attribute_user(POLICYNAME$2);
         }

         var3.setStringValue(var1);
      }
   }

   public void xsetPolicyName(XmlString var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlString var3 = null;
         var3 = (XmlString)this.get_store().find_attribute_user(POLICYNAME$2);
         if (var3 == null) {
            var3 = (XmlString)this.get_store().add_attribute_user(POLICYNAME$2);
         }

         var3.set(var1);
      }
   }
}
