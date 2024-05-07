package weblogic.common.internal;

import java.util.Map;
import weblogic.rjvm.RJVM;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.BasicRemoteRef;
import weblogic.rmi.internal.ClientMethodDescriptor;
import weblogic.rmi.internal.ClientRuntimeDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.security.acl.SecurityService;

public final class RMIBootServiceStub {
   private static final String STUB_NAME = ServerHelper.getStubClassName("weblogic.common.internal.RMIBootServiceImpl");
   private static final String[] bootServiceInterfaces = new String[]{SecurityService.class.getName(), StubInfoIntf.class.getName()};
   private static final ClientMethodDescriptor desc = new ClientMethodDescriptor("*", false, false, false, false, 0);
   private static final ClientRuntimeDescriptor bootServiceDescriptor;

   public static SecurityService getStub(RJVM var0, String var1, long var2) {
      BasicRemoteRef var4 = new BasicRemoteRef(27, var0.getID(), var1);
      StubInfo var5 = new StubInfo(var4, getClientRuntimeDescriptorWithTimeout((int)var2), STUB_NAME);
      return (SecurityService)StubFactory.getStub(var5);
   }

   private static ClientRuntimeDescriptor getClientRuntimeDescriptorWithTimeout(int var0) {
      if (var0 <= 0) {
         return bootServiceDescriptor;
      } else {
         ClientMethodDescriptor var1 = new ClientMethodDescriptor("*", false, false, false, false, var0);
         return new ClientRuntimeDescriptor(bootServiceInterfaces, (String)null, (Map)null, var1, STUB_NAME);
      }
   }

   static {
      bootServiceDescriptor = new ClientRuntimeDescriptor(bootServiceInterfaces, (String)null, (Map)null, desc, STUB_NAME);
   }
}
