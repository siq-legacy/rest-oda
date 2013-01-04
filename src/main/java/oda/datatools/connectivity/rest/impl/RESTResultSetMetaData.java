package oda.datatools.connectivity.rest.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
public class RESTResultSetMetaData
  implements IResultSetMetaData
{
  private List<String> colHeaders = new ArrayList<String>();
  protected static Logger logger = Logger.getLogger(RESTResultSetMetaData.class.getName());
  private RESTList siqlist;

  public RESTResultSetMetaData(RESTList siqlistentries)
  {
	  this.siqlist = siqlistentries;
	    for (String tag : siqlistentries.getColumnlist())
	      this.colHeaders.add(tag);
  }

  public int getColumnCount() throws OdaException
  {
    return this.colHeaders.size();
  }

  public int getColumnDisplayLength(int arg0)
    throws OdaException
  {
    return 0;
  }

  public String getColumnLabel(int index)
    throws OdaException
  {
    return getColumnName(index);
  }

  public String getColumnName(int index)
    throws OdaException
  {
    return (String)this.colHeaders.get(index - 1);
  }

  public String getdatatype(int index)
  {
    List<Class<?>> siqdatatype = this.siqlist.getDatatype();
    Class<?> cls = (Class<?>)siqdatatype.get(index-1);
 
    return cls.getName();
   
  }
  public int getColumnType(int index)
    throws OdaException
  {
	  DataSetType typeMapping[] = RESTDriver.getManifest().getDataSetTypes();
		for(int i=0;i<typeMapping.length;i++)
		{
			DataTypeMapping typemap[]=typeMapping[i].getDataTypeMappings();
			for(int j=0;j<typemap.length;j++)
			{
				if(getdatatype(index).equals(typemap[j].getNativeType()))
				{
					return typemap[j].getNativeTypeCode();
				}
				
			}
		
		}
		return 1;
   
  }

  public String getColumnTypeName(int index)
    throws OdaException
  {
	return getdatatype(index);

  }

  public int getPrecision(int arg0)
    throws OdaException
  {
    return 0;
  }

  public int getScale(int arg0)
    throws OdaException
  {
    return 0;
  }

  public int isNullable(int arg0)
    throws OdaException
  {
    return 0;
  }
}