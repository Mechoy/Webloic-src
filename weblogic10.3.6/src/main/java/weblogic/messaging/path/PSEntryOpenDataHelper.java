package weblogic.messaging.path;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.messaging.runtime.OpenDataConverter;

public class PSEntryOpenDataHelper implements OpenDataConverter {
   public CompositeData createCompositeData(Object var1) throws OpenDataException {
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof PSEntryInfo)) {
         throw new OpenDataException("Unexpected class " + var1.getClass().getName());
      } else {
         return ((PSEntryInfo)var1).toCompositeData();
      }
   }
}
