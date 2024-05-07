package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import com.bea.xml.XmlPositiveInteger;
import java.math.BigInteger;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.MessageAgeDocument;

public class MessageAgeDocumentImpl extends XmlComplexContentImpl implements MessageAgeDocument {
   private static final long serialVersionUID = 1L;
   private static final QName MESSAGEAGE$0 = new QName("http://www.bea.com/wls90/security/policy", "MessageAge");

   public MessageAgeDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public MessageAgeDocument.MessageAge getMessageAge() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         MessageAgeDocument.MessageAge var2 = null;
         var2 = (MessageAgeDocument.MessageAge)this.get_store().find_element_user(MESSAGEAGE$0, 0);
         return var2 == null ? null : var2;
      }
   }

   public void setMessageAge(MessageAgeDocument.MessageAge var1) {
      this.generatedSetterHelperImpl(var1, MESSAGEAGE$0, 0, (short)1);
   }

   public MessageAgeDocument.MessageAge addNewMessageAge() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         MessageAgeDocument.MessageAge var2 = null;
         var2 = (MessageAgeDocument.MessageAge)this.get_store().add_element_user(MESSAGEAGE$0);
         return var2;
      }
   }

   public static class MessageAgeImpl extends XmlComplexContentImpl implements MessageAgeDocument.MessageAge {
      private static final long serialVersionUID = 1L;
      private static final QName AGE$0 = new QName("", "Age");

      public MessageAgeImpl(SchemaType var1) {
         super(var1);
      }

      public BigInteger getAge() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var2 = null;
            var2 = (SimpleValue)this.get_store().find_attribute_user(AGE$0);
            return var2 == null ? null : var2.getBigIntegerValue();
         }
      }

      public XmlPositiveInteger xgetAge() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlPositiveInteger var2 = null;
            var2 = (XmlPositiveInteger)this.get_store().find_attribute_user(AGE$0);
            return var2;
         }
      }

      public boolean isSetAge() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(AGE$0) != null;
         }
      }

      public void setAge(BigInteger var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            SimpleValue var3 = null;
            var3 = (SimpleValue)this.get_store().find_attribute_user(AGE$0);
            if (var3 == null) {
               var3 = (SimpleValue)this.get_store().add_attribute_user(AGE$0);
            }

            var3.setBigIntegerValue(var1);
         }
      }

      public void xsetAge(XmlPositiveInteger var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            XmlPositiveInteger var3 = null;
            var3 = (XmlPositiveInteger)this.get_store().find_attribute_user(AGE$0);
            if (var3 == null) {
               var3 = (XmlPositiveInteger)this.get_store().add_attribute_user(AGE$0);
            }

            var3.set(var1);
         }
      }

      public void unsetAge() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(AGE$0);
         }
      }
   }
}
