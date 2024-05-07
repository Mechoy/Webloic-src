package weblogic.wsee.tools.xcatalog;

public class FileAnalysis {
   static final int TYPE_LOCAL_CATALOG = 0;
   static final int TYPE_REMOTE_CATALOG = 1;
   static final int TYPE_REAL_URI = 2;
   String systemId;
   String baseDir;
   String idOrURI;
   String cBaseDir;
   String originalURI;
   int Type;

   public FileAnalysis(int var1, String var2, String var3, String var4, String var5) {
      this.Type = var1;
      this.baseDir = var2;
      this.idOrURI = var3;
      this.systemId = var4;
      this.originalURI = var5;
   }
}
