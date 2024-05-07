package weblogic.jms.backend;

import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDestinationSecurity;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.JMSResource;

public class BEDestinationSecurityImpl implements JMSDestinationSecurity {
   private JMSResource jmsResource_send = null;
   private JMSResource jmsResource_receive = null;
   private JMSResource jmsResource_browse = null;

   public BEDestinationSecurityImpl(EntityName var1, String var2) {
      if (var1 != null) {
         String var3 = var1.getApplicationName();
         String var4 = var1.getEntityName();
         if (var3 != null && var3.equals("interop-jms")) {
            var3 = null;
         }

         if (var4 != null && var4.indexOf("@") >= 0) {
            var4 = var4.substring(var4.indexOf("@") + 1);
         }

         if (var3 != null && var3.equals("interop-jms")) {
            var3 = null;
         }

         this.jmsResource_send = new JMSResource(var3, var1.getEARModuleName(), var2, var4, "send");
         this.jmsResource_receive = new JMSResource(var3, var1.getEARModuleName(), var2, var4, "receive");
         this.jmsResource_browse = new JMSResource(var3, var1.getEARModuleName(), var2, var4, "browse");
      }
   }

   public void checkSendPermission(AuthenticatedSubject var1) throws JMSSecurityException {
      JMSSecurityHelper.checkPermission(this.jmsResource_send, var1);
   }

   public void checkSendPermission() throws JMSSecurityException {
      JMSSecurityHelper.checkPermission(this.jmsResource_send);
   }

   public void checkReceivePermission(AuthenticatedSubject var1) throws JMSSecurityException {
      JMSSecurityHelper.checkPermission(this.jmsResource_receive, var1);
   }

   public void checkReceivePermission() throws JMSSecurityException {
      JMSSecurityHelper.checkPermission(this.jmsResource_receive);
   }

   public void checkBrowsePermission(AuthenticatedSubject var1) throws JMSSecurityException {
      JMSSecurityHelper.checkPermission(this.jmsResource_browse, var1);
   }

   public void checkBrowsePermission() throws JMSSecurityException {
      JMSSecurityHelper.checkPermission(this.jmsResource_browse);
   }

   public JMSResource getJMSResourceForSend() {
      return this.jmsResource_send;
   }

   public JMSResource getJMSResourceForReceive() {
      return this.jmsResource_receive;
   }

   public JMSResource getJMSResourceForBrowse() {
      return this.jmsResource_browse;
   }
}
