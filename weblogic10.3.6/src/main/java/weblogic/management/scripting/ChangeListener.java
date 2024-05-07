package weblogic.management.scripting;

import java.util.EmptyStackException;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.ObjectName;
import weblogic.management.RemoteNotificationListener;
import weblogic.management.scripting.utils.WLSTInterpreter;
import weblogic.management.scripting.utils.WLSTUtil;

public class ChangeListener implements RemoteNotificationListener {
   public void handleNotification(Notification notification, Object handback) {
      WLSTInterpreter interp = WLSTUtil.getWLSTInterpreter();
      if (interp != null) {
         WLScriptContext ctx = null;

         try {
            ctx = (WLScriptContext)interp.get("WLS_ON", WLScriptContext.class);
         } catch (NullPointerException var12) {
            return;
         }

         if (!ctx.atBeanLevel) {
            MBeanServerNotification serverNotification = null;
            ObjectName wlsObjectName = null;
            if (notification instanceof MBeanServerNotification) {
               serverNotification = (MBeanServerNotification)notification;

               try {
                  wlsObjectName = serverNotification.getMBeanName();
                  if (ctx.inNewTree() && !wlsObjectName.getDomain().equals("com.bea")) {
                     return;
                  }

                  if (!ctx.inNewTree() && wlsObjectName.getDomain().equals("com.bea")) {
                     return;
                  }
               } catch (Exception var13) {
                  return;
               }

               boolean isRegister = serverNotification.getType().equals("JMX.mbean.registered");
               String mbeanType = null;

               try {
                  mbeanType = (String)ctx.prompts.peek();
               } catch (EmptyStackException var11) {
                  return;
               }

               mbeanType = ctx.getRightType(mbeanType);

               try {
                  if (mbeanType.equals(wlsObjectName.getKeyProperty("Type"))) {
                     if (isRegister) {
                        if (ctx.inMBeanType) {
                           ctx.setInstanceObjectName(wlsObjectName);
                        } else if (ctx.inMBeanTypes) {
                           ctx.addInstanceObjectName(wlsObjectName);
                        }
                     } else if (ctx.inMBeanType) {
                        ctx.setInstanceObjectName((ObjectName)null);
                     } else if (ctx.inMBeanTypes) {
                        ctx.removeInstanceObjectName(wlsObjectName);
                     }
                  }
               } catch (ArrayIndexOutOfBoundsException var10) {
               }
            }

         }
      }
   }
}
