package weblogic.jms.backend;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import javax.jms.Destination;
import weblogic.jms.common.JMSID;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class BETempDestinationFactory_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$javax$jms$Destination;
   // $FF: synthetic field
   private static Class class$weblogic$jms$common$JMSID;
   // $FF: synthetic field
   private static Class class$weblogic$messaging$dispatcher$DispatcherId;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      switch (var1) {
         case 0:
            DispatcherId var5;
            JMSID var6;
            boolean var7;
            int var8;
            long var9;
            String var11;
            try {
               MsgInput var12 = var2.getMsgInput();
               var5 = (DispatcherId)var12.readObject(class$weblogic$messaging$dispatcher$DispatcherId == null ? (class$weblogic$messaging$dispatcher$DispatcherId = class$("weblogic.messaging.dispatcher.DispatcherId")) : class$weblogic$messaging$dispatcher$DispatcherId);
               var6 = (JMSID)var12.readObject(class$weblogic$jms$common$JMSID == null ? (class$weblogic$jms$common$JMSID = class$("weblogic.jms.common.JMSID")) : class$weblogic$jms$common$JMSID);
               var7 = var12.readBoolean();
               var8 = var12.readInt();
               var9 = var12.readLong();
               var11 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var16) {
               throw new UnmarshalException("error unmarshalling arguments", var16);
            } catch (ClassNotFoundException var17) {
               throw new UnmarshalException("error unmarshalling arguments", var17);
            }

            Destination var13 = ((BETempDestinationFactoryRemote)var4).createTempDestination(var5, var6, var7, var8, var9, var11);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var13, class$javax$jms$Destination == null ? (class$javax$jms$Destination = class$("javax.jms.Destination")) : class$javax$jms$Destination);
               return var3;
            } catch (IOException var15) {
               throw new MarshalException("error marshalling return", var15);
            }
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public Object invoke(int var1, Object[] var2, Object var3) throws Exception {
      switch (var1) {
         case 0:
            return ((BETempDestinationFactoryRemote)var3).createTempDestination((DispatcherId)var2[0], (JMSID)var2[1], (Boolean)var2[2], (Integer)var2[3], (Long)var2[4], (String)var2[5]);
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
