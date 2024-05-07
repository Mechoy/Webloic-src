package weblogic.protocol;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Locale;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServerLogger;
import weblogic.server.ServiceFailureException;
import weblogic.utils.jars.ManifestManager;

public class ProtocolService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      loadProtocol("t3", "weblogic.rjvm.t3.ProtocolHandlerT3", true);
      loadProtocol("http", "weblogic.servlet.internal.ProtocolHandlerHTTP", true);
      loadProtocol("iiop", true);
      loadProtocol("ldap", true);
      loadProtocol("ons", true);
      loadProtocol("cluster", "weblogic.cluster.messaging.protocol.ProtocolHandlerClusterBroadcast", true);
      loadProtocol("snmp", "weblogic.diagnostics.snmp.muxer.ProtocolHandlerSNMP", false);
      loadProtocol("admin", "weblogic.protocol.ProtocolHandlerAdmin");
      ServerLogger.logAdminProtocolConfigured(ProtocolManager.getDefaultAdminProtocol().getAsURLPrefix());
      ManifestManager.getServices(AbstractProtocolService.class);
   }

   public static void loadProtocol(String var0, String var1) {
      try {
         Class var2 = Class.forName(var1);
         Method var3 = var2.getMethod("getProtocolHandler");
         var3.setAccessible(true);
         var3.invoke((Object)null);
         ServerLogger.logProtocolConfigured(var0);
      } catch (Exception var4) {
      }

   }

   public static void loadProtocol(String var0, String var1, boolean var2) {
      loadProtocol(var0, var1);
      if (var2) {
         loadProtocol(var0 + "s", var1 + "S");
      }

   }

   public static void loadProtocol(String var0, boolean var1) {
      loadProtocol(var0, "weblogic." + var0.toLowerCase(Locale.ENGLISH) + ".ProtocolHandler" + var0.toUpperCase(Locale.ENGLISH), var1);
   }

   public static void loadProtocol(String var0) {
      loadProtocol(var0, false);
   }

   public static boolean legalProtocol(String var0) {
      return true;
   }
}
