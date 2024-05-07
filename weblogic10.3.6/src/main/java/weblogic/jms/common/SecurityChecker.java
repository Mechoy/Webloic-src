package weblogic.jms.common;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.jms.JMSService;
import weblogic.security.SubjectUtils;
import weblogic.security.service.JMSResource;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;

public final class SecurityChecker implements TimerListener {
   private JMSService jmsService;
   private ConcurrentHashMap<TimedSecurityParticipant, JMSResource> checkMe;

   public SecurityChecker(JMSService var1) {
      this.jmsService = var1;
      this.checkMe = new ConcurrentHashMap();
   }

   public void registerWithChecker(JMSResource var1, TimedSecurityParticipant var2) {
      synchronized(this.checkMe) {
         if (this.checkMe.size() <= 0) {
            this.jmsService.fireUpSecurityChecks();
         }
      }

      this.checkMe.put(var2, var1);
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("Registered " + JMSSecurityHelper.getSimpleAuthenticatedName() + " for security checks on " + var1);
      }

   }

   public void timerExpired(Timer var1) {
      Iterator var2 = this.checkMe.keySet().iterator();

      while(true) {
         while(var2.hasNext()) {
            TimedSecurityParticipant var3 = (TimedSecurityParticipant)var2.next();
            if (var3.isClosed()) {
               if (JMSDebug.JMSCommon.isDebugEnabled()) {
                  JMSDebug.JMSCommon.debug("Removing closed participant " + var3);
               }

               this.checkMe.remove(var3);
            } else {
               JMSResource var4 = (JMSResource)this.checkMe.get(var3);

               try {
                  JMSSecurityHelper.checkPermission(var4, var3.getSubject());
               } catch (JMSSecurityException var8) {
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("Security lapsed for " + var4 + SubjectUtils.getUsername(var3.getSubject()));
                  }

                  var3.securityLapsed();
               }
            }
         }

         synchronized(this.checkMe) {
            if (this.checkMe.size() <= 0) {
               this.jmsService.stopSecurityChecks();
               return;
            }

            return;
         }
      }
   }
}
