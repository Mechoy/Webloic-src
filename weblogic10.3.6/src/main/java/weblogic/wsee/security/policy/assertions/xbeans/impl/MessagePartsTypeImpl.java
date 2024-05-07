package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.JavaStringHolderEx;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.DialectType;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;

public class MessagePartsTypeImpl extends JavaStringHolderEx implements MessagePartsType {
   private static final long serialVersionUID = 1L;
   private static final QName DIALECT$0 = new QName("", "Dialect");

   public MessagePartsTypeImpl(SchemaType var1) {
      super(var1, true);
   }

   protected MessagePartsTypeImpl(SchemaType var1, boolean var2) {
      super(var1, var2);
   }

   public String getDialect() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var2 = null;
         var2 = (SimpleValue)this.get_store().find_attribute_user(DIALECT$0);
         return var2 == null ? null : var2.getStringValue();
      }
   }

   public DialectType xgetDialect() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         DialectType var2 = null;
         var2 = (DialectType)this.get_store().find_attribute_user(DIALECT$0);
         return var2;
      }
   }

   public boolean isSetDialect() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().find_attribute_user(DIALECT$0) != null;
      }
   }

   public void setDialect(String var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_attribute_user(DIALECT$0);
         if (var3 == null) {
            var3 = (SimpleValue)this.get_store().add_attribute_user(DIALECT$0);
         }

         var3.setStringValue(var1);
      }
   }

   public void xsetDialect(DialectType var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         DialectType var3 = null;
         var3 = (DialectType)this.get_store().find_attribute_user(DIALECT$0);
         if (var3 == null) {
            var3 = (DialectType)this.get_store().add_attribute_user(DIALECT$0);
         }

         var3.set(var1);
      }
   }

   public void unsetDialect() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_attribute(DIALECT$0);
      }
   }
}
