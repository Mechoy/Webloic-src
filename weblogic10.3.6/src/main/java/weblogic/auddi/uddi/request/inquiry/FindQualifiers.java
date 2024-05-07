package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.TooManyOptionsException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.UDDIList;

public class FindQualifiers extends UDDIList {
   public void add(FindQualifier var1) throws UDDIException {
      String var2 = var1.getValue();
      FindQualifier var3 = (FindQualifier)super.getVFirst();
      boolean var4 = false;
      if (var3 != null) {
         while(var3 != null) {
            String var5 = var3.getValue();
            if (var5.equals(var2)) {
               var4 = true;
            }

            if (var5.equals("sortByNameAsc") && var2.equals("sortByNameDesc")) {
               throw new TooManyOptionsException(UDDIMessages.get("error.tooManyOptions.exclusive", "sortByNameAsc", "sortByNameDesc"));
            }

            if (var5.equals("sortByDateAsc") && var2.equals("sortByDateDesc")) {
               throw new TooManyOptionsException(UDDIMessages.get("error.tooManyOptions.exclusive", "sortByDateAsc", "sortByDateDesc"));
            }

            var3 = (FindQualifier)super.getVNext();
         }
      }

      if (!var4) {
         super.add(var1);
      }

   }

   public FindQualifier getFirst() {
      return (FindQualifier)super.getVFirst();
   }

   public FindQualifier getNext() {
      return (FindQualifier)super.getVNext();
   }

   public String toXML() {
      return super.toXML("findQualifiers");
   }
}
