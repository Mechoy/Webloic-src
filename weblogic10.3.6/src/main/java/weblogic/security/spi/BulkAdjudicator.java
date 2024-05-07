package weblogic.security.spi;

import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.security.service.ContextHandler;

public interface BulkAdjudicator {
   void initialize(String[] var1);

   Set<Resource> adjudicate(List<Map<Resource, Result>> var1, List<Resource> var2, ContextHandler var3);
}
