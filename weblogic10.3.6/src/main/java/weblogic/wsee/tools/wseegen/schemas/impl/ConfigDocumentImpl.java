package weblogic.wsee.tools.wseegen.schemas.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.wseegen.schemas.ConfigDocument;
import weblogic.wsee.tools.wseegen.schemas.ConfigType;

public class ConfigDocumentImpl extends XmlComplexContentImpl implements ConfigDocument {
   private static final long serialVersionUID = 1L;
   private static final QName CONFIG$0 = new QName("http://www.bea.com/wsee/ns/config", "config");

   public ConfigDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public ConfigType getConfig() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ConfigType var2 = null;
         var2 = (ConfigType)this.get_store().find_element_user(CONFIG$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setConfig(ConfigType var1) {
      this.generatedSetterHelperImpl(var1, CONFIG$0, 0, (short)1);
   }

   public ConfigType addNewConfig() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         ConfigType var2 = null;
         var2 = (ConfigType)this.get_store().add_element_user(CONFIG$0);
         return var2;
      }
   }
}
