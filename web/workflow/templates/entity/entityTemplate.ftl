<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Configuration DTD//EN" 
"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping auto-import="true" default-access="property" default-cascade="none" default-lazy="true">

	<class abstract="false" name="com.kwchina.oa.workflow.customfields.domain.CustomizableEntity" table="${_TableName}" schema="dbo">

		<id column="id" name="id">
			<generator class="native" />
		</id>
		<property name="instanceId" column="instanceId" type="int" />

		<dynamic-component insert="true" name="customProperties" optimistic-lock="true" unique="false" update="true"></dynamic-component>
	
	</class>
</hibernate-mapping>
