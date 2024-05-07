package weblogic.security.acl.internal;

import java.rmi.RemoteException;
import weblogic.iiop.Connection;
import weblogic.iiop.EndPoint;
import weblogic.iiop.IDLUtils;
import weblogic.iiop.IOR;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.spi.InboundRequest;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.SecurityService;
import weblogic.security.acl.UserInfo;

public final class SecurityServiceImpl implements SecurityService {
   public static IOR getIOR() {
      return SecurityServiceImpl.SingletonMaker.ior;
   }

   public static SecurityServiceImpl getSingleton() {
      return SecurityServiceImpl.SingletonMaker.SINGLETON;
   }

   private SecurityServiceImpl() {
   }

   public AuthenticatedUser authenticate(UserInfo var1) throws RemoteException {
      throw new AssertionError("authenticate()");
   }

   public AuthenticatedUser authenticate(UserInfo var1, InboundRequest var2) throws RemoteException {
      Connection var3 = ((EndPoint)var2.getEndPoint()).getConnection();
      var3.authenticate(var1);
      AuthenticatedSubject var4 = var3.getUser();
      if (ChannelHelper.isLocalAdminChannelEnabled() && SubjectUtils.isUserAnAdministrator(var4) && var3.getChannel().getProtocol().getQOS() != 103) {
         throw new SecurityException("All administrative tasks must go through an Administration Port.");
      } else {
         return var4;
      }
   }

   // $FF: synthetic method
   SecurityServiceImpl(Object var1) {
      this();
   }

   private static final class SingletonMaker {
      private static final SecurityServiceImpl SINGLETON = new SecurityServiceImpl();
      private static final IOR ior = new IOR(IDLUtils.getTypeID(SecurityService.class), 14);
   }
}
