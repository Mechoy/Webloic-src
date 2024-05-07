package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.sun.java.xml.ns.j2Ee.PortComponentHandlerType;
import com.sun.java.xml.ns.j2Ee.String;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.HandlerChainType;

public class HandlerChainTypeImpl extends XmlComplexContentImpl implements HandlerChainType {
   private static final long serialVersionUID = 1L;
   private static final QName HANDLERCHAINNAME$0 = new QName("http://www.bea.com/xml/ns/jws", "handler-chain-name");
   private static final QName HANDLER$2 = new QName("http://www.bea.com/xml/ns/jws", "handler");

   public HandlerChainTypeImpl(SchemaType var1) {
      super(var1);
   }

   public String getHandlerChainName() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         String var2 = null;
         var2 = (String)this.get_store().find_element_user(HANDLERCHAINNAME$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setHandlerChainName(String var1) {
      this.generatedSetterHelperImpl(var1, HANDLERCHAINNAME$0, 0, (short)1);
   }

   public String addNewHandlerChainName() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         String var2 = null;
         var2 = (String)this.get_store().add_element_user(HANDLERCHAINNAME$0);
         return var2;
      }
   }

   public PortComponentHandlerType[] getHandlerArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ArrayList var2 = new ArrayList();
         this.get_store().find_all_element_users(HANDLER$2, var2);
         PortComponentHandlerType[] var3 = new PortComponentHandlerType[var2.size()];
         var2.toArray(var3);
         return var3;
      }
   }

   public PortComponentHandlerType getHandlerArray(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         PortComponentHandlerType var3 = null;
         var3 = (PortComponentHandlerType)this.get_store().find_element_user(HANDLER$2, var1);
         if (var3 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return var3;
         }
      }
   }

   public int sizeOfHandlerArray() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().count_elements(HANDLER$2);
      }
   }

   public void setHandlerArray(PortComponentHandlerType[] var1) {
      this.check_orphaned();
      this.arraySetterHelper(var1, HANDLER$2);
   }

   public void setHandlerArray(int var1, PortComponentHandlerType var2) {
      this.generatedSetterHelperImpl(var2, HANDLER$2, var1, (short)2);
   }

   public PortComponentHandlerType insertNewHandler(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         PortComponentHandlerType var3 = null;
         var3 = (PortComponentHandlerType)this.get_store().insert_element_user(HANDLER$2, var1);
         return var3;
      }
   }

   public PortComponentHandlerType addNewHandler() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         PortComponentHandlerType var2 = null;
         var2 = (PortComponentHandlerType)this.get_store().add_element_user(HANDLER$2);
         return var2;
      }
   }

   public void removeHandler(int var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_element(HANDLER$2, var1);
      }
   }
}
