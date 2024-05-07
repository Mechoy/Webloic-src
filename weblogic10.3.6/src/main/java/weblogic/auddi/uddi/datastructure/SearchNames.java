package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.TooManyOptionsException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class SearchNames extends Names {
   public SearchNames() {
   }

   public SearchNames(SearchNames var1) throws UDDIException {
      super(var1);
   }

   public void add(Name var1) throws UDDIException {
      if (this.size() == 5) {
         throw new TooManyOptionsException(UDDIMessages.get("error.tooManyOptions.name", "5"));
      } else {
         super.add(var1);
      }
   }
}
