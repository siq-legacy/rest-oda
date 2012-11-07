package oda.datatools.connectivity.rest.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
public class RESTList
{
  List<String> columnlist;
  
  
  public void setColumnlist(List<String> columnlist) {
	this.columnlist = columnlist;
}

  private List<Class<?>> datatype;
  public void setDatatype(List<Class<?>> datatype) {
	this.datatype = datatype;
}

private List<Object> row;
  private List<List<Object>> rowlist;

  public List<Class<?>> getDatatype()
  {
    return this.datatype;
  }
  public List<String> getColumnlist() {
    return this.columnlist;
  }

  public String toString() {
    return this.rowlist.toString();
  }

  public RESTList()
  {
    
  }

  public void addDatatype(Class<?> clas) {
    this.datatype.add(clas);
  }

  
  public void addObj(Object obj,int position) {
    this.row.set(position, obj);
  }

  public void createRow() {
	  this.row =  Arrays.asList(new Object[this.columnlist.size()]);
	  
  }
  public void freeMem()
  {
	  
	  this.rowlist = new LinkedList<List<Object>>();
  }

  public void addtoRowList()
  {
    this.rowlist.add(this.row);
  }

  public List<List<Object>> getRows() {
    return this.rowlist;
  }
}