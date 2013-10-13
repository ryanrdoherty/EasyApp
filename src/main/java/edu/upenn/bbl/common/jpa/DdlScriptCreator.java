package edu.upenn.bbl.common.jpa;

import java.util.Collection;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;

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

	private static final String DEFAULT_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";

    private final Configuration hibernateConfiguration;
    private final Properties dialectProps;
    
    public DdlScriptCreator(final Collection<Class<?>> entities) {
    	this(entities, DEFAULT_DIALECT);
    }
    
    public DdlScriptCreator(final Collection<Class<?>> entities, String dialectClass) {

    	hibernateConfiguration = new Configuration();
    	for (final Class<?> entity : entities) {
    		hibernateConfiguration.addAnnotatedClass(entity);
    	}

    	dialectProps = new Properties();
    	dialectProps.put("hibernate.dialect", dialectClass);
    }

    /**
     * Create the SQL script to create all tables.
     * 
     * @return A {@link String} representing the SQL script.
     */
    public String createTablesScript() {
    	final StringBuilder script = new StringBuilder();

    	final String[] creationScript = hibernateConfiguration.generateSchemaCreationScript(Dialect
    			.getDialect(dialectProps));
    	for (final String string : creationScript) {
    		script.append(string).append(";\n");
    	}
    	script.append("\ngo\n\n");

    	return script.toString();
    }

    /**
     * Create the SQL script to drop all tables.
     * 
     * @return A {@link String} representing the SQL script.
     */
    public String dropTablesScript() {
    	final StringBuilder script = new StringBuilder();

    	final String[] creationScript = hibernateConfiguration.generateDropSchemaScript(Dialect
    			.getDialect(dialectProps));
    	for (final String string : creationScript) {
    		script.append(string).append(";\n");
    	}
    	script.append("\ngo\n\n");

    	return script.toString();
    }
}

