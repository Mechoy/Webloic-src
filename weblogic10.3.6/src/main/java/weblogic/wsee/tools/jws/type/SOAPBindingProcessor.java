package weblogic.wsee.tools.jws.type;

import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;

public interface SOAPBindingProcessor {
   void processMethod(WebMethodDecl var1) throws WsBuildException;
}
