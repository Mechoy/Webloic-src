package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.HandlerConfig;
import weblogic.wsee.tools.wseegen.schemas.HandlerConfigType;

public class HandlerConfigImpl extends XmlComplexContentImpl implements HandlerConfig {
   private static final long serialVersionUID = 1L;
   private static final QName HANDLERCONFIG$0 = new QName("http://www.bea.com/xml/ns/jws", "handler-config");

   public HandlerConfigImpl(SchemaType var1) {
      super(var1);
   }

   public HandlerConfigType getHandlerConfig() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         HandlerConfigType var2 = null;
         var2 = (HandlerConfigType)this.get_store().find_element_user(HANDLERCONFIG$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setHandlerConfig(HandlerConfigType var1) {
      this.generatedSetterHelperImpl(var1, HANDLERCONFIG$0, 0, (short)1);
   }

   public HandlerConfigType addNewHandlerConfig() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         HandlerConfigType var2 = null;
         var2 = (HandlerConfigType)this.get_store().add_element_user(HANDLERCONFIG$0);
         return var2;
      }
   }
}
