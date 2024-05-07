package weblogic.jms.common;

import java.io.IOException;
import java.io.ObjectInput;
import weblogic.jms.JMSClientExceptionLogger;

public abstract class Destination implements javax.jms.Destination {
   static final byte NULLDESTINATIONIMPL = 0;
   static final byte DESTINATIONIMPL = 1;
   static final byte DISTRIBUTEDDESTINATIONIMPL = 2;
   static final byte FUTUREDESTINATIONIMPL1 = 3;
   static final byte FUTUREDESTINATIONIMPL2 = 4;
   static final byte FUTUREDESTINATIONIMPL3 = 5;
   static final byte FUTUREDESTINATIONIMPL4 = 6;
   static final byte FUTUREDESTINATIONIMPL5 = 7;
   public static final byte _IFDESTCANNOTBENULL = 1;
   public static final byte _IFMUSTBEQUEUE = 2;
   public static final byte _IFMUSTBETOPIC = 4;

   public static int getDestinationType(Destination var0, int var1) {
      if (var0 == null) {
         return 0;
      } else {
         byte var2 = var0.getDestinationInstanceType();
         return var2 << var1;
      }
   }

   protected abstract byte getDestinationInstanceType();

   public static boolean equalsForDS(Destination var0, Destination var1) {
      byte var2 = var0.getDestinationInstanceType();
      byte var3 = var1.getDestinationInstanceType();
      if (var2 != var3) {
         return false;
      } else {
         switch (var2) {
            case 1:
               return var0.equals(var1);
            case 2:
               return ((DistributedDestinationImpl)var0).same(((DistributedDestinationImpl)var1).getName());
            default:
               return false;
         }
      }
   }

   public static DestinationImpl createDestination(byte var0, ObjectInput var1) throws IOException, ClassNotFoundException {
      DistributedDestinationImpl var2 = null;
      switch (var0) {
         case 0:
            return var2;
         case 1:
            DestinationImpl var3 = new DestinationImpl();
            var3.readExternal(var1);
            return var3;
         case 2:
            var2 = new DistributedDestinationImpl();
            var2.readExternal(var1);
            return var2;
         default:
            throw new IOException(JMSClientExceptionLogger.logInternalMarshallingErrorLoggable(var0).getMessage());
      }
   }

   public static final void checkDestinationType(javax.jms.Destination var0, byte var1) throws javax.jms.JMSException {
      if (var0 == null) {
         if ((var1 & 1) != 0) {
            throw new InvalidDestinationException(JMSClientExceptionLogger.logDestinationNullLoggable().getMessage());
         }
      } else {
         if (!(var0 instanceof DestinationImpl)) {
            throw new InvalidDestinationException(JMSClientExceptionLogger.logForeignDestination3Loggable(var0.toString()).getMessage());
         }

         if (var1 != 0) {
            if ((var1 & 2) != 0) {
               if (!((DestinationImpl)var0).isQueue()) {
                  throw new InvalidDestinationException(JMSClientExceptionLogger.logDestinationMustBeQueueLoggable(var0.toString()).getMessage());
               }
            } else if ((var1 & 4) != 0 && !((DestinationImpl)var0).isTopic()) {
               throw new InvalidDestinationException(JMSClientExceptionLogger.logDestinationMustBeTopicLoggable(var0.toString()).getMessage());
            }
         }
      }

   }
}
