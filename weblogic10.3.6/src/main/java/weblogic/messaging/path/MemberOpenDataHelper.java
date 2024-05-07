package weblogic.messaging.path;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.messaging.runtime.OpenDataConverter;

public class MemberOpenDataHelper implements OpenDataConverter {
   public CompositeData createCompositeData(Object var1) throws OpenDataException {
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof Member)) {
         throw new OpenDataException("Unexpected class " + var1.getClass().getName());
      } else {
         MemberInfo var2 = new MemberInfo((Member)var1);
         return var2.toCompositeData();
      }
   }
}
