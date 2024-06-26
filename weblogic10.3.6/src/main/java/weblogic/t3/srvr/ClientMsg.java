package weblogic.t3.srvr;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.Manufacturable;

public final class ClientMsg implements Manufacturable, Externalizable {
   private static final long serialVersionUID = -8434373145364482668L;
   public boolean verbose = false;
   public byte cmd = 0;
   public int hardDisconnectTimeoutMins = -2;
   public int softDisconnectTimeoutMins = -2;
   public int idleSoftDisconnectTimeoutMins = -2;
   public int notifyChannel = -1;
   public String serverName;
   public String wsName;
   public String reason;
   public static final byte CMD_NONE = 0;
   public static final byte CMD_SET = 1;
   public static final byte CMD_GET = 6;
   public static final byte CMD_DISCONNECTED = 8;
   public static final byte CMD_NOTIFYCHANNEL = 9;
   public static final String[] CMD_TEXT = new String[]{"NONE", "SET", "LOG", "STORE", "FETCH", "REMOVE", "GET", "GETPROP", "DISCONNECTED", "NOTIFYCHANNEL"};
   public static final int DISCONNECT_MIN = -2;
   public static final int DISCONNECT_TIMEOUT_DEFAULT = -2;
   public static final int DISCONNECT_TIMEOUT_NEVER = -1;

   public void initialize() {
   }

   public void destroy() {
   }

   public String toString() {
      String var1 = "[CliMsg: Cmd=" + CMD_TEXT[this.cmd] + " ";
      switch (this.cmd) {
         case 1:
         case 6:
            return var1 + "n/h/s/i/v=" + this.wsName + "/" + this.hardDisconnectTimeoutMins + "/" + this.softDisconnectTimeoutMins + "/" + this.idleSoftDisconnectTimeoutMins + "/" + this.verbose + "]";
         default:
            return var1 + "]";
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeByte(this.cmd);
      switch (this.cmd) {
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         default:
            throw new IOException("No such cmd: " + this.cmd);
         case 8:
            var2.writeString(this.reason);
         case 1:
         case 6:
            var2.writeInt(this.hardDisconnectTimeoutMins);
            var2.writeInt(this.softDisconnectTimeoutMins);
            var2.writeInt(this.idleSoftDisconnectTimeoutMins);
            var2.writeString(this.wsName);
            var2.writeBoolean(this.verbose);
            var2.writeString(this.serverName);
            break;
         case 9:
            var2.writeInt(this.notifyChannel);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.cmd = var2.readByte();
      switch (this.cmd) {
         case 2:
         case 3:
         case 4:
         case 5:
         case 7:
         default:
            throw new IOException("No such cmd: " + this.cmd);
         case 8:
            this.reason = var2.readString();
         case 1:
         case 6:
            this.hardDisconnectTimeoutMins = var2.readInt();
            this.softDisconnectTimeoutMins = var2.readInt();
            this.idleSoftDisconnectTimeoutMins = var2.readInt();
            this.wsName = var2.readString();
            this.verbose = var2.readBoolean();
            this.serverName = var2.readString();
            break;
         case 9:
            this.notifyChannel = var2.readInt();
      }

   }
}
