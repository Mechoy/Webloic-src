package weblogic.xml.jaxr.registry.util;

import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.UnexpectedObjectException;
import weblogic.auddi.util.uuid.UUIDException;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class JAXRUtil {
   private JAXRUtil() {
   }

   public static JAXRException mapException(UUIDException var0) {
      return new JAXRException(var0.getMessage());
   }

   public static void verifyCollectionContent(Collection var0, Class var1) throws UnexpectedObjectException {
      if (var0 == null) {
         String var6 = JAXRMessages.getMessage("jaxr.infomodel.collection.nullCollection", new Object[]{var1.getName()});
         throw new UnexpectedObjectException(var6);
      } else {
         Iterator var2 = var0.iterator();

         Class var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            Object var3 = var2.next();
            if (var3 == null) {
               String var7 = JAXRMessages.getMessage("jaxr.infomodel.collection.invalidContent", new Object[]{var1.getName(), "NULL"});
               throw new UnexpectedObjectException(var7);
            }

            var4 = var3.getClass();
         } while(var1.isAssignableFrom(var4));

         String var5 = JAXRMessages.getMessage("jaxr.infomodel.collection.invalidContent", new Object[]{var1.getName(), "NULL"});
         throw new UnexpectedObjectException(var5);
      }
   }

   public static void verifyNotNull(Object var0, Class var1) throws InvalidRequestException {
      if (var0 == null) {
         String var2 = JAXRMessages.getMessage("jaxr.infomodel.argument.null", new Object[]{var1.getName()});
         throw new InvalidRequestException(var2);
      }
   }
}
