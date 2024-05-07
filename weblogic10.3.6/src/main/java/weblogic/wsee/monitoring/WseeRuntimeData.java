package weblogic.wsee.monitoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.deploy.VersioningHelper;

public final class WseeRuntimeData extends WseeBaseRuntimeData {
   private String contextPath = null;
   private String URI = null;
   private String appName = null;
   private String version = null;
   private String implementationType = null;
   private String webserviceDescriptionName = null;
   private Set<WseePortRuntimeData> ports = new HashSet();
   private WseePolicyRuntimeData wprd = null;
   private OwsmSecurityPolicyRuntimeData owsmSecPolicy = null;
   private long startTime = 0L;

   WseeRuntimeData(String var1, String var2, String var3, String var4, String var5, String var6) {
      super(var1, (WseeBaseRuntimeData)null);
      this.contextPath = var2;
      this.URI = var2 != null ? var2 + var3 : var3;
      this.version = var5;
      this.appName = var4;
      this.implementationType = var6;
   }

   public String getContextPath() {
      return this.contextPath;
   }

   public String getURI() {
      return this.URI;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getVersion() {
      return this.version;
   }

   public WseePortRuntimeData[] getPorts() {
      return (WseePortRuntimeData[])this.ports.toArray(new WseePortRuntimeData[this.ports.size()]);
   }

   public void clearPorts() {
      this.ports = null;
   }

   void setPolicyRuntime(WseePolicyRuntimeData var1) {
      this.wprd = var1;
   }

   public WseePolicyRuntimeData getPolicyRuntime() {
      return this.wprd;
   }

   public void clearPolicyRuntime() {
      this.wprd = null;
   }

   public long getConversationInstanceCount() {
      return VersioningHelper.getCount(this.appName, this.version);
   }

   public String getImplementationType() {
      return this.implementationType;
   }

   public void setWebserviceDescriptrionName(String var1) {
      this.webserviceDescriptionName = var1;
   }

   public String getWebserviceDescriptionName() {
      return this.webserviceDescriptionName;
   }

   public boolean addPort(WseePortRuntimeData var1) {
      assert var1 != null;

      Iterator var2 = this.ports.iterator();

      WseePortRuntimeData var3;
      do {
         if (!var2.hasNext()) {
            var1.setParentData(this);
            this.ports.add(var1);
            return true;
         }

         var3 = (WseePortRuntimeData)var2.next();
      } while(!var3.getName().equals(var1.getName()));

      return false;
   }

   void setOwsmSecurityPolicyRuntime(OwsmSecurityPolicyRuntimeData var1) {
      this.owsmSecPolicy = var1;
   }

   public OwsmSecurityPolicyRuntimeData getOwsmSecurityPolicyRuntime() {
      return this.owsmSecPolicy;
   }

   public void clearOwsmSecurityPolicyRuntime() {
      this.owsmSecPolicy = null;
   }

   public int getPolicyFaults() {
      int var1 = 0;
      WseePortRuntimeData[] var2 = this.getPorts();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WseePortRuntimeData var5 = var2[var4];
         var1 += var5.getPolicyFaults();
      }

      return var1;
   }

   public long getStartTime() {
      return this.startTime;
   }

   /** @deprecated */
   @Deprecated
   public int getTotalFaults() {
      int var1 = 0;
      WseePortRuntimeData[] var2 = this.getPorts();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WseePortRuntimeData var5 = var2[var4];
         var1 += var5.getTotalFaults();
      }

      return var1;
   }

   public int getTotalSecurityFaults() {
      int var1 = 0;
      WseePortRuntimeData[] var2 = this.getPorts();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WseePortRuntimeData var5 = var2[var4];
         var1 += var5.getTotalSecurityFaults();
      }

      return var1;
   }

   public void setURI(String var1) {
      this.URI = var1;
   }

   public void setAppName(String var1) {
      this.appName = var1;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public void setImplementationType(String var1) {
      this.implementationType = var1;
   }

   public void setWebserviceDescriptionName(String var1) {
      this.webserviceDescriptionName = var1;
   }

   public void setStartTime(long var1) {
      this.startTime = var1;
   }
}
