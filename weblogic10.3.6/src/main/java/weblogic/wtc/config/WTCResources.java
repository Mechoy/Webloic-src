package weblogic.wtc.config;

import com.bea.core.jatmi.config.TuxedoConnectorResources;
import weblogic.wtc.gwt.WTCService;

public class WTCResources extends ConfigObjectBase implements TuxedoConnectorResources {
   private String _name;
   private String[] _fldtbl16;
   private String[] _fldtbl32;
   private String[] _viewtbl16;
   private String[] _viewtbl32;
   private String _pwd;
   private String _tpusrfile;
   private String _encoding;
   private String _mapfile;

   public WTCResources() {
      this.ctype = CONFIG_OBJ_TYPE_RES;
      this.mtype = MBEAN_OBJ_TYPE_RES;
      this._name = WTCService.getWTCServerName();
   }

   public void setTuxedoConnectorName(String var1) {
   }

   public String getTuxedoConnectorName() {
      return this._name;
   }

   public void setFldTbl16Classes(String[] var1) {
      this._fldtbl16 = var1;
   }

   public String[] getFldTbl16Classes() {
      return this._fldtbl16;
   }

   public void setFldTbl32Classes(String[] var1) {
      this._fldtbl32 = var1;
   }

   public String[] getFldTbl32Classes() {
      return this._fldtbl32;
   }

   public void setViewTbl16Classes(String[] var1) {
      this._viewtbl16 = var1;
   }

   public String[] getViewTbl16Classes() {
      return this._viewtbl16;
   }

   public void setViewTbl32Classes(String[] var1) {
      this._viewtbl32 = var1;
   }

   public String[] getViewTbl32Classes() {
      return this._viewtbl32;
   }

   public void setAppPassword(String var1) {
      this._pwd = var1;
   }

   public String getAppPassword() {
      return this._pwd;
   }

   public void setTpUsrFile(String var1) {
      this._tpusrfile = var1;
   }

   public String getTpUsrFile() {
      return this._tpusrfile;
   }

   public void setRemoteMBEncoding(String var1) {
      this._encoding = var1;
   }

   public String getRemoteMBEncoding() {
      return this._encoding;
   }

   public void setMBEncodingMapFile(String var1) {
      this._mapfile = var1;
   }

   public String getMBEncodingMapFile() {
      return this._mapfile;
   }

   public void copyResources(WTCResources var1) {
      var1.setFldTbl16Classes(this._fldtbl16);
      var1.setFldTbl32Classes(this._fldtbl32);
      var1.setViewTbl16Classes(this._viewtbl16);
      var1.setViewTbl32Classes(this._viewtbl32);
      var1.setAppPassword(this._pwd);
      var1.setTpUsrFile(this._tpusrfile);
      var1.setRemoteMBEncoding(this._encoding);
      var1.setMBEncodingMapFile(this._mapfile);
      var1.setConfigSource(this.configSource);
      this.configSource.changeConfigObj(this, var1);
   }
}
