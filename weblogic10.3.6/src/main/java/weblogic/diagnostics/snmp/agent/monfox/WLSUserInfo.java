package weblogic.diagnostics.snmp.agent.monfox;

import java.security.NoSuchAlgorithmException;
import monfox.toolkit.snmp.engine.SnmpEngineID;
import monfox.toolkit.snmp.v3.usm.USMUtil;
import monfox.toolkit.snmp.v3.usm.ext.UsmUserSecurityExtension;
import weblogic.diagnostics.debug.DebugLogger;

public class WLSUserInfo implements UsmUserSecurityExtension.UserInfo {
   private SnmpEngineID engineID;
   private String userName;
   private int secLevel;
   private int authProtocol;
   private int privProtocol;
   private byte[] localizedAuthKey;
   private byte[] localizedPrivKey;
   private boolean nonExistentUser;
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPAgent");

   public WLSUserInfo(SnmpEngineID var1, String var2, int var3, int var4, int var5, byte[] var6, byte[] var7, boolean var8) throws NoSuchAlgorithmException {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Creating WLSUserInfo: user_name=" + var2 + " sec_level=" + var3 + " auth_protocol=" + var4 + " priv_protocol=" + var5);
      }

      this.engineID = var1;
      this.userName = var2;
      this.secLevel = var3;
      this.authProtocol = var4;
      this.privProtocol = var5;
      this.nonExistentUser = var8;
      byte[] var9;
      if (var6 != null && var6.length > 0) {
         var9 = USMUtil.generateKey(var6, var4);
         this.localizedAuthKey = USMUtil.localizeKey(var9, var1.toByteArray(), var4);
      }

      if (var7 != null && var7.length > 0) {
         var9 = USMUtil.generateKey(var7, var4);
         this.localizedPrivKey = USMUtil.localizeKey(var9, var1.toByteArray(), var4);
      }

   }

   public String toString() {
      return this.userName;
   }

   public SnmpEngineID getEngineID() {
      return this.engineID;
   }

   public String getUserName() {
      return this.userName;
   }

   public byte[] getLocalizedAuthKey() {
      return this.localizedAuthKey;
   }

   public byte[] getLocalizedPrivKey() {
      return this.localizedPrivKey;
   }

   public int getAuthProtocol() {
      return this.authProtocol;
   }

   public int getPrivProtocol() {
      return this.privProtocol;
   }

   public int getSecLevel() {
      return this.secLevel;
   }

   public boolean isNonExistentUser() {
      return this.nonExistentUser;
   }
}
