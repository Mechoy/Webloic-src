package weblogic.iiop.csi;

import java.io.UnsupportedEncodingException;
import weblogic.corba.cos.security.GSSUtil;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class GSSUPImpl {
   private static final DebugLogger debugIIOPSecurity = DebugLogger.getDebugLogger("DebugIIOPSecurity");
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.iiop.security");
   private String userName;
   private String userNameScope;
   private String password;
   private String targetName;

   public GSSUPImpl(byte[] var1) throws GSSUPDecodeException {
      byte[] var2 = GSSUtil.getGSSUPInnerToken(var1);
      if (var2 == null) {
         throw new GSSUPDecodeException("Invalid or unsupported GSS Token");
      } else {
         IIOPInputStream var3 = new IIOPInputStream(var2);
         var3.consumeEndian();
         byte[] var4 = var3.read_octet_sequence();
         byte[] var5 = var3.read_octet_sequence();
         byte[] var6 = var3.read_octet_sequence();

         try {
            this.userName = new String(var4, "UTF8");
            int var7 = this.userName.lastIndexOf(64);
            if (var7 == 0) {
               this.userName = "";
               this.userNameScope = this.userName.substring(var7 + 1);
            } else if (var7 > 0) {
               if (this.userName.charAt(var7 - 1) != '\\') {
                  this.userNameScope = this.userName.substring(var7 + 1);
                  this.userName = GSSUtil.getUnquotedGSSUserName(this.userName.substring(0, var7));
               } else {
                  this.userName = GSSUtil.getUnquotedGSSUserName(this.userName);
               }
            }

            if (var5 != null && var5.length > 0) {
               this.password = new String(var5, "UTF8");
            } else {
               this.password = "";
            }
         } catch (UnsupportedEncodingException var8) {
            throw new GSSUPDecodeException("Error decoding UTF8 user and password", var8);
         }

         this.targetName = GSSUtil.extractGSSUPGSSNTExportedName(var6);
         if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
            p("created " + this);
         }

      }
   }

   public GSSUPImpl(String var1, String var2, String var3, String var4) {
      this.userName = GSSUtil.getQuotedGSSUserName(var1);
      this.userNameScope = var2;
      this.password = var3;
      this.targetName = var4;
      if (debugIIOPSecurity.isDebugEnabled() || debugSecurity.isEnabled()) {
         p("created from strings " + this);
      }

   }

   public String getUserName() {
      return this.userName;
   }

   public String getUserNameScope() {
      return this.userNameScope;
   }

   public String getPassword() {
      return this.password;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public byte[] getBytes() {
      try {
         String var1 = this.userName;
         if (this.userNameScope != null) {
            var1 = var1 + "@" + this.userNameScope;
         }

         byte[] var2 = var1.getBytes("UTF8");
         byte[] var3 = this.password.getBytes("UTF8");
         byte[] var4 = GSSUtil.createGSSUPGSSNTExportedName(this.targetName);
         IIOPOutputStream var5 = new IIOPOutputStream();
         var5.putEndian();
         var5.write_octet_sequence(var2);
         var5.write_octet_sequence(var3);
         var5.write_octet_sequence(var4);
         byte[] var6 = var5.getBuffer();
         return GSSUtil.getGSSUPToken(var6);
      } catch (UnsupportedEncodingException var7) {
         return null;
      }
   }

   public String toString() {
      return "GSSUPImpl (user= " + this.userName + ", scope=" + this.userNameScope + ", target= " + this.targetName + ")";
   }

   private static void p(String var0) {
      IIOPLogger.logDebugSecurity("<GSSUPImpl>: " + var0);
   }
}
