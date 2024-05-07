package weblogic.application.internal;

import java.util.Collections;
import java.util.Map;
import weblogic.application.RedeployInfo;

public class RedeployInfoImpl implements RedeployInfo {
   private final Map uriMappings;
   private final boolean urisAreNew;

   public RedeployInfoImpl() {
      this(Collections.EMPTY_MAP, false);
   }

   RedeployInfoImpl(Map var1, boolean var2) {
      this.uriMappings = var1;
      this.urisAreNew = var2;
   }

   public Map getUriMappings() {
      return this.uriMappings;
   }

   public boolean areUrisNew() {
      return this.urisAreNew;
   }

   public String toString() {
      return "uri mappings" + this.uriMappings + " are they new uris? " + this.urisAreNew;
   }
}
