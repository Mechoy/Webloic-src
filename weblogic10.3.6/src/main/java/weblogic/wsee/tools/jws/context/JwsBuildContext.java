package weblogic.wsee.tools.jws.context;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import weblogic.wsee.tools.logging.Logger;

public interface JwsBuildContext {
   Logger getLogger();

   boolean isInError();

   List<String> getErrorMsgs();

   Map getProperties();

   /** @deprecated */
   Task getTask();

   String getSrcEncoding();

   String getDestEncoding();

   String getCodegenOutputEncoding();

   String[] getSourcepath();

   String[] getClasspath();

   ClassLoader getClassLoader();

   Collection<File> getClientGenOutputDirs();

   /** @deprecated */
   public static enum Task {
      APT,
      JWSC,
      JWSWSEEGEN;
   }
}
