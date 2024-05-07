package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PoolParamsMBeanCustomImpl extends XMLElementMBeanDelegate implements PoolParamsMBean {
   private String descrEncoding = null;
   private String descriptorVersion = null;
   private SizeParamsMBean sizeParams;
   private XaParamsMBean xaParams;
   private Integer loginDelay;
   private Integer secondsToTrustAnIdlePoolConnection;
   private Boolean leakProfilingEnabled;
   private ConnectionCheckParamsMBean connCheckParams;
   private Integer xaDebugLevel;
   private Boolean removeConnsEnabled;

   public void setEncoding(String var1) {
      this.descrEncoding = var1;
   }

   public void setVersion(String var1) {
      String var2 = this.descriptorVersion;
      this.descriptorVersion = var1;
      this.checkChange("version", var2, var1);
   }

   public void setSizeParams(SizeParamsMBean var1) {
      this.sizeParams = var1;
   }

   public void setXaParams(XaParamsMBean var1) {
      this.xaParams = var1;
   }

   public void setLoginDelaySeconds(int var1) {
      this.loginDelay = new Integer(var1);
   }

   public void setSecondsToTrustAnIdlePoolConnection(int var1) {
      this.secondsToTrustAnIdlePoolConnection = new Integer(var1);
   }

   public void setLeakProfilingEnabled(boolean var1) {
      this.leakProfilingEnabled = new Boolean(var1);
   }

   public void setConnectionCheckParams(ConnectionCheckParamsMBean var1) {
      this.connCheckParams = var1;
   }

   public void setJDBCXADebugLevel(int var1) {
      Integer var2 = this.xaDebugLevel;
      this.xaDebugLevel = new Integer(var1);
      this.checkChange("JDBCXADebugLevel", var2, this.xaDebugLevel);
   }

   public void setRemoveInfectedConnectionsEnabled(boolean var1) {
      Boolean var2 = this.removeConnsEnabled;
      this.removeConnsEnabled = new Boolean(var1);
      this.checkChange("RemoveInfectedConnectionsEnabled", var2, this.removeConnsEnabled);
   }

   public String getEncoding() {
      return this.descrEncoding;
   }

   public String getVersion() {
      return this.descriptorVersion;
   }

   public SizeParamsMBean getSizeParams() {
      return this.sizeParams;
   }

   public XaParamsMBean getXaParams() {
      return this.xaParams;
   }

   public int getLoginDelaySeconds() {
      return this.loginDelay != null ? this.loginDelay : 0;
   }

   public int getSecondsToTrustAnIdlePoolConnection() {
      return this.secondsToTrustAnIdlePoolConnection != null ? this.secondsToTrustAnIdlePoolConnection : 0;
   }

   public boolean isLeakProfilingEnabled() {
      return this.leakProfilingEnabled != null ? this.leakProfilingEnabled : false;
   }

   public ConnectionCheckParamsMBean getConnectionCheckParams() {
      return this.connCheckParams;
   }

   public int getJDBCXADebugLevel() {
      return this.xaDebugLevel != null ? this.xaDebugLevel : 0;
   }

   public boolean isRemoveInfectedConnectionsEnabled() {
      return this.removeConnsEnabled != null ? this.removeConnsEnabled : true;
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<pool-params");
      var2.append(">\n");
      if (null != this.getSizeParams()) {
         var2.append(this.getSizeParams().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getXaParams()) {
         var2.append(this.getXaParams().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append("<login-delay-seconds>").append(this.getLoginDelaySeconds()).append("</login-delay-seconds>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<secs-to-trust-an-idle-pool-con>").append(this.getSecondsToTrustAnIdlePoolConnection()).append("</secs-to-trust-an-idle-pool-con>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<leak-profiling-enabled>").append(ToXML.capitalize((new Boolean(this.isLeakProfilingEnabled())).toString())).append("</leak-profiling-enabled>\n");
      if (null != this.getConnectionCheckParams()) {
         var2.append(this.getConnectionCheckParams().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append("<jdbcxa-debug-level>").append(this.getJDBCXADebugLevel()).append("</jdbcxa-debug-level>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<remove-infected-connections-enabled>").append(ToXML.capitalize((new Boolean(this.isRemoveInfectedConnectionsEnabled())).toString())).append("</remove-infected-connections-enabled>\n");
      var2.append(ToXML.indent(var1)).append("</pool-params>\n");
      return var2.toString();
   }
}
