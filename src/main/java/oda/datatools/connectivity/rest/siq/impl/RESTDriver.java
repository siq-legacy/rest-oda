package oda.datatools.connectivity.rest.siq.impl;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

public class RESTDriver
  implements IDriver,RESTConstants
{
 

  public IConnection getConnection(String arg0) throws OdaException
  {
    System.out.println("i got inside REST driver");
    return new RESTConnection();
  }

  public RESTConnection getRestConnection(String arg0) throws OdaException
  {
    System.out.println("i got inside REST driver");
    return new RESTConnection();
  }
  public int getMaxConnections()
    throws OdaException
  {
    return 0;
  }

  public void setAppContext(Object arg0)
    throws OdaException
  {
  }

  public void setLogConfiguration(LogConfiguration arg0)
    throws OdaException
  {
  }

  public static ExtensionManifest getManifest() throws OdaException
  {
    return ManifestExplorer.getInstance().getExtensionManifest(ODA_DATA_SOURCE_ID);
  }

  static String getNativeDataTypeName(int nativeDataTypeCode) throws OdaException {
    DataTypeMapping typeMapping = getManifest().getDataSetType(ODA_DATA_SET_ID)
      .getDataTypeMapping(nativeDataTypeCode);
    if (typeMapping != null)
      return typeMapping.getNativeType();
    return "Non-defined";
  }
}