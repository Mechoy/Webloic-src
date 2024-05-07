package weblogic.wsee.sender.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.sender.DefaultProvider.DefaultSendingServiceProvider;
import weblogic.wsee.sender.spi.SendingServiceProvider;

public class SendingServiceFactory {
   static List<SendingServiceProvider> _providerList = new ArrayList();

   public static SendingService getSendingService(Preferences var0) throws SendingServiceException {
      SendingServiceProvider var1 = SendingServiceFactory.Chooser.choose(var0);
      return var1;
   }

   private static class Chooser {
      static SendingServiceProvider choose(Preferences var0) throws SendingServiceException {
         Object var1 = null;
         Iterator var2 = SendingServiceFactory._providerList.iterator();

         while(var2.hasNext()) {
            SendingServiceProvider var3 = (SendingServiceProvider)var2.next();
            if (var3.getPreferences().equals(var0)) {
               var1 = var3;
               break;
            }
         }

         if (var1 == null) {
            var1 = new DefaultSendingServiceProvider(var0);
            SendingServiceFactory._providerList.add(var1);
         }

         return (SendingServiceProvider)var1;
      }
   }
}
