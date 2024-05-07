package weblogic.wsee.server.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Processor {
   boolean process(HttpServletRequest var1, HttpServletResponse var2, BaseWSServlet var3) throws IOException;
}
