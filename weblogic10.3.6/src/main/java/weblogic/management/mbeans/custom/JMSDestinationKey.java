package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class JMSDestinationKey extends ConfigurationMBeanCustomizer {
   private transient DestinationKeyBean delegate;

   public JMSDestinationKey(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DestinationKeyBean var1) {
      this.delegate = var1;
   }

   public String getProperty() {
      if (this.delegate != null && this.delegate.isSet("Property")) {
         return this.delegate.getProperty();
      } else {
         Object var1 = this.getValue("Property");
         return var1 != null && var1 instanceof String ? (String)var1 : "JMSMessageID";
      }
   }

   public void setProperty(String var1) {
      this.putValue("Property", var1);
      if (this.delegate != null) {
         this.delegate.setProperty(var1);
      }

   }

   public String getKeyType() {
      if (this.delegate != null && this.delegate.isSet("KeyType")) {
         return this.delegate.getKeyType();
      } else {
         Object var1 = this.getValue("KeyType");
         return var1 != null && var1 instanceof String ? (String)var1 : "String";
      }
   }

   public void setKeyType(String var1) {
      this.putValue("KeyType", var1);
      if (this.delegate != null) {
         this.delegate.setKeyType(var1);
      }

   }

   public String getDirection() {
      if (this.delegate != null && this.delegate.isSet("SortOrder")) {
         return this.delegate.getSortOrder();
      } else {
         Object var1 = this.getValue("Direction");
         return var1 != null && var1 instanceof String ? (String)var1 : "Ascending";
      }
   }

   public void setDirection(String var1) {
      this.putValue("Direction", var1);
      if (this.delegate != null) {
         this.delegate.setSortOrder(var1);
      }

   }

   public String getNotes() {
      if (this.delegate != null && this.delegate.isSet("Notes")) {
         return this.delegate.getNotes();
      } else {
         Object var1 = this.getValue("Notes");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      }
   }

   public void setNotes(String var1) {
      this.putValue("Notes", var1);
      if (this.delegate != null) {
         this.delegate.setNotes(var1);
      }

   }
}
