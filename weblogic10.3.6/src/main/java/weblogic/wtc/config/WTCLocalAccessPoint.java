package weblogic.wtc.config;

import com.bea.core.jatmi.config.TuxedoConnectorLAP;
import weblogic.wtc.gwt.TDMLocalTDomain;
import weblogic.wtc.jatmi.TPException;

public class WTCLocalAccessPoint extends ConfigObjectBase implements TuxedoConnectorLAP {
   private String _ap;
   private String _apId;
   private String[] _addr;
   private String _sec;
   private long _blktime;
   private String _interop;
   private String[] _parsedEPG;

   public WTCLocalAccessPoint() {
      this.ctype = CONFIG_OBJ_TYPE_LAP;
      this.mtype = MBEAN_OBJ_TYPE_LAP;
   }

   public void setAccessPoint(String var1) {
      this._ap = var1;
   }

   public String getAccessPoint() {
      return this._ap;
   }

   public void setAccessPointId(String var1) {
      this._apId = var1;
   }

   public String getAccessPointId() {
      return this._apId;
   }

   public void setNetworkAddr(String[] var1) throws TPException {
      this._addr = var1;
   }

   public String[] getNetworkAddr() {
      return this._addr;
   }

   public void setSecurity(String var1) {
      this._sec = var1;
   }

   public String getSecurity() {
      return this._sec;
   }

   public void setBlockTime(long var1) {
      this._blktime = var1;
   }

   public long getBlockTime() {
      return this._blktime;
   }

   public void setInteroperate(String var1) {
      this._interop = var1;
   }

   public String getInteroperate() {
      return this._interop;
   }

   public void setEndPointGroup(String[] var1) {
      this._parsedEPG = var1;
   }

   public String[] getEndPointGroup() {
      return this._parsedEPG;
   }

   public void fillFromSource(TDMLocalTDomain var1) {
      this._ap = var1.getAccessPoint();
      this._apId = var1.getAccessPointId();
      this._sec = var1.getSecurity();
      this._blktime = var1.getBlockTime();
      this._interop = var1.getInteroperate();
      this.configSource = var1;
   }
}
