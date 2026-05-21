package org.conical.common.bbl.jpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;

/**
 * SQL Creator for Tables according to JPA/Hibernate annotations.
 *
 * Use:
 *
 * {@link #createTablesScript()} To create the table creationg script
 *
 * {@link #dropTablesScript()} to create the table destruction script
 * 
 */
public class DdlScriptCreator {

	private static final String DEFAULT_DIALECT = org.hibernate.dialect.PostgreSQL10Dialect.class.getName();

	private final Function<Action,String> _sqlGenerator;
    
    public DdlScriptCreator(final Collection<Class<?>> entities) {
    	this(entities, DEFAULT_DIALECT);
    }
    
    public DdlScriptCreator(final Collection<Class<?>> entities, String dialectClass) {

    	Map<String, String> settings = new HashMap<>();
    	settings.put("dialect", dialectClass);
        settings.put("hibernate.hbm2ddl.auto", "create");
        settings.put("show_sql", "true");
 
        MetadataSources metadata = new MetadataSources(
            new StandardServiceRegistryBuilder().applySettings(settings).build());
    	for (final Class<?> entity : entities) {
    		metadata.addAnnotatedClass(entity);
    	}

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setDelimiter(";\n");
        schemaExport.setHaltOnError(true);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");

        _sqlGenerator = action -> {
        	StringBuilder sql = new StringBuilder();
        	schemaExport.perform(action, metadata.buildMetadata(), new ScriptTargetOutput() {
        		@Override public void prepare() { }
        		@Override public void release() { }
        		@Override public void accept(String str) { sql.append(str); }
        	});
        	return sql.toString();
        };
    }

    /**
     * Create the SQL script to create all tables.
     * 
     * @return A {@link String} representing the SQL script.
     */
    public String createTablesScript() {
    	return _sqlGenerator.apply(Action.CREATE);
    }

    /**
     * Create the SQL script to drop all tables.
     * 
     * @return A {@link String} representing the SQL script.
     */
    public String dropTablesScript() {
    	return _sqlGenerator.apply(Action.DROP);
    }

}
