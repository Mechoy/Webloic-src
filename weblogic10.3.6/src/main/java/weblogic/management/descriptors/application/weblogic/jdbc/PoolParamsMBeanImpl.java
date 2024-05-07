package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PoolParamsMBeanImpl extends XMLElementMBeanDelegate implements PoolParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_secondsToTrustAnIdlePoolConnection = false;
   private int secondsToTrustAnIdlePoolConnection;
   private boolean isSet_connectionCheckParams = false;
   private ConnectionCheckParamsMBean connectionCheckParams;
   private boolean isSet_leakProfilingEnabled = false;
   private boolean leakProfilingEnabled;
   private boolean isSet_sizeParams = false;
   private SizeParamsMBean sizeParams;
   private boolean isSet_loginDelaySeconds = false;
   private int loginDelaySeconds;
   private boolean isSet_removeInfectedConnectionsEnabled = false;
   private boolean removeInfectedConnectionsEnabled;
   private boolean isSet_xaParams = false;
   private XaParamsMBean xaParams;
   private boolean isSet_jdbcxaDebugLevel = false;
   private int jdbcxaDebugLevel;

   public int getSecondsToTrustAnIdlePoolConnection() {
      return this.secondsToTrustAnIdlePoolConnection;
   }

   public void setSecondsToTrustAnIdlePoolConnection(int var1) {
      int var2 = this.secondsToTrustAnIdlePoolConnection;
      this.secondsToTrustAnIdlePoolConnection = var1;
      this.isSet_secondsToTrustAnIdlePoolConnection = var1 != -1;
      this.checkChange("secondsToTrustAnIdlePoolConnection", var2, this.secondsToTrustAnIdlePoolConnection);
   }

   public ConnectionCheckParamsMBean getConnectionCheckParams() {
      return this.connectionCheckParams;
   }

   public void setConnectionCheckParams(ConnectionCheckParamsMBean var1) {
      ConnectionCheckParamsMBean var2 = this.connectionCheckParams;
      this.connectionCheckParams = var1;
      this.isSet_connectionCheckParams = var1 != null;
      this.checkChange("connectionCheckParams", var2, this.connectionCheckParams);
   }

   public boolean isLeakProfilingEnabled() {
      return this.leakProfilingEnabled;
   }

   public void setLeakProfilingEnabled(boolean var1) {
      boolean var2 = this.leakProfilingEnabled;
      this.leakProfilingEnabled = var1;
      this.isSet_leakProfilingEnabled = true;
      this.checkChange("leakProfilingEnabled", var2, this.leakProfilingEnabled);
   }

   public SizeParamsMBean getSizeParams() {
      return this.sizeParams;
   }

   public void setSizeParams(SizeParamsMBean var1) {
      SizeParamsMBean var2 = this.sizeParams;
      this.sizeParams = var1;
      this.isSet_sizeParams = var1 != null;
      this.checkChange("sizeParams", var2, this.sizeParams);
   }

   public int getLoginDelaySeconds() {
      return this.loginDelaySeconds;
   }

   public void setLoginDelaySeconds(int var1) {
      int var2 = this.loginDelaySeconds;
      this.loginDelaySeconds = var1;
      this.isSet_loginDelaySeconds = var1 != -1;
      this.checkChange("loginDelaySeconds", var2, this.loginDelaySeconds);
   }

   public boolean isRemoveInfectedConnectionsEnabled() {
      return this.removeInfectedConnectionsEnabled;
   }

   public void setRemoveInfectedConnectionsEnabled(boolean var1) {
      boolean var2 = this.removeInfectedConnectionsEnabled;
      this.removeInfectedConnectionsEnabled = var1;
      this.isSet_removeInfectedConnectionsEnabled = true;
      this.checkChange("removeInfectedConnectionsEnabled", var2, this.removeInfectedConnectionsEnabled);
   }

   public XaParamsMBean getXaParams() {
      return this.xaParams;
   }

   public void setXaParams(XaParamsMBean var1) {
      XaParamsMBean var2 = this.xaParams;
      this.xaParams = var1;
      this.isSet_xaParams = var1 != null;
      this.checkChange("xaParams", var2, this.xaParams);
   }

   public int getJDBCXADebugLevel() {
      return this.jdbcxaDebugLevel;
   }

   public void setJDBCXADebugLevel(int var1) {
      int var2 = this.jdbcxaDebugLevel;
      this.jdbcxaDebugLevel = var1;
      this.isSet_jdbcxaDebugLevel = var1 != -1;
      this.checkChange("jdbcxaDebugLevel", var2, this.jdbcxaDebugLevel);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<pool-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</pool-params>\n");
      return var2.toString();
   }
}
