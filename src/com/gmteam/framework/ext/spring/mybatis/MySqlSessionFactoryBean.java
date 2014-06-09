package com.gmteam.framework.ext.spring.mybatis;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.gmteam.framework.core.dao.DatabaseType;
import com.gmteam.framework.ext.mybatis.interceptor.PageInterceptor;

/**
 * 为实现多个配置文件的导入
 *
 * @author wh
 * 
 * @see #org.mybatis.spring.SqlSessionFactoryBean
 * @see #setConfigLocations
 * @version 0.1
 */
public class MySqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
  public static final String FIRST_CONFIG = "frameworkMybatis.xml";

  private static final Log logger = LogFactory.getLog(MySqlSessionFactoryBean.class);
  private List<String> configLocations;
  private Resource[] mapperLocations;
  private DataSource dataSource;
  private TransactionFactory transactionFactory;
  private Properties configurationProperties;
  private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
  private SqlSessionFactory sqlSessionFactory;
  private String environment = MySqlSessionFactoryBean.class.getSimpleName();
  private boolean failFast;
  private Interceptor[] plugins;
  private TypeHandler<?>[] typeHandlers;
  private String typeHandlersPackage;
  private Class<?>[] typeAliases;
  private String typeAliasesPackage;
  private Class<?> typeAliasesSuperType;
  private DatabaseIdProvider databaseIdProvider;
  private ObjectFactory objectFactory;
  private ObjectWrapperFactory objectWrapperFactory;
  private String databaseType;

  /**
   * 设置数据库类型
   * @param databaseType
   */
  public void setDatabaseType(String databaseType) {
    this.databaseType = databaseType;
  }

  /**
   * Sets the ObjectFactory.
   * 
   * @since 1.1.2
   * @param objectFactory
   */
  public void setObjectFactory(ObjectFactory objectFactory) {
    this.objectFactory = objectFactory;
  }

  /**
   * Sets the ObjectWrapperFactory.
   * 
   * @since 1.1.2
   * @param objectWrapperFactory
   */
  public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
    this.objectWrapperFactory = objectWrapperFactory;
  }

  /**
   * Gets the DatabaseIdProvider
   *
   * @since 1.1.0
   * @return DatabaseIdProvider
   */
  public DatabaseIdProvider getDatabaseIdProvider() {
    return databaseIdProvider;
  }

  /**
   * Sets the DatabaseIdProvider.
   * As of version 1.2.2 this variable is not initialized by default. 
   *
   * @since 1.1.0
   * @param databaseIdProvider
   */
  public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
    this.databaseIdProvider = databaseIdProvider;
  }

  /**
   * Mybatis plugin list.
   *
   * @since 1.0.1
   * @param plugins list of plugins
   */
  public void setPlugins(Interceptor[] plugins) {
    this.plugins = plugins;
  }

  /**
   * Packages to search for type aliases.
   *
   * @since 1.0.1
   * @param typeAliasesPackage package to scan for domain objects
   */
  public void setTypeAliasesPackage(String typeAliasesPackage) {
    this.typeAliasesPackage = typeAliasesPackage;
  }

  /**
   * Super class which domain objects have to extend to have a type alias created.
   * No effect if there is no package to scan configured.
   *
   * @since 1.1.2
   * @param typeAliasesSuperType super class for domain objects
   */
  public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
    this.typeAliasesSuperType = typeAliasesSuperType;
  }

  /**
   * Packages to search for type handlers.
   *
   * @since 1.0.1
   * @param typeHandlersPackage package to scan for type handlers
   */
  public void setTypeHandlersPackage(String typeHandlersPackage) {
    this.typeHandlersPackage = typeHandlersPackage;
  }

  /**
   * Set type handlers. They must be annotated with {@code MappedTypes} and optionally with {@code MappedJdbcTypes}
   *
   * @since 1.0.1
   * @param typeHandlers Type handler list
   */
  public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
    this.typeHandlers = typeHandlers;
  }

  /**
   * List of type aliases to register. They can be annotated with {@code Alias}
   *
   * @since 1.0.1
   * @param typeAliases Type aliases list
   */
  public void setTypeAliases(Class<?>[] typeAliases) {
    this.typeAliases = typeAliases;
  }

  /**
   * If true, a final check is done on Configuration to assure that all mapped
   * statements are fully loaded and there is no one still pending to resolve
   * includes. Defaults to false.
   *
   * @since 1.0.1
   * @param failFast enable failFast
   */
  public void setFailFast(boolean failFast) {
    this.failFast = failFast;
  }

  /**
   * Set the location of the MyBatis {@code SqlSessionFactory} config file. A typical value is
   * "WEB-INF/mybatis-configuration.xml".
   */
  public void setConfigLocations(List<String> configLocations) {
    this.configLocations = configLocations;
  }

  /**
   * Set locations of MyBatis mapper files that are going to be merged into the {@code SqlSessionFactory}
   * configuration at runtime.
   *
   * This is an alternative to specifying "&lt;sqlmapper&gt;" entries in an MyBatis config file.
   * This property being based on Spring's resource abstraction also allows for specifying
   * resource patterns here: e.g. "classpath*:sqlmap/*-mapper.xml".
   */
  public void setMapperLocations(Resource[] mapperLocations) {
    this.mapperLocations = mapperLocations;
  }

  /**
   * Set optional properties to be passed into the SqlSession configuration, as alternative to a
   * {@code &lt;properties&gt;} tag in the configuration xml file. This will be used to
   * resolve placeholders in the config file.
   */
  public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
    this.configurationProperties = sqlSessionFactoryProperties;
  }

  /**
   * Set the JDBC {@code DataSource} that this instance should manage transactions for. The {@code DataSource}
   * should match the one used by the {@code SqlSessionFactory}: for example, you could specify the same
   * JNDI DataSource for both.
   *
   * A transactional JDBC {@code Connection} for this {@code DataSource} will be provided to application code
   * accessing this {@code DataSource} directly via {@code DataSourceUtils} or {@code DataSourceTransactionManager}.
   *
   * The {@code DataSource} specified here should be the target {@code DataSource} to manage transactions for, not
   * a {@code TransactionAwareDataSourceProxy}. Only data access code may work with
   * {@code TransactionAwareDataSourceProxy}, while the transaction manager needs to work on the
   * underlying target {@code DataSource}. If there's nevertheless a {@code TransactionAwareDataSourceProxy}
   * passed in, it will be unwrapped to extract its target {@code DataSource}.
   */
  public void setDataSource(DataSource dataSource) {
    if (dataSource instanceof TransactionAwareDataSourceProxy) {
      // If we got a TransactionAwareDataSourceProxy, we need to perform
      // transactions for its underlying target DataSource, else data
      // access code won't see properly exposed transactions (i.e.
      // transactions for the target DataSource).
      this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
    } else {
      this.dataSource = dataSource;
    }
  }

  /**
   * Sets the {@code SqlSessionFactoryBuilder} to use when creating the {@code SqlSessionFactory}.
   *
   * This is mainly meant for testing so that mock SqlSessionFactory classes can be injected. By
   * default, {@code SqlSessionFactoryBuilder} creates {@code DefaultSqlSessionFactory} instances.
   */
  public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
    this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
  }

  /**
   * Set the MyBatis TransactionFactory to use. Default is {@code SpringManagedTransactionFactory}
   *
   * The default {@code SpringManagedTransactionFactory} should be appropriate for all cases:
   * be it Spring transaction management, EJB CMT or plain JTA. If there is no active transaction,
   * SqlSession operations will execute SQL statements non-transactionally.
   *
   * <b>It is strongly recommended to use the default {@code TransactionFactory}.</b> If not used, any
   * attempt at getting an SqlSession through Spring's MyBatis framework will throw an exception if
   * a transaction is active.
   *
   * @see SpringManagedTransactionFactory
   * @param transactionFactory the MyBatis TransactionFactory
   */
  public void setTransactionFactory(TransactionFactory transactionFactory) {
    this.transactionFactory = transactionFactory;
  }


  /**
   * <b>NOTE:</b> This class <em>overrides</em> any {@code Environment} you have set in the MyBatis
   * config file. This is used only as a placeholder name. The default value is
   * {@code SqlSessionFactoryBean.class.getSimpleName()}.
   *
   * @param environment the environment name
   */
  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  /**
   * {@inheritDoc}
   */
  public void afterPropertiesSet() throws Exception {
    notNull(dataSource, "Property 'dataSource' is required");
    notNull(sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");

    this.sqlSessionFactory = buildSqlSessionFactory();
  }

  /**
   * Build a {@code SqlSessionFactory} instance.
   *
   * The default implementation uses the standard MyBatis {@code XMLConfigBuilder} API to build a
   * {@code SqlSessionFactory} instance based on an Reader.
   *
   * @return SqlSessionFactory
   * @throws IOException if loading the config file failed
   */
  protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
    Configuration configuration;

    XMLConfigBuilder xmlConfigBuilder = null;
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    List<Resource> myConfigs=new ArrayList<Resource>();
      for (String location: this.configLocations) {
      Resource[] aConfigs=null;
      aConfigs = resourcePatternResolver.getResources(location);
      for (Resource r: aConfigs) {
        myConfigs.add(r);
      }
    }
    //调整顺序
    if (myConfigs!=null&&myConfigs.size()>0) {
      int i=0, flagIndex=-1;
      for (; i<myConfigs.size(); i++) {
        if ((""+myConfigs.get(i).getURI()).endsWith("/"+MySqlSessionFactoryBean.FIRST_CONFIG)) flagIndex=i;
      }
      if (flagIndex!=-1&&flagIndex!=0) {
        Resource r = myConfigs.remove(flagIndex);
        Resource _r = myConfigs.remove(0);
        myConfigs.add(0, r);
        myConfigs.add(flagIndex, _r);
      }
    }
    //正真处理
    if (myConfigs!=null&&myConfigs.size()>0) {
      xmlConfigBuilder = new XMLConfigBuilder(myConfigs.get(0).getInputStream(), null, this.configurationProperties);
      configuration = xmlConfigBuilder.getConfiguration();
    } else {
      if (logger.isDebugEnabled()) logger.debug("Property 'configLocations' not specified, using default MyBatis Configuration");
      configuration = new Configuration();
      configuration.setVariables(this.configurationProperties);
    }

    if (this.objectFactory != null) configuration.setObjectFactory(this.objectFactory);

    if (this.objectWrapperFactory != null) configuration.setObjectWrapperFactory(this.objectWrapperFactory);

    if (hasLength(this.typeAliasesPackage)) {
      String[] typeAliasPackageArray = tokenizeToStringArray(this.typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
      for (String packageToScan : typeAliasPackageArray) {
        configuration.getTypeAliasRegistry().registerAliases(packageToScan, typeAliasesSuperType == null ? Object.class : typeAliasesSuperType);
        if (logger.isDebugEnabled()) logger.debug("Scanned package: '" + packageToScan + "' for aliases");
      }
    }

    if (!isEmpty(this.typeAliases)) {
      for (Class<?> typeAlias : this.typeAliases) {
        configuration.getTypeAliasRegistry().registerAlias(typeAlias);
        if (logger.isDebugEnabled()) logger.debug("Registered type alias: '" + typeAlias + "'");
      }
    }

    if (!isEmpty(this.plugins)) {
      for (Interceptor plugin : this.plugins) {
        configuration.addInterceptor(plugin);
        if (logger.isDebugEnabled()) logger.debug("Registered plugin: '" + plugin + "'");
      }
    }

    if (hasLength(this.typeHandlersPackage)) {
      String[] typeHandlersPackageArray = tokenizeToStringArray(this.typeHandlersPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
      for (String packageToScan : typeHandlersPackageArray) {
        configuration.getTypeHandlerRegistry().register(packageToScan);
        if (logger.isDebugEnabled()) logger.debug("Scanned package: '" + packageToScan + "' for type handlers");
      }
    }

    if (!isEmpty(this.typeHandlers)) {
      for (TypeHandler<?> typeHandler : this.typeHandlers) {
        configuration.getTypeHandlerRegistry().register(typeHandler);
        if (logger.isDebugEnabled()) logger.debug("Registered type handler: '" + typeHandler + "'");
      }
    }

    if (xmlConfigBuilder != null) {
      try {
        xmlConfigBuilder.parse();
        if (logger.isDebugEnabled()) logger.debug("Parsed configuration file: '" + myConfigs.get(0) + "'");
      } catch (Exception ex) {
        throw new NestedIOException("Failed to parse config resource: " + myConfigs.get(0), ex);
      } finally {
        ErrorContext.instance().reset();
      }
      
      //扩展内容从多个配置文件中读取信息，但注意setting;environment不进行加载，只加在第一个配置文件中的内容
      Configuration tc=null;
      for (int i=1; i<myConfigs.size(); i++) {
        tc=null;
        XMLConfigBuilder xmlConfigBuilder1 = null;
        xmlConfigBuilder1 = new XMLConfigBuilder(myConfigs.get(i).getInputStream(), null, this.configurationProperties);
        tc = xmlConfigBuilder1.getConfiguration();
        if (xmlConfigBuilder1!=null) {
          try {
            tc=xmlConfigBuilder1.parse();
            //propertiesElement(root.evalNode("properties"))
            Properties p = tc.getVariables();
            Properties p2 = configuration.getVariables();
            if (p!=null) p2.putAll(p);
            configuration.setVariables(p2);
            //pluginElement(root.evalNode("plugins"))
            List<Interceptor> l = tc.getInterceptors();
            if (l!=null) {
              for (Interceptor itc: l) configuration.addInterceptor(itc);
            }
            //objectFactoryElement(root.evalNode("objectFactory"))
            if (tc.getObjectFactory()!=null) configuration.setObjectFactory(tc.getObjectFactory());
            //objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"))
            if (tc.getObjectWrapperFactory()!=null) configuration.setObjectWrapperFactory(tc.getObjectWrapperFactory());
            //setting NO
            //environments NO
            //databaseIdProviderElement(root.evalNode("databaseIdProvider"))
            if (tc.getDatabaseId()!=null) configuration.setDatabaseId(tc.getDatabaseId());
            //typeAliasesElement(root.evalNode("typeAliases"))
            TypeAliasRegistry tar = tc.getTypeAliasRegistry();
            if (tar!=null) {
              Map<String, Class<?>> m = tar.getTypeAliases();
              if (m!=null) {
                Iterator<String> iterator=m.keySet().iterator();
                while(iterator.hasNext()){
                  String alias=String.valueOf(iterator.next());
                  configuration.getTypeAliasRegistry().registerAlias(alias, m.get(alias));
                }
              }
            }
            //typeHandlerElement(root.evalNode("typeHandlers"))
            TypeHandlerRegistry thr = tc.getTypeHandlerRegistry();
            if (thr!=null) {
              Collection<TypeHandler<?>> thc = thr.getTypeHandlers();
              if (thc!=null) {
                for(TypeHandler<?> th: thc) configuration.getTypeHandlerRegistry().register(th);
              }
            }
            //mapper这是重点
            Collection<Class<?>> mc = tc.getMapperRegistry().getMappers();
            if (mc!=null) {
              for(Class<?> c: mc) configuration.addMapper(c);
            }
            if (tc.getMappedStatementNames()!=null) {
              for (String name:tc.getMappedStatementNames()) {
                MappedStatement ms = tc.getMappedStatement(name);
                try {
                  configuration.addMappedStatement(ms);
                } catch(IllegalArgumentException iae) {
                  if (logger.isDebugEnabled()) {
                    logger.debug(iae.getMessage());
                  }
                }
              }
            }
            if (logger.isDebugEnabled()) logger.debug("Parsed configuration file: '" + myConfigs.get(i) + "'");
          } catch (Exception ex) {
            throw new NestedIOException("Failed to parse config resource: " + myConfigs.get(i), ex);
          } finally {
            ErrorContext.instance().reset();
          }
        }
      }
    }

    //处理数据库类型
    if (this.databaseType==null||this.databaseType.equals("")) {
        this.databaseType="MySql";
    }
    DatabaseType.getDatabaseType(this.databaseType);
    Properties prop = new Properties();
    prop.put("databaseType", this.databaseType);
    //处理拦截器中参数，设置数据库类型
    List<Interceptor> l = configuration.getInterceptors();
    if (l!=null) {
      for (Interceptor itc: l) {
        if (itc instanceof PageInterceptor) itc.setProperties(prop);
      }
    }

    if (this.transactionFactory == null) {
      this.transactionFactory = new SpringManagedTransactionFactory();
    }

    Environment environment = new Environment(this.environment, this.transactionFactory, this.dataSource);
    configuration.setEnvironment(environment);

    if (this.databaseIdProvider != null) {
      try {
        configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
      } catch (SQLException e) {
        throw new NestedIOException("Failed getting a databaseId", e);
      }
    }

    //
    
    if (!isEmpty(this.mapperLocations)) {
      for (Resource mapperLocation : this.mapperLocations) {
        if (mapperLocation == null) {
          continue;
        }

        try {
          XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), configuration, mapperLocation.toString(), configuration.getSqlFragments());
          xmlMapperBuilder.parse();
        } catch (Exception e) {
          throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
        } finally {
          ErrorContext.instance().reset();
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Parsed mapper file: '" + mapperLocation + "'");
        }
      }
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("Property 'mapperLocations' was not specified or no matching resources found");
      }
    }

    return this.sqlSessionFactoryBuilder.build(configuration);
  }

  /**
   * {@inheritDoc}
   */
  public SqlSessionFactory getObject() throws Exception {
    if (this.sqlSessionFactory == null) {
      afterPropertiesSet();
    }
    return this.sqlSessionFactory;
  }

  /**
   * {@inheritDoc}
   */
  public Class<? extends SqlSessionFactory> getObjectType() {
    return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isSingleton() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void onApplicationEvent(ApplicationEvent event) {
    if (failFast && event instanceof ContextRefreshedEvent) {
      // fail-fast -> check all statements are completed
      this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
    }
  }
}