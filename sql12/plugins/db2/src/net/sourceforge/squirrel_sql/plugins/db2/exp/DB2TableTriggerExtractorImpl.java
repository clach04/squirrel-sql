/*
 * Copyright (C) 2007 Rob Manning
 * manningr@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.squirrel_sql.plugins.db2.exp;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.expanders.ITableTriggerExtractor;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.plugins.db2.sql.DB2Sql;

/**
 * Provides the query and parameter binding behavior for DB2's trigger catalog.
 * 
 * @author manningr
 */
public class DB2TableTriggerExtractorImpl implements ITableTriggerExtractor
{

	/** Logger for this class */
	private final static ILogger s_log = LoggerController.createLogger(DB2TableTriggerExtractorImpl.class);

	/** Object that contains methods for retrieving SQL that works for each DB2 platform */
	private final DB2Sql db2Sql;

	/**
	 * Ctor.
	 * 
	 * @param db2Sql
	 *           Object that contains methods for retrieving SQL that works for each DB2 platform
	 */
	public DB2TableTriggerExtractorImpl(DB2Sql db2Sql)
	{
		this.db2Sql = db2Sql;
	}

	/**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.expanders.ITableTriggerExtractor#bindParamters(java.sql.PreparedStatement,
	 *      net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo)
	 */
	public void bindParamters(PreparedStatement pstmt, IDatabaseObjectInfo dbo) throws SQLException
	{
		if (s_log.isDebugEnabled())
		{
			s_log.debug("Binding schema name " + dbo.getSchemaName() + " as first bind value");
			s_log.debug("Binding table name " + dbo.getSimpleName() + " as second bind value");
		}
		pstmt.setString(1, dbo.getSchemaName());
		pstmt.setString(2, dbo.getSimpleName());

	}

	/**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.expanders.ITableTriggerExtractor#getTableTriggerQuery()
	 */
	public String getTableTriggerQuery()
	{
		return db2Sql.getTableTriggerListSql();
	}

}
