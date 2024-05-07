package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public final class JMSTopic extends JMSDestination {
   private static final long serialVersionUID = 6209479225384067069L;
   private long _creationTime = 1L;
   private transient TopicBean delegate;

   public JMSTopic(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setCreationTime(long var1) {
      this._creationTime = var1;
   }

   public long getCreationTime() {
      return this._creationTime;
   }

   public void useDelegates(DomainMBean var1, JMSBean var2, TopicBean var3) {
      this.delegate = var3;
      super.useDelegates(var1, var2, this.delegate);
   }

   public String getMulticastAddress() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MulticastAddress");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getMulticast().getMulticastAddress();
      }
   }

   public void setMulticastAddress(String var1) {
      if (this.delegate == null) {
         this.putValue("MulticastAddress", var1);
      } else {
         this.delegate.getMulticast().setMulticastAddress(var1);
      }

   }

   public int getMulticastTTL() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MulticastTTL");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 1;
      } else {
         return this.delegate.getMulticast().getMulticastTimeToLive();
      }
   }

   public void setMulticastTTL(int var1) {
      if (this.delegate == null) {
         this.putValue("MulticastTTL", new Integer(var1));
      } else {
         this.delegate.getMulticast().setMulticastTimeToLive(var1);
      }

   }

   public int getMulticastPort() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MulticastPort");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 6001;
      } else {
         return this.delegate.getMulticast().getMulticastPort();
      }
   }

   public void setMulticastPort(int var1) {
      if (this.delegate == null) {
         this.putValue("MulticastPort", new Integer(var1));
      } else {
         this.delegate.getMulticast().setMulticastPort(var1);
      }

   }
}
