package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.UnsupportedException;
import weblogic.auddi.uddi.datastructure.UDDIListObject;

public class FindQualifier extends UDDIListObject {
   private String value = null;
   public static final String EXACT_NAME_MATCH = "exactNameMatch";
   public static final String CASE_SENSITIVE_MATCH = "caseSensitiveMatch";
   public static final String SORT_BY_NAME_ASC = "sortByNameAsc";
   public static final String SORT_BY_NAME_DESC = "sortByNameDesc";
   public static final String SORT_BY_DATE_ASC = "sortByDateAsc";
   public static final String SORT_BY_DATE_DESC = "sortByDateDesc";
   public static final String OR_LIKE_KEYS = "orLikeKeys";
   public static final String OR_ALL_KEYS = "orAllKeys";
   public static final String AND_ALL_KEYS = "andAllKeys";
   public static final String COMBINE_CATEGORY_BAGS = "combineCategoryBags";
   public static final String SERVICE_SUBSET = "serviceSubset";
   public static final String SOUNDEX = "soundex";

   public FindQualifier(String var1) throws UDDIException {
      if (!var1.equals("exactNameMatch") && !var1.equals("caseSensitiveMatch") && !var1.equals("sortByNameAsc") && !var1.equals("sortByNameDesc") && !var1.equals("sortByDateAsc") && !var1.equals("sortByDateDesc") && !var1.equals("orLikeKeys") && !var1.equals("orAllKeys") && !var1.equals("andAllKeys") && !var1.equals("combineCategoryBags") && !var1.equals("serviceSubset") && !var1.equals("soundex")) {
         throw new UnsupportedException(UDDIMessages.get("error.unsupported.type", "findQualifier", var1));
      } else {
         this.value = var1;
      }
   }

   public String getValue() {
      return this.value;
   }

   public String toXML() {
      return "<findQualifier>" + this.value + "</findQualifier>\n";
   }
}
