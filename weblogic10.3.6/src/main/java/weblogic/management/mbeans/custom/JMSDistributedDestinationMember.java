package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public abstract class JMSDistributedDestinationMember extends ConfigurationMBeanCustomizer {
   private DistributedDestinationMemberBean delegate;

   public JMSDistributedDestinationMember(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DistributedDestinationMemberBean var1) {
      this.delegate = var1;
   }

   public int getWeight() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Weight");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 1;
      } else {
         return this.delegate.getWeight();
      }
   }

   public void setWeight(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Weight", new Integer(var1));
      } else {
         this.delegate.setWeight(var1);
      }

   }

   public String getNotes() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Notes");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getNotes();
      }
   }

   public void setNotes(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Notes", var1);
      } else {
         this.delegate.setNotes(var1);
      }

   }
}
