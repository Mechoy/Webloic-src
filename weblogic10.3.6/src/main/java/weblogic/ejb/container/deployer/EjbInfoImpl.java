package weblogic.ejb.container.deployer;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import weblogic.deployment.EjbInfo;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;

public final class EjbInfoImpl implements EjbInfo {
   private BeanInfo bi;
   private SoftReference interfaceNames;

   public EjbInfoImpl(BeanInfo var1) {
      this.bi = var1;
   }

   public String getHomeName() {
      return this.bi instanceof ClientDrivenBeanInfo ? ((ClientDrivenBeanInfo)this.bi).getHomeInterfaceName() : null;
   }

   public String getLocalHomeName() {
      return this.bi instanceof ClientDrivenBeanInfo ? ((ClientDrivenBeanInfo)this.bi).getLocalHomeInterfaceName() : null;
   }

   public String[] getLocalBusinessInterfaceNames() {
      return this.bi instanceof Ejb3SessionBeanInfo ? ((Ejb3SessionBeanInfo)this.bi).getBusinessLocalNames() : new String[0];
   }

   public String[] getRemoteBusinessInterfaceNames() {
      return this.bi instanceof Ejb3SessionBeanInfo ? ((Ejb3SessionBeanInfo)this.bi).getBusinessRemoteNames() : new String[0];
   }

   public String[] getImplementedInterfaceNames() {
      return (String[])((String[])this.getInterfaceNames().toArray(new String[1]));
   }

   public boolean implementsInterface(String var1) {
      return var1 == null ? false : this.getInterfaceNames().contains(var1);
   }

   private Set getInterfaceNames() {
      if (this.interfaceNames != null) {
         Set var1 = (Set)this.interfaceNames.get();
         if (var1 != null) {
            return var1;
         }
      }

      HashSet var4 = new HashSet();
      if (this.bi instanceof ClientDrivenBeanInfo) {
         ClientDrivenBeanInfo var2 = (ClientDrivenBeanInfo)this.bi;
         if (var2.hasDeclaredRemoteHome()) {
            var4.add(var2.getHomeInterfaceName());
         }

         if (var2.hasDeclaredLocalHome()) {
            var4.add(var2.getLocalHomeInterfaceName());
         }

         if (this.bi instanceof Ejb3SessionBeanInfo) {
            Ejb3SessionBeanInfo var3 = (Ejb3SessionBeanInfo)this.bi;
            if (var3.hasBusinessLocals()) {
               var4.addAll(Arrays.asList(var3.getBusinessLocalNames()));
            }

            if (var3.hasBusinessRemotes()) {
               var4.addAll(Arrays.asList(var3.getBusinessRemoteNames()));
            }
         }
      }

      this.interfaceNames = new SoftReference(var4);
      return var4;
   }
}
