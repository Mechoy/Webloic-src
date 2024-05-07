package weblogic.application.io;

import java.io.IOException;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClassFinder;

public abstract class Archive {
   protected static final boolean DEBUG = Debug.getCategory("weblogic.application.io").isEnabled();

   public abstract ClassFinder getClassFinder() throws IOException;

   public abstract void remove();
}
