package weblogic.wsee.tools.xcatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.tools.ant.types.FileSet;

public class CatalogInfo {
   List<FileSet> copyFiles = new ArrayList();
   HashMap<String, String> localCatalogs = new HashMap();
   HashMap<String, String> remoteCatalogs = new HashMap();
   List<FileAnalysis> fileAnalysisList = new ArrayList();
   boolean copyUpperRelativePathFlag = false;
   boolean isEmbeddedInJwsc = false;

   public int catalogSize() {
      return this.localCatalogs.size() + this.remoteCatalogs.size();
   }

   public CatalogInfo() {
   }

   public CatalogInfo(boolean var1) {
      this.isEmbeddedInJwsc = var1;
   }
}
