package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.XmlComplexContentImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.SimpleValue;
import com.bea.xml.XmlAnyURI;
import com.bea.xml.XmlBoolean;
import javax.xml.namespace.QName;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;

public class SecurityTokenTypeImpl extends XmlComplexContentImpl implements SecurityTokenType {
   private static final long serialVersionUID = 1L;
   private static final QName TOKENTYPE$0 = new QName("", "TokenType");
   private static final QName INCLUDEINMESSAGE$2 = new QName("", "IncludeInMessage");
   private static final QName DERIVEDFROMTOKENTYPE$4 = new QName("", "DerivedFromTokenType");

   public SecurityTokenTypeImpl(SchemaType var1) {
      super(var1);
   }

   public String getTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var2 = null;
         var2 = (SimpleValue)this.get_store().find_attribute_user(TOKENTYPE$0);
         return var2 == null ? null : var2.getStringValue();
      }
   }

   public XmlAnyURI xgetTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlAnyURI var2 = null;
         var2 = (XmlAnyURI)this.get_store().find_attribute_user(TOKENTYPE$0);
         return var2;
      }
   }

   public boolean isSetTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().find_attribute_user(TOKENTYPE$0) != null;
      }
   }

   public void setTokenType(String var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_attribute_user(TOKENTYPE$0);
         if (var3 == null) {
            var3 = (SimpleValue)this.get_store().add_attribute_user(TOKENTYPE$0);
         }

         var3.setStringValue(var1);
      }
   }

   public void xsetTokenType(XmlAnyURI var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlAnyURI var3 = null;
         var3 = (XmlAnyURI)this.get_store().find_attribute_user(TOKENTYPE$0);
         if (var3 == null) {
            var3 = (XmlAnyURI)this.get_store().add_attribute_user(TOKENTYPE$0);
         }

         var3.set(var1);
      }
   }

   public void unsetTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_attribute(TOKENTYPE$0);
      }
   }

   public boolean getIncludeInMessage() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var2 = null;
         var2 = (SimpleValue)this.get_store().find_attribute_user(INCLUDEINMESSAGE$2);
         return var2 == null ? false : var2.getBooleanValue();
      }
   }

   public XmlBoolean xgetIncludeInMessage() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlBoolean var2 = null;
         var2 = (XmlBoolean)this.get_store().find_attribute_user(INCLUDEINMESSAGE$2);
         return var2;
      }
   }

   public boolean isSetIncludeInMessage() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().find_attribute_user(INCLUDEINMESSAGE$2) != null;
      }
   }

   public void setIncludeInMessage(boolean var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_attribute_user(INCLUDEINMESSAGE$2);
         if (var3 == null) {
            var3 = (SimpleValue)this.get_store().add_attribute_user(INCLUDEINMESSAGE$2);
         }

         var3.setBooleanValue(var1);
      }
   }

   public void xsetIncludeInMessage(XmlBoolean var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlBoolean var3 = null;
         var3 = (XmlBoolean)this.get_store().find_attribute_user(INCLUDEINMESSAGE$2);
         if (var3 == null) {
            var3 = (XmlBoolean)this.get_store().add_attribute_user(INCLUDEINMESSAGE$2);
         }

         var3.set(var1);
      }
   }

   public void unsetIncludeInMessage() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_attribute(INCLUDEINMESSAGE$2);
      }
   }

   public String getDerivedFromTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var2 = null;
         var2 = (SimpleValue)this.get_store().find_attribute_user(DERIVEDFROMTOKENTYPE$4);
         return var2 == null ? null : var2.getStringValue();
      }
   }

   public XmlAnyURI xgetDerivedFromTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlAnyURI var2 = null;
         var2 = (XmlAnyURI)this.get_store().find_attribute_user(DERIVEDFROMTOKENTYPE$4);
         return var2;
      }
   }

   public boolean isSetDerivedFromTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         return this.get_store().find_attribute_user(DERIVEDFROMTOKENTYPE$4) != null;
      }
   }

   public void setDerivedFromTokenType(String var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         SimpleValue var3 = null;
         var3 = (SimpleValue)this.get_store().find_attribute_user(DERIVEDFROMTOKENTYPE$4);
         if (var3 == null) {
            var3 = (SimpleValue)this.get_store().add_attribute_user(DERIVEDFROMTOKENTYPE$4);
         }

         var3.setStringValue(var1);
      }
   }

   public void xsetDerivedFromTokenType(XmlAnyURI var1) {
      synchronized(this.monitor()) {
         this.check_orphaned();
         XmlAnyURI var3 = null;
         var3 = (XmlAnyURI)this.get_store().find_attribute_user(DERIVEDFROMTOKENTYPE$4);
         if (var3 == null) {
            var3 = (XmlAnyURI)this.get_store().add_attribute_user(DERIVEDFROMTOKENTYPE$4);
         }

         var3.set(var1);
      }
   }

   public void unsetDerivedFromTokenType() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         this.get_store().remove_attribute(DERIVEDFROMTOKENTYPE$4);
      }
   }
}
