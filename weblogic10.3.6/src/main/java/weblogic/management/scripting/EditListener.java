package weblogic.management.scripting;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import weblogic.management.RemoteNotificationListener;
import weblogic.management.scripting.utils.WLSTInterpreter;
import weblogic.management.scripting.utils.WLSTUtil;

public class EditListener implements RemoteNotificationListener {
   public void handleNotification(Notification notification, Object handback) {
      WLSTInterpreter interp = WLSTUtil.getWLSTInterpreter();
      if (interp != null) {
         WLScriptContext ctx = null;

         try {
            ctx = (WLScriptContext)interp.get("WLS_ON", WLScriptContext.class);
         } catch (NullPointerException var7) {
            return;
         }

         if (ctx.isEditSessionInProgress) {
            AttributeChangeNotification notif = (AttributeChangeNotification)notification;
            String newUser = null;
            if (notif.getNewValue() != null) {
               newUser = (String)notif.getNewValue();
               if (!newUser.equals(new String(ctx.username_bytes)) || notif.getNewValue() == null) {
                  ctx.resetEditSession();
                  ctx.println(ctx.getWLSTMsgFormatter().getEditSessionTerminated());
                  interp.exec("updatePrompt()");
               }
            }

         }
      }
   }
}
