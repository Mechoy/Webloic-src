package weblogic.application.internal;

import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextFactory;

public final class ApplicationContextFactoryImpl extends ApplicationContextFactory {
   public ApplicationContext newApplicationContext(String var1) {
      return new ApplicationContextImpl(var1);
   }
}
