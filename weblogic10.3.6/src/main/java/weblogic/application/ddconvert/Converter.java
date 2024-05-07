package weblogic.application.ddconvert;

import java.io.File;
import weblogic.utils.jars.VirtualJarFile;

interface Converter {
   void convertDDs(ConvertCtx var1, VirtualJarFile var2, File var3) throws DDConvertException;

   void printStartMessage(String var1);

   void printEndMessage(String var1);
}
