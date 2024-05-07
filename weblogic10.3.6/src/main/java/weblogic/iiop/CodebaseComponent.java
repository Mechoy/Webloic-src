package weblogic.iiop;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.rjvm.JVMID;
import weblogic.servlet.internal.WebService;
import weblogic.utils.http.HttpParsing;

public final class CodebaseComponent extends TaggedComponent {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private String codebase;
   private String applicationName;
   private ConnectionKey key;
   private ServerIdentity target;

   CodebaseComponent(ServerIdentity var1) {
      this(var1, (String)null);
   }

   CodebaseComponent(ServerIdentity var1, String var2) {
      super(25);
      this.codebase = null;
      this.applicationName = var2;
      ServerChannel var3 = ServerChannelManager.findServerChannel(var1, ProtocolHandlerIIOP.PROTOCOL_IIOP);
      if (var3 != null) {
         this.key = new ConnectionKey(var3.getAddress(), var3.getPort());
      } else {
         this.key = ConnectionKey.NULL_KEY;
      }

      this.target = var1;
   }

   CodebaseComponent(String var1) {
      this(LocalServerIdentity.getIdentity(), var1);
   }

   CodebaseComponent(IIOPInputStream var1) {
      super(25);
      this.codebase = null;
      this.read(var1);
   }

   public String getCodebase() {
      return this.codebase;
   }

   public static String getCodebase(ConnectionKey var0, String var1, boolean var2) {
      StringBuffer var3 = new StringBuffer();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("getCodebase(" + var0 + ", " + var1 + ", " + var2 + ")");
      }

      if (var2) {
         var3.append("https://");
      } else {
         var3.append("http://");
      }

      var3.append(var0.getAddress()).append(":").append(var0.getPort());
      if (!WebService.getInternalWebAppContextPath().startsWith("/")) {
         var3.append('/');
      }

      var3.append(WebService.getInternalWebAppContextPath()).append("/classes").append('/');
      if (var1 != null) {
         var3.append(var1).append('/');
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("getCodebase(" + var1 + ") == " + var3.toString());
      }

      return var3.toString();
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.codebase = HttpParsing.unescape(var1.read_string());
      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      if (this.codebase != null) {
         var1.write_string(HttpParsing.escape(this.codebase));
      } else {
         ServerChannel var4 = var1.getServerChannel();
         if (var4 instanceof JVMID) {
            ServerChannel var5 = (ServerChannel)var4;
            ConnectionKey var6 = new ConnectionKey(var5.getPublicAddress(), var5.getPublicPort());
            var1.write_string(HttpParsing.escape(getCodebase(var6, this.applicationName, var1.isSecure())));
         } else {
            var1.write_string(HttpParsing.escape(getCodebase(this.key.writeReplace(var1, this.target), this.applicationName, var1.isSecure())));
         }
      }

      var1.endEncapsulation(var2);
   }

   protected static void p(String var0) {
      System.err.println("<CodebaseComponent> " + var0);
   }
}
