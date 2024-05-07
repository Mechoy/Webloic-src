package weblogic.wsee.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.OwsmSecurityPolicyRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WseeBaseRuntimeMBean;
import weblogic.management.runtime.WseePolicyRuntimeMBean;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.servlet.internal.WebAppRuntimeMBeanImpl;
import weblogic.t3.srvr.ServerRuntime;
import weblogic.wsee.deploy.VersioningHelper;
import weblogic.wsee.policy.mbean.OwsmSecurityPolicyRuntimeMBeanImpl;
import weblogic.wsee.policy.mbean.WseePolicyRuntimeMBeanImpl;

abstract class WseeBaseRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeBaseRuntimeMBean, WseeRuntimeData> implements WseeBaseRuntimeMBean {
   private Set<WseePortRuntimeMBeanImpl> ports;
   private WseePolicyRuntimeMBeanImpl wprm;
   private OwsmSecurityPolicyRuntimeMBeanImpl owsmSecPolicy;
   protected String serviceName;

   public WseeBaseRuntimeMBeanImpl() throws ManagementException {
      super((String)null, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
      this.ports = new HashSet();
      this.wprm = null;
      this.owsmSecPolicy = null;
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   protected WseeBaseRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeBaseRuntimeMBeanImpl var3) throws ManagementException {
      super(var1, var2, var3, false);
      this.ports = new HashSet();
      this.wprm = null;
      this.owsmSecPolicy = null;
   }

   WseeBaseRuntimeMBeanImpl(String var1, RuntimeMBean var2, String var3, String var4, String var5, String var6, String var7, String var8) throws ManagementException {
      this(var1, var2, var3, var4, var5, var6, var7, var8, true);
   }

   protected WseeBaseRuntimeMBeanImpl(String var1, RuntimeMBean var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      this.ports = new HashSet();
      this.wprm = null;
      this.owsmSecPolicy = null;
      if (var9 && !(var2 instanceof ComponentRuntimeMBean) && !(var2 instanceof J2EEApplicationRuntimeMBeanImpl)) {
         throw new IllegalArgumentException("Attempt to parent a WseeV2RuntimeMBean off something other than a component runtime: " + var2);
      } else {
         WseeRuntimeData var10 = new WseeRuntimeData(var1, var3, var4, var5, var6, var7);
         this.setData(var10);
         this.serviceName = var8;
         this.setWprm(new WseePolicyRuntimeMBeanImpl(this.getName(), this));
         this.setOwsmSecPolicy(new OwsmSecurityPolicyRuntimeMBeanImpl(this.getName(), this));
         if (var9) {
            if (var2 instanceof WebAppComponentRuntimeMBean) {
               ((WebAppRuntimeMBeanImpl)var2).addWseeV2Runtime((WseeV2RuntimeMBean)this);
            } else if (var2 instanceof EJBComponentRuntimeMBean) {
               ((EJBComponentRuntimeMBeanImpl)var2).addWseeV2Runtime((WseeV2RuntimeMBean)this);
            } else if (var2 instanceof J2EEApplicationRuntimeMBeanImpl) {
               ((J2EEApplicationRuntimeMBeanImpl)var2).addWseeV2Runtime((WseeV2RuntimeMBean)this);
            }
         }

         this.register();
      }
   }

   protected WseeBaseRuntimeMBeanImpl internalInitProxy(WseeBaseRuntimeMBeanImpl var1) throws ManagementException {
      var1.setData(this.getData());
      var1.setWprm((WseePolicyRuntimeMBeanImpl)this.wprm.createProxy(this.wprm.getName(), var1));
      var1.setOwsmSecPolicy((OwsmSecurityPolicyRuntimeMBeanImpl)this.owsmSecPolicy.createProxy(this.owsmSecPolicy.getName(), var1));
      Iterator var2 = this.ports.iterator();

      while(var2.hasNext()) {
         WseePortRuntimeMBeanImpl var3 = (WseePortRuntimeMBeanImpl)var2.next();
         WseePortRuntimeMBeanImpl var4 = (WseePortRuntimeMBeanImpl)var3.createProxy(var3.getName(), var1);
         var1.addPort(var4);
      }

      return var1;
   }

   protected void setWprm(WseePolicyRuntimeMBeanImpl var1) {
      this.wprm = var1;
      ((WseeRuntimeData)this.getData()).setPolicyRuntime((WseePolicyRuntimeData)var1.getData());
   }

   protected void setOwsmSecPolicy(OwsmSecurityPolicyRuntimeMBeanImpl var1) {
      this.owsmSecPolicy = var1;
      ((WseeRuntimeData)this.getData()).setOwsmSecurityPolicyRuntime((OwsmSecurityPolicyRuntimeData)var1.getData());
   }

   public String getURI() {
      return ((WseeRuntimeData)this.getData()).getURI();
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public WseePortRuntimeMBean[] getPorts() {
      return (WseePortRuntimeMBean[])this.ports.toArray(new WseePortRuntimeMBean[this.ports.size()]);
   }

   public WseePolicyRuntimeMBean getPolicyRuntime() {
      return this.wprm;
   }

   public long getConversationInstanceCount() {
      return ((WseeRuntimeData)this.getData()).getConversationInstanceCount();
   }

   public String getImplementationType() {
      return ((WseeRuntimeData)this.getData()).getImplementationType();
   }

   public void setWebserviceDescriptrionName(String var1) {
      ((WseeRuntimeData)this.getData()).setWebserviceDescriptrionName(var1);
   }

   public String getWebserviceDescriptionName() {
      return ((WseeRuntimeData)this.getData()).getWebserviceDescriptionName();
   }

   public void addPort(WseePortRuntimeMBean var1) {
      Iterator var2 = this.ports.iterator();

      WseePortRuntimeMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            ((WseeRuntimeData)this.getData()).addPort((WseePortRuntimeData)((WseePortRuntimeMBeanImpl)var1).getData());
            this.ports.add((WseePortRuntimeMBeanImpl)var1);

            try {
               var1.setParent(this);
               if (this.isRegistered()) {
                  ((WseePortRuntimeMBeanImpl)var1).register();
               }

               return;
            } catch (Exception var4) {
               throw new RuntimeException(var4.toString(), var4);
            }
         }

         var3 = (WseePortRuntimeMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1.getName()));

   }

   public OwsmSecurityPolicyRuntimeMBean getOwsmSecurityPolicyRuntime() {
      return this.owsmSecPolicy;
   }

   public int getPolicyFaults() {
      return ((WseeRuntimeData)this.getData()).getPolicyFaults();
   }

   public long getStartTime() {
      return ((WseeRuntimeData)this.getData()).getStartTime();
   }

   /** @deprecated */
   @Deprecated
   public int getTotalFaults() {
      return ((WseeRuntimeData)this.getData()).getTotalFaults();
   }

   public int getTotalSecurityFaults() {
      return ((WseeRuntimeData)this.getData()).getTotalSecurityFaults();
   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         super.register();
         this.wprm.register();
         this.owsmSecPolicy.register();
         if (!this.isProxy()) {
            ((WseeRuntimeData)this.getData()).setStartTime(System.currentTimeMillis());
         }

         Iterator var1 = this.ports.iterator();

         while(var1.hasNext()) {
            WseePortRuntimeMBeanImpl var2 = (WseePortRuntimeMBeanImpl)var1.next();
            var2.setParent(this);
            var2.register();
         }

      }
   }

   public void unregister() throws ManagementException {
      super.unregister();
      WseeRuntimeMBeanManager.remove(this.getServiceName());
      this.unregisterPorts();
      this.unregisterWprm();
      this.unregisterOwsmSecPolicy();
      if (!this.isProxy()) {
         ((WseeRuntimeData)this.getData()).setStartTime(0L);
         if (((WseeRuntimeData)this.getData()).getVersion() != null) {
            VersioningHelper.removeRecord(((WseeRuntimeData)this.getData()).getAppName(), ((WseeRuntimeData)this.getData()).getVersion());
         }
      }

      ServerRuntime.theOne().removeChild(this);
   }

   private void unregisterPorts() {
      Iterator var1 = this.ports.iterator();

      while(var1.hasNext()) {
         WseePortRuntimeMBeanImpl var2 = (WseePortRuntimeMBeanImpl)var1.next();

         try {
            var2.unregister();
            ServerRuntime.theOne().removeChild(var2);
         } catch (ManagementException var4) {
            var4.printStackTrace();
         }
      }

      this.ports.clear();
      ((WseeRuntimeData)this.getData()).clearPorts();
   }

   private void unregisterWprm() {
      if (this.wprm != null) {
         try {
            this.wprm.unregister();
            ServerRuntime.theOne().removeChild(this.wprm);
         } catch (ManagementException var2) {
            var2.printStackTrace();
            this.wprm = null;
            ((WseeRuntimeData)this.getData()).clearPolicyRuntime();
         }
      }

      this.wprm = null;
      ((WseeRuntimeData)this.getData()).clearPolicyRuntime();
   }

   private void unregisterOwsmSecPolicy() {
      if (this.owsmSecPolicy != null) {
         try {
            this.owsmSecPolicy.unregister();
            ServerRuntime.theOne().removeChild(this.owsmSecPolicy);
         } catch (ManagementException var2) {
            var2.printStackTrace();
            this.owsmSecPolicy = null;
            ((WseeRuntimeData)this.getData()).clearOwsmSecurityPolicyRuntime();
         }
      }

      this.owsmSecPolicy = null;
      ((WseeRuntimeData)this.getData()).clearOwsmSecurityPolicyRuntime();
   }

   public WseeBaseRuntimeMBean.Type getWsType() {
      return ((WseeRuntimeData)this.getData()).getImplementationType().startsWith(WseeBaseRuntimeMBean.Type.JAXRPC.toString()) ? WseeBaseRuntimeMBean.Type.JAXRPC : WseeBaseRuntimeMBean.Type.JAXWS;
   }
}
