package weblogic.wtc.config;

import com.bea.core.jatmi.config.TuxedoConnectorSessionProfile;
import weblogic.wtc.gwt.TDMRemoteTDomain;

public class WTCSGProfile extends ConfigObjectBase implements TuxedoConnectorSessionProfile {
   private String _name;
   private String _cp;
   private String _aclp;
   private String _credp;
   private long _interval;
   private long _maxRetries;
   private int _cmpLimit;
   private int _minEncBits;
   private int _maxEncBits;
   private int _ka;
   private int _kaw;
   private String _security;

   public WTCSGProfile() {
      this.ctype = CONFIG_OBJ_TYPE_SGP;
      this.mtype = MBEAN_OBJ_TYPE_LAP;
   }

   public void setSessionProfileName(String var1) {
      this._name = var1;
   }

   public String getSessionProfileName() {
      return this._name;
   }

   public void setSecurity(String var1) {
      this._security = var1;
   }

   public String getSecurity() {
      return this._security;
   }

   public void setConnectionPolicy(String var1) {
      this._cp = var1;
   }

   public String getConnectionPolicy() {
      return this._cp;
   }

   public void setAclPolicy(String var1) {
      this._aclp = var1;
   }

   public String getAclPolicy() {
      return this._aclp;
   }

   public void setCredentialPolicy(String var1) {
      this._credp = var1;
   }

   public String getCredentialPolicy() {
      return this._credp;
   }

   public void setRetryInterval(long var1) {
      this._interval = var1;
   }

   public long getRetryInterval() {
      return this._interval;
   }

   public void setMaxRetries(long var1) {
      this._maxRetries = var1;
   }

   public long getMaxRetries() {
      return this._maxRetries;
   }

   public void setCmpLimit(int var1) {
      this._cmpLimit = var1;
   }

   public int getCmpLimit() {
      return this._cmpLimit;
   }

   public void setMinEncryptBits(int var1) {
      this._minEncBits = var1;
   }

   public int getMinEncryptBits() {
      return this._minEncBits;
   }

   public void setMaxEncryptBits(int var1) {
      this._maxEncBits = var1;
   }

   public int getMaxEncryptBits() {
      return this._maxEncBits;
   }

   public void setKeepAlive(int var1) {
      this._ka = var1;
   }

   public int getKeepAlive() {
      return this._ka;
   }

   public void setKeepAliveWait(int var1) {
      this._kaw = var1;
   }

   public int getKeepAliveWait() {
      return this._kaw;
   }

   public void fillFromSource(TDMRemoteTDomain var1) {
      this._name = new String("Prof-" + var1.getAccessPoint());
      this._cp = var1.getConnectionPolicy();
      this._aclp = var1.getAclPolicy();
      this._credp = var1.getCredentialPolicy();
      this._interval = var1.getRetryInterval();
      this._maxRetries = var1.getMaxRetries();
      this._cmpLimit = var1.getCmpLimit();
      this._minEncBits = var1.getMinEncryptBits();
      this._maxEncBits = var1.getMaxEncryptBits();
      this._ka = var1.getKeepAlive();
      this._kaw = var1.getKeepAliveWait();
      this._security = var1.getLocalAccessPointObject().getSecurity();
      this.configSource = var1;
   }
}
