package weblogic.wsee.reliability2.exception;

import weblogic.wsee.reliability.WsrmConstants;

public class WsrmUnsupportedConfigurationException extends WsrmException {
   private final WsrmConstants.RMVersion _rmVersion;
   private final String _sequenceId;
   private final boolean _clientSide;

   public WsrmUnsupportedConfigurationException(WsrmConstants.RMVersion var1, String var2, boolean var3, String var4) {
      super(var4);

      assert var1 != null;

      this._rmVersion = var1;
      this._sequenceId = var2;
      this._clientSide = var3;
   }

   public WsrmUnsupportedConfigurationException(WsrmConstants.RMVersion var1, String var2, boolean var3, String var4, Throwable var5) {
      super(var4, var5);

      assert var1 != null;

      this._rmVersion = var1;
      this._sequenceId = var2;
      this._clientSide = var3;
   }

   public WsrmUnsupportedConfigurationException(WsrmConstants.RMVersion var1, String var2, boolean var3, Throwable var4) {
      super(var4);

      assert var1 != null;

      this._rmVersion = var1;
      this._sequenceId = var2;
      this._clientSide = var3;
   }

   public WsrmConstants.RMVersion getRMVersion() {
      return this._rmVersion;
   }

   public boolean isClient() {
      return this._clientSide;
   }

   public String getSequenceId() {
      return this._sequenceId;
   }
}
