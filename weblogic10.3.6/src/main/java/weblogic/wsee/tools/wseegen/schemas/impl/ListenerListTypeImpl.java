package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.ListenerListType;
import weblogic.wsee.tools.wseegen.schemas.ListenerType;

public class ListenerListTypeImpl extends XmlComplexContentImpl implements ListenerListType {
   private static final long serialVersionUID = 1L;
   private static final QName LISTENER$0 = new QName("http://www.bea.com/wsee/ns/config", "listener");

   public ListenerListTypeImpl(SchemaType var1) {
      super(var1);
   }

   public ListenerType[] getListenerArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(LISTENER$0, var2);
         ListenerType[] var3 = new ListenerType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public ListenerType getListenerArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerType var3 = null;
         var3 = (ListenerType)this.get_store().find_element_user(LISTENER$0, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfListenerArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(LISTENER$0);
      }
   }

   public void setListenerArray(ListenerType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, LISTENER$0);
   }

   public void setListenerArray(int var1, ListenerType var2) {
      this.generatedSetterHelperImpl(var2, LISTENER$0, var1, (short)2);
   }

   public ListenerType insertNewListener(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerType var3 = null;
         var3 = (ListenerType)this.get_store().insert_element_user(LISTENER$0, var1);
         return var3;
      }
   }

   public ListenerType addNewListener() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ListenerType var2 = null;
         var2 = (ListenerType)this.get_store().add_element_user(LISTENER$0);
         return var2;
      }
   }

   public void removeListener(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(LISTENER$0, var1);
      }
   }
}
