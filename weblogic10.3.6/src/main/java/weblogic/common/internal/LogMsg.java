package weblogic.common.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;

public final class LogMsg implements Externalizable {
   private static final long serialVersionUID = -4898439307567022541L;
   public static final String PROXYCLASS = "weblogic.common.internal.LogProxy";
   public static final String PROXYLAZYCLASS = "weblogic.common.internal.LogProxyLazy";
   private static boolean verbose = false;
   public byte cmd;
   public String logmsg;
   public String exception;
   public static final byte INFO = 0;
   public static final byte ERROR = 1;
   public static final byte WARNING = 2;
   public static final byte SECURITY = 3;
   public static final byte DEBUG = 4;

   public String toString() {
      return "LogMsg " + getCmdVerb(this.cmd) + " - " + this.logmsg;
   }

   private static String getCmdVerb(byte var0) {
      switch (var0) {
         case 0:
            return "INFO";
         case 1:
            return "ERROR";
         case 2:
            return "WARNING";
         case 3:
            return "SECURITY";
         case 4:
            return "DEBUG";
         default:
            return (new Integer(var0)).toString();
      }
   }

   public void initialize() {
   }

   public void destroy() {
   }

   public LogMsg() {
   }

   public LogMsg(byte var1) {
      this.cmd = var1;
   }

   public LogMsg(byte var1, String var2) {
      this(var1);
      this.logmsg = var2;
   }

   public LogMsg(byte var1, String var2, String var3) {
      this(var1, var2);
      this.exception = var3;
   }

   public void readExternal(ObjectInput var1) throws IOException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.cmd = var2.readByte();
      this.logmsg = var2.readString();
      this.exception = var2.readString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeByte(this.cmd);
      var2.writeString(this.logmsg);
      var2.writeString(this.exception);
   }
}
