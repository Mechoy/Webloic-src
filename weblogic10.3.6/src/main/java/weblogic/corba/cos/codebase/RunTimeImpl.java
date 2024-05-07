package weblogic.corba.cos.codebase;

import org.omg.SendingContext._RunTimeImplBase;
import weblogic.iiop.IOR;
import weblogic.iiop.ObjectKey;
import weblogic.rmi.internal.InitialReferenceConstants;

public final class RunTimeImpl extends _RunTimeImplBase implements InitialReferenceConstants {
   public static final String TYPE_ID = "IDL:omg.org/SendingContext/RunTime:1.0";
   private static final IOR ior = new IOR("IDL:omg.org/SendingContext/RunTime:1.0", new ObjectKey("IDL:omg.org/SendingContext/RunTime:1.0", 11));
   private static final RunTimeImpl runtime = new RunTimeImpl();

   private RunTimeImpl() {
   }

   public static final RunTimeImpl getRunTime() {
      return runtime;
   }

   public IOR getIOR() {
      return ior;
   }
}
