package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import com.bea.xml.XmlAnyURI;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.TransformType;

public class TransformTypeImpl extends XmlComplexContentImpl implements TransformType {
   private static final long serialVersionUID = 1L;
   private static final QName URI$0 = new QName("", "URI");

   public TransformTypeImpl(SchemaType var1) {
      super(var1);
   }

   public String getURI() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var2 = null;
         var2 = (SimpleValue)this.get_store().find_attribute_user(URI$0);
         return var2 == null ? null : var2.getStringValue();
      }
   }

   public XmlAnyURI xgetURI() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlAnyURI var2 = null;
         var2 = (XmlAnyURI)this.get_store().find_attribute_user(URI$0);
         return var2;
      }
   }

   public void setURI(String var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_attribute_user(URI$0);
         if (var3 == null) {
            var3 = (SimpleValue)this.get_store().add_attribute_user(URI$0);
         }

         var3.setStringValue(var1);
      }
   }

   public void xsetURI(XmlAnyURI var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlAnyURI var3 = null;
         var3 = (XmlAnyURI)this.get_store().find_attribute_user(URI$0);
         if (var3 == null) {
            var3 = (XmlAnyURI)this.get_store().add_attribute_user(URI$0);
         }

         var3.set(var1);
      }
   }
}
