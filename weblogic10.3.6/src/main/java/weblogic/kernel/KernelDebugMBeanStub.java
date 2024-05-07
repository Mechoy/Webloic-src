package weblogic.kernel;

import weblogic.management.configuration.DebugScopeMBean;
import weblogic.management.configuration.KernelDebugMBean;

final class KernelDebugMBeanStub extends MBeanStub implements KernelDebugMBean {
   private boolean abbreviation;
   private boolean connection;
   private boolean messaging;
   private boolean routing;
   private boolean loadbalancing;
   private boolean failover;
   private boolean forceGCeachDGC;
   private boolean DGCEnrollment;
   private boolean DGCStatistics;
   private boolean ssl;
   private boolean rc4;
   private boolean rsa;
   private boolean iiop;
   private boolean muxer;
   private boolean muxerDetail;
   private boolean muxerTimeout;
   private boolean muxerConn;
   private boolean muxerException;
   private boolean workContext;
   private boolean iiopTransport;
   private boolean iiopMarshal;
   private boolean iiopSecurity;
   private boolean iiopOTS;
   private boolean iiopReplacer;
   private boolean iiopConnection;
   private boolean iiopStartup;
   private boolean selfTuning;

   KernelDebugMBeanStub() {
      this.initializeFromSystemProperties("weblogic.debug.");
   }

   public boolean getDebugAbbreviation() {
      return this.abbreviation;
   }

   public void setDebugAbbreviation(boolean var1) {
      this.abbreviation = var1;
   }

   public boolean getDebugConnection() {
      return this.connection;
   }

   public void setDebugConnection(boolean var1) {
      this.connection = var1;
   }

   public boolean getDebugMessaging() {
      return this.messaging;
   }

   public void setDebugMessaging(boolean var1) {
      this.messaging = var1;
   }

   public boolean getDebugRouting() {
      return this.routing;
   }

   public void setDebugRouting(boolean var1) {
      this.routing = var1;
   }

   public boolean getDebugWorkContext() {
      return this.workContext;
   }

   public void setDebugWorkContext(boolean var1) {
      this.workContext = var1;
   }

   public boolean getDebugLoadBalancing() {
      return this.loadbalancing;
   }

   public void setDebugLoadBalancing(boolean var1) {
      this.loadbalancing = var1;
   }

   public boolean getDebugFailOver() {
      return this.failover;
   }

   public void setDebugFailOver(boolean var1) {
      this.failover = var1;
   }

   public boolean getForceGCEachDGCPeriod() {
      return this.forceGCeachDGC;
   }

   public void setForceGCEachDGCPeriod() {
   }

   public void setForceGCEachDGCPeriod(boolean var1) {
      this.forceGCeachDGC = var1;
   }

   public boolean getDebugDGCEnrollment() {
      return this.DGCEnrollment;
   }

   public void setDebugDGCEnrollment() {
   }

   public void setDebugDGCEnrollment(boolean var1) {
      this.DGCEnrollment = var1;
   }

   public boolean getLogDGCStatistics() {
      return this.DGCStatistics;
   }

   public void setLogDGCStatistics(boolean var1) {
      this.DGCStatistics = var1;
   }

   public boolean getDebugSSL() {
      return this.ssl;
   }

   public void setDebugSSL(boolean var1) {
      this.ssl = var1;
   }

   public boolean getDebugRC4() {
      return this.rc4;
   }

   public void setDebugRC4(boolean var1) {
      this.rc4 = var1;
   }

   public boolean getDebugRSA() {
      return this.rsa;
   }

   public void setDebugRSA(boolean var1) {
      this.rsa = var1;
   }

   public boolean getDebugIIOP() {
      return this.iiop;
   }

   public void setDebugIIOP(boolean var1) {
      this.iiop = var1;
   }

   public boolean getDebugMuxer() {
      return this.muxer;
   }

   public void setDebugMuxer(boolean var1) {
      this.muxer = var1;
   }

   public boolean getDebugMuxerTimeout() {
      return this.muxerTimeout;
   }

   public void setDebugMuxerTimeout(boolean var1) {
      this.muxerTimeout = var1;
   }

   public boolean getDebugMuxerDetail() {
      return this.muxerDetail;
   }

   public void setDebugMuxerDetail(boolean var1) {
      this.muxerDetail = var1;
   }

   public boolean getDebugMuxerConnection() {
      return this.muxerConn;
   }

   public void setDebugMuxerConnection(boolean var1) {
      this.muxerConn = var1;
   }

   public boolean getDebugMuxerException() {
      return this.muxerException;
   }

   public void setDebugMuxerException(boolean var1) {
      this.muxerException = var1;
   }

   public boolean getDebugIIOPTransport() {
      return this.iiopTransport;
   }

   public void setDebugIIOPTransport(boolean var1) {
      this.iiopTransport = var1;
   }

   public boolean getDebugIIOPMarshal() {
      return this.iiopMarshal;
   }

   public void setDebugIIOPMarshal(boolean var1) {
      this.iiopMarshal = var1;
   }

   public boolean getDebugIIOPSecurity() {
      return this.iiopSecurity;
   }

   public void setDebugIIOPSecurity(boolean var1) {
      this.iiopSecurity = var1;
   }

   public boolean getDebugIIOPOTS() {
      return this.iiopOTS;
   }

   public void setDebugIIOPOTS(boolean var1) {
      this.iiopOTS = var1;
   }

   public boolean getDebugIIOPReplacer() {
      return this.iiopReplacer;
   }

   public void setDebugIIOPReplacer(boolean var1) {
      this.iiopReplacer = var1;
   }

   public boolean getDebugIIOPConnection() {
      return this.iiopConnection;
   }

   public void setDebugIIOPConnection(boolean var1) {
      this.iiopConnection = var1;
   }

   public boolean getDebugIIOPStartup() {
      return this.iiopStartup;
   }

   public void setDebugIIOPStartup(boolean var1) {
      this.iiopStartup = var1;
   }

   public boolean getDebugSelfTuning() {
      return this.selfTuning;
   }

   public void setDebugSelfTuning(boolean var1) {
      this.selfTuning = var1;
   }

   public DebugScopeMBean[] getDebugScopes() {
      return null;
   }

   public DebugScopeMBean createDebugScope(String var1) {
      return null;
   }

   public void destroyDebugScope(DebugScopeMBean var1) {
   }

   public DebugScopeMBean lookupDebugScope(String var1) {
      return null;
   }
}
