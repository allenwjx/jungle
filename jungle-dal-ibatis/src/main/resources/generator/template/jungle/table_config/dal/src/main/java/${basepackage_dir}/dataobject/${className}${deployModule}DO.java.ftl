<#assign table = tableConfig.table>   
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<#include '/java_copyright.include'/>

package ${tableConfig.basepackage}.dataobject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

<#include '/java_import.include'/>

/**
 * ${tableConfig.className}${deployModule}DO
<#include '/java_description.include'/>
 */
public class ${className}${deployModule}DO implements java.io.Serializable {
	private static final long serialVersionUID = -5216457518046898601L;
	
	<#list table.columns as column>
	/**
	 * ${column.columnAlias!} 		db_column: ${column.sqlName} 
	 */
	private ${column.simpleJavaType} ${column.columnNameLower};
	</#list>

<@generateJavaColumns/>

	public String toString() {
		return new ToStringBuilder(this)
		<#list table.columns as column>
			.append("${column.columnName}",get${column.columnName}())
		</#list>
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
		<#list table.pkColumns as column>
			.append(get${column.columnName}())
		</#list>
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		if(obj instanceof ${className}${deployModule}DO == false) return false;
		${className}${deployModule}DO other = (${className}${deployModule}DO)obj;
		return new EqualsBuilder()
			<#list table.pkColumns as column>
			.append(get${column.columnName}(),other.get${column.columnName}())
			</#list>
			.isEquals();
	}
}

<#macro generateJavaColumns>
	<#list table.columns as column>
	
	public void set${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}) {
		this.${column.columnNameLower} = ${column.columnNameLower};
	}
	
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	</#list>
</#macro>