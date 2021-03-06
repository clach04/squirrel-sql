package net.sourceforge.squirrel_sql.fw.sql;


/*
 * Copyright (C) 2010 Rob Manning
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

public interface IObjectTypes
{

	DatabaseObjectType getConsumerGroupParent();

	DatabaseObjectType getFunctionParent();

	DatabaseObjectType getIndexParent();

	DatabaseObjectType getInstanceParent();

	DatabaseObjectType getLobParent();

	DatabaseObjectType getPackageParent();

	DatabaseObjectType getSequenceParent();

	DatabaseObjectType getSessionParent();

	DatabaseObjectType getTriggerParent();

	DatabaseObjectType getTypeParent();

	DatabaseObjectType getUserParent();

	DatabaseObjectType getConstraintParent();

	DatabaseObjectType getConsumerGroup();

	DatabaseObjectType getInstance();

	DatabaseObjectType getLob();

	DatabaseObjectType getPackage();

	DatabaseObjectType getSession();

	DatabaseObjectType getType();

	DatabaseObjectType getConstraint();

}