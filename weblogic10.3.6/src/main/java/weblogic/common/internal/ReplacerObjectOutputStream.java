package weblogic.common.internal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelStream;
import weblogic.utils.io.Replacer;

public final class ReplacerObjectOutputStream extends ObjectOutputStream implements ServerChannelStream {
   private static final boolean DEBUG = false;
   private final Replacer replacer;
   private ServerChannel channel;

   public ReplacerObjectOutputStream(OutputStream var1, Replacer var2) throws IOException {
      super(var1);
      this.enableReplaceObject(var2 != null);
      this.replacer = var2;
   }

   public final void setServerChannel(ServerChannel var1) {
      this.channel = var1;
   }

   public ServerChannel getServerChannel() {
      return this.channel;
   }

   protected Object replaceObject(Object var1) throws IOException {
      Object var2 = this.replacer.replaceObject(var1);
      return var2;
   }

   private static void say(String var0) {
      System.err.println(var0);
   }
}
