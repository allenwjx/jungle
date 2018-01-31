${gg.setIgnoreOutput(sql.columnsCount <= 1 || sql.columnsInSameTable)}

<#include '/java_copyright.include'/>

package ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case};

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

<#include '/java_import.include'/>

/**
<#include '/java_description.include'/>
 */
public class ${sql.resultClassName} implements java.io.Serializable {
	private static final long serialVersionUID = -5216457518046898601L;
	
	<#list sql.columns as column>
	/** ${column.columnAlias!} */
	private ${column.possibleShortJavaType} ${column.columnNameLower};
	</#list>

	<#list sql.columns as column>
	public void set${column.columnName}(${column.possibleShortJavaType} ${column.columnNameLower}) {
		this.${column.columnNameLower} = ${column.columnNameLower};
	}
	
	public ${column.possibleShortJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	</#list>

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
