package weblogic.wsee.tools.jws;

import weblogic.wsee.tools.WsBuildException;

public interface JWSProcessor {
   void init(ModuleInfo var1) throws WsBuildException;

   void process(WebServiceInfo var1) throws WsBuildException;

   void finish() throws WsBuildException;
}
