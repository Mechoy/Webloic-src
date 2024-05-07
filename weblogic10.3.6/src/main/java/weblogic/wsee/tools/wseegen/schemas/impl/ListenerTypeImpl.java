package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.sun.java.xml.ns.j2Ee.FullyQualifiedClassType;
import com.sun.java.xml.ns.j2Ee.ParamValueType;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.ListenerType;

public class ListenerTypeImpl extends XmlComplexContentImpl implements ListenerType {
   private static final long serialVersionUID = 1L;
   private static final QName LISTENERCLASS$0 = new QName("http://www.bea.com/wsee/ns/config", "listener-class");
   private static final QName INITPARAM$2 = new QName("http://www.bea.com/wsee/ns/config", "init-param");

   public ListenerTypeImpl(SchemaType var1) {
      super(var1);
   }

   public FullyQualifiedClassType getListenerClass() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         FullyQualifiedClassType var2 = null;
         var2 = (FullyQualifiedClassType)this.get_store().find_element_user(LISTENERCLASS$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setListenerClass(FullyQualifiedClassType var1) {
      this.generatedSetterHelperImpl(var1, LISTENERCLASS$0, 0, (short)1);
   }

   public FullyQualifiedClassType addNewListenerClass() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         FullyQualifiedClassType var2 = null;
         var2 = (FullyQualifiedClassType)this.get_store().add_element_user(LISTENERCLASS$0);
         return var2;
      }
   }

   public ParamValueType[] getInitParamArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(INITPARAM$2, var2);
         ParamValueType[] var3 = new ParamValueType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public ParamValueType getInitParamArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ParamValueType var3 = null;
         var3 = (ParamValueType)this.get_store().find_element_user(INITPARAM$2, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfInitParamArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(INITPARAM$2);
      }
   }

   public void setInitParamArray(ParamValueType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, INITPARAM$2);
   }

   public void setInitParamArray(int var1, ParamValueType var2) {
      this.generatedSetterHelperImpl(var2, INITPARAM$2, var1, (short)2);
   }

   public ParamValueType insertNewInitParam(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ParamValueType var3 = null;
         var3 = (ParamValueType)this.get_store().insert_element_user(INITPARAM$2, var1);
         return var3;
      }
   }

   public ParamValueType addNewInitParam() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ParamValueType var2 = null;
         var2 = (ParamValueType)this.get_store().add_element_user(INITPARAM$2);
         return var2;
      }
   }

   public void removeInitParam(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(INITPARAM$2, var1);
      }
   }
}
