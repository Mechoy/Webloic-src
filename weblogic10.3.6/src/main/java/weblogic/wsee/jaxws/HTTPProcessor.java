package weblogic.wsee.jaxws;

import javax.servlet.ServletException;
import weblogic.servlet.http.RequestResponseKey;

interface HTTPProcessor {
   boolean get(RequestResponseKey var1, boolean var2) throws ServletException;

   boolean post(RequestResponseKey var1, boolean var2) throws ServletException;
}
