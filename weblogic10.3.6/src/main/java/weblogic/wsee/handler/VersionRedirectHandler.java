package weblogic.wsee.handler;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.wsee.conversation.ConversationCMPHandler;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.VersionRedirectUtil;

public class VersionRedirectHandler extends ConversationCMPHandler implements WLHandler {
   private static final boolean verbose = Verbose.isVerbose(VersionRedirectHandler.class);
   public static final String VERSION_REDIRECT_DATA_PROP = "weblogic.wsee.version.redirect.data.prop";
   public static final String VERSION_REDIRECT_RESPONSE_PROP = "weblogic.wsee.version.redirect.response.prop";
   public static final String REDIRECTED_REQUEST = "weblogic.wsee.util.VersionRedirectUtil.redirected";
   public static final String APP_VERSIONID_PROP = "weblogic.wsee.version.appversion.id";

   public boolean handleRequest(MessageContext var1) {
      if (verbose) {
         Verbose.log((Object)"handleRequest");
      }

      WlMessageContext var2 = WlMessageContext.narrow(var1);
      String var3 = ConversationUtils.getConversationAppVersion(var2);
      if (var3 == null) {
         return true;
      } else {
         String var4 = ApplicationVersionUtils.getCurrentVersionId();
         if (var4 != null && !var3.equals(var4)) {
            if (verbose) {
               Verbose.log((Object)("Got request for version " + var3 + " my version = " + var4));
               Verbose.log((Object)"Redirecting request");
            }

            try {
               VersionRedirectUtil.redirect(var2, var3);
            } catch (Throwable var10) {
               if (verbose) {
                  Verbose.logException(var10);
               }

               throw new JAXRPCException(var10);
            } finally {
               return false;
            }

            return false;
         } else {
            return true;
         }
      }
   }

   public boolean handleResponse(MessageContext var1) {
      return true;
   }

   public boolean handleClosure(MessageContext var1) {
      return true;
   }

   public boolean handleFault(MessageContext var1) {
      return true;
   }
}
