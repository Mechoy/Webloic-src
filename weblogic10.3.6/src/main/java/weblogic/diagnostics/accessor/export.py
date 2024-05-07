import sys
import java
import weblogic.diagnostics.accessor.XMLExporter as XMLExporter
import weblogic.kernel.Kernel as Kernel

def exportDiagnosticData(**dict) :
    xmlExporter = XMLExporter()

    dict.setdefault('logicalName', 'ServerLog')
    dict.setdefault('logName', 'myserver.log')
    dict.setdefault('logRotationDir', '.')
    dict.setdefault('storeDir', '../data/store/diagnostics')
    dict.setdefault('query', '')
    dict.setdefault('exportFileName', 'export.xml')
    dict.setdefault('beginTimestamp', 0)
    dict.setdefault('endTimestamp', Long.MAX_VALUE)
    dict.setdefault('elfFields', '')

    System.out.println()    
    System.out.print("Input parameters: ");
    System.out.print("{logicalName='" + dict.get('logicalName') + "', ")
    System.out.print("logName='" + dict.get('logName') + "', ")
    System.out.print("logRotationDir='" + dict.get('logRotationDir') + "', ")
    System.out.print("storeDir='" + dict.get('storeDir') + "', ")
    System.out.print("query='" + dict.get('query') + "', ")
    System.out.print("exportFileName='" + dict.get('exportFileName') + "', ")
    System.out.print("elfFields='" + dict.get('elfFields') + "', ")
    System.out.print("beginTimestamp=")
    System.out.print(dict.get('beginTimestamp'))
    System.out.print("L, endTimestamp=")
    System.out.print(dict.get('endTimestamp'))
    System.out.print("L}")        
    System.out.println()
    System.out.println()    
    
    # print dict
    
    logicalName = dict.get('logicalName')
    logName = dict.get('logName')         
    logRotationDir = dict.get('logRotationDir')
    storeDir = dict.get('storeDir')
    query = dict.get('query')
    exportFileName = dict.get('exportFileName')
    elfFields = dict.get('elfFields')
    begin = dict.get('beginTimestamp')
    end = dict.get('endTimestamp')

    System.out.println("Exporting diagnostic data to " + exportFileName + " ...")  
    System.out.println()

    Kernel.ensureInitialized()

    xmlExporter.exportDiagnosticData(\
    logicalName, \
    logName, \
    logRotationDir, \
    storeDir, \
    query, \
    exportFileName, \
    elfFields, \
    begin, end)

    System.out.println()    
    System.out.println("Exported diagnostic data successfully.")
    System.out.println()