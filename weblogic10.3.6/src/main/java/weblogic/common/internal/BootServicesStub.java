package weblogic.common.internal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import weblogic.common.CommonLogger;
import weblogic.common.WLObjectInput;
import weblogic.protocol.Protocol;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.MsgAbbrevOutputStream;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.Response;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedUser;

public class BootServicesStub implements BootServices {
   private static int ID = 1;
   private RJVM rjvm;
   private Protocol protocol;

   public static WLObjectInput getMsg(Response var0) throws RemoteException {
      Throwable var1 = var0.getThrowable();
      if (var1 == null) {
         return var0.getMsg();
      } else if (var1 instanceof RemoteException) {
         throw (RemoteException)var1;
      } else if (var1 instanceof RuntimeException) {
         throw (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      } else {
         CommonLogger.logUnexpectedProblem(var1);
         throw new ClassCastException("Expected RemoteException, RuntimeException, or Error but received: '" + var1.toString() + "'");
      }
   }

   public BootServicesStub(RJVM var1, Protocol var2) {
      this.rjvm = var1;
      this.protocol = var2;
   }

   public AuthenticatedUser authenticate(UserInfo var1) throws RemoteException {
      MsgAbbrevOutputStream var2 = null;

      try {
         var2 = this.rjvm.getRequestStreamForDefaultUser(this.protocol);
         var2.marshalCustomCallData();
         var2.writeByte(1);
         var2.writeObject(var1);
         return (AuthenticatedUser)getMsg(var2.sendRecv(ID, this.protocol.getQOS())).readObject();
      } catch (IOException var4) {
         throw new UnexpectedException("Marshalling: ", var4);
      } catch (ClassNotFoundException var5) {
         throw new UnexpectedException("Marshalling: ", var5);
      }
   }

   public T3ClientParams findOrCreateClientContext(String var1, UserInfo var2, int var3) throws RemoteException {
      MsgAbbrevOutputStream var4 = null;

      try {
         var4 = this.rjvm.getRequestStream((ServerChannel)null);
         var4.marshalCustomCallData();
         var4.writeByte(2);
         var4.writeString(var1);
         var4.writeObjectWL(var2);
         var4.writeInt(var3);
         var4.writeByte(this.protocol.getQOS());
         return (T3ClientParams)getMsg(var4.sendRecv(ID, this.protocol.getQOS())).readObjectWL();
      } catch (IOException var6) {
         throw new UnexpectedException("Marshalling: ", var6);
      } catch (ClassNotFoundException var7) {
         throw new UnexpectedException("Marshalling: ", var7);
      }
   }
}
