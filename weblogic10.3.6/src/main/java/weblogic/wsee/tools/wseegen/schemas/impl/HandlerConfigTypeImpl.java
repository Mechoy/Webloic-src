package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.HandlerChainType;
import weblogic.wsee.tools.wseegen.schemas.HandlerConfigType;

public class HandlerConfigTypeImpl extends XmlComplexContentImpl implements HandlerConfigType {
   private static final long serialVersionUID = 1L;
   private static final QName HANDLERCHAIN$0 = new QName("http://www.bea.com/xml/ns/jws", "handler-chain");

   public HandlerConfigTypeImpl(SchemaType var1) {
      super(var1);
   }

   public HandlerChainType[] getHandlerChainArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(HANDLERCHAIN$0, var2);
         HandlerChainType[] var3 = new HandlerChainType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public HandlerChainType getHandlerChainArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         HandlerChainType var3 = null;
         var3 = (HandlerChainType)this.get_store().find_element_user(HANDLERCHAIN$0, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfHandlerChainArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(HANDLERCHAIN$0);
      }
   }

   public void setHandlerChainArray(HandlerChainType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, HANDLERCHAIN$0);
   }

   public void setHandlerChainArray(int var1, HandlerChainType var2) {
      this.generatedSetterHelperImpl(var2, HANDLERCHAIN$0, var1, (short)2);
   }

   public HandlerChainType insertNewHandlerChain(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         HandlerChainType var3 = null;
         var3 = (HandlerChainType)this.get_store().insert_element_user(HANDLERCHAIN$0, var1);
         return var3;
      }
   }

   public HandlerChainType addNewHandlerChain() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         HandlerChainType var2 = null;
         var2 = (HandlerChainType)this.get_store().add_element_user(HANDLERCHAIN$0);
         return var2;
      }
   }

   public void removeHandlerChain(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(HANDLERCHAIN$0, var1);
      }
   }
}
