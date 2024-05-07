package weblogic.kernel;

import weblogic.management.configuration.IIOPMBean;

final class IIOPMBeanStub extends MBeanStub implements IIOPMBean {
   private int defaultGIOPMinorVersion = 2;
   private String iiopLocationForwardPolicy = "failover";
   private boolean useIIOPLocateRequest = false;
   private boolean useFullRepositoyIdList = false;
   private String iiopTxMechanism = "ots";
   private int idleIIOPConnectionTimeout = 240000;
   private String charCodeset = "US-ASCII";
   private String wcharCodeset = "UCS-2";
   public String systemSecurity = "none";
   private boolean statefulAuthentication = true;
   private boolean useSFV2 = false;
   private boolean enableIORServlet = false;
   private boolean useJavaSerialization = false;

   IIOPMBeanStub() {
      this.initializeFromSystemProperties("weblogic.iiop.");
   }

   public int getMaxMessageSize() {
      return 10000000;
   }

   public void setMaxMessageSize(int var1) {
   }

   public int getCompleteMessageTimeout() {
      return 60;
   }

   public void setCompleteMessageTimeout(int var1) {
   }

   public int getDefaultMinorVersion() {
      return this.defaultGIOPMinorVersion;
   }

   public void setDefaultMinorVersion(int var1) {
      this.defaultGIOPMinorVersion = var1;
   }

   public String getLocationForwardPolicy() {
      return this.iiopLocationForwardPolicy;
   }

   public void setLocationForwardPolicy(String var1) {
      this.iiopLocationForwardPolicy = var1;
   }

   public boolean getUseLocateRequest() {
      return this.useIIOPLocateRequest;
   }

   public void setUseLocateRequest(boolean var1) {
      this.useIIOPLocateRequest = var1;
   }

   public boolean getUseFullRepositoryIdList() {
      return this.useFullRepositoyIdList;
   }

   public void setUseFullRepositoryIdList(boolean var1) {
      this.useFullRepositoyIdList = var1;
   }

   public String getTxMechanism() {
      return this.iiopTxMechanism;
   }

   public void setTxMechanism(String var1) {
      this.iiopTxMechanism = var1;
   }

   public int getIdleConnectionTimeout() {
      return this.idleIIOPConnectionTimeout / 1000;
   }

   public void setIdleConnectionTimeout(int var1) {
      this.idleIIOPConnectionTimeout = var1 * 1000;
   }

   public String getDefaultCharCodeset() {
      return this.charCodeset;
   }

   public void setDefaultCharCodeset(String var1) {
      this.charCodeset = var1;
   }

   public String getDefaultWideCharCodeset() {
      return this.wcharCodeset;
   }

   public void setDefaultWideCharCodeset(String var1) {
      this.wcharCodeset = var1;
   }

   public String getSystemSecurity() {
      return this.systemSecurity;
   }

   public void setSystemSecurity(String var1) {
      this.systemSecurity = var1;
   }

   public boolean getUseStatefulAuthentication() {
      return this.statefulAuthentication;
   }

   public void setUseStatefulAuthentication(boolean var1) {
      this.statefulAuthentication = var1;
   }

   public boolean getUseSerialFormatVersion2() {
      return this.useSFV2;
   }

   public void setUseSerialFormatVersion2(boolean var1) {
      this.useSFV2 = var1;
   }

   public boolean getEnableIORServlet() {
      return this.enableIORServlet;
   }

   public void setEnableIORServlet(boolean var1) {
      this.enableIORServlet = var1;
   }

   public boolean getUseJavaSerialization() {
      return this.useJavaSerialization;
   }

   public void setUseJavaSerialization(boolean var1) {
      this.useJavaSerialization = var1;
   }
}
