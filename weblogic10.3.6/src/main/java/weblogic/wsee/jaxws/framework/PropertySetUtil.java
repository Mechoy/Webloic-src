package weblogic.wsee.jaxws.framework;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.message.Packet;
import java.lang.reflect.Constructor;
import javax.xml.ws.WebServiceException;

public class PropertySetUtil {
   public static <T extends PropertySet> PropertySetRetriever<T> getRetriever(Class<T> var0) {
      try {
         final Constructor var1 = var0.getConstructor(Packet.class);
         return new PropertySetRetriever<T>(var0) {
            public T getFromPacket(Packet var1x) {
               PropertySet var2 = super.getFromPacket(var1x);
               if (var2 == null) {
                  try {
                     var2 = (PropertySet)var1.newInstance(var1x);
                     var1x.addSatellite(var2);
                  } catch (Throwable var4) {
                     throw new WebServiceException(var4);
                  }
               }

               return var2;
            }
         };
      } catch (NoSuchMethodException var4) {
         try {
            final Constructor var2 = var0.getConstructor();
            return new PropertySetRetriever<T>(var0) {
               public T getFromPacket(Packet var1) {
                  PropertySet var2x = super.getFromPacket(var1);
                  if (var2x == null) {
                     try {
                        var2x = (PropertySet)var2.newInstance();
                        var1.addSatellite(var2x);
                     } catch (Throwable var4) {
                        throw new WebServiceException(var4);
                     }
                  }

                  return var2x;
               }
            };
         } catch (NoSuchMethodException var3) {
            throw new WebServiceException(var3);
         }
      }
   }

   public static class PropertySetRetriever<T extends PropertySet> {
      private final Class<T> clazz;

      public PropertySetRetriever(Class<T> var1) {
         this.clazz = var1;
      }

      public T getFromPacket(Packet var1) {
         return var1.getSatellite(this.clazz);
      }
   }
}
