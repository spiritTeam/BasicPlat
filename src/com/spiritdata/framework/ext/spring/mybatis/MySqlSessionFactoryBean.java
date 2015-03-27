package com.spiritdata.framework.ext.spring.mybatis;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.BlobByteObjectArrayTypeHandler;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.ByteArrayTypeHandler;
import org.apache.ibatis.type.ByteObjectArrayTypeHandler;
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

import com.spiritdata.framework.core.dao.DatabaseType;
import com.spiritdata.framework.ext.mybatis.interceptor.PageInterceptor;
import com.spiritdata.framework.util.StringUtils;

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
    //test
//    Configuration c = this.sqlSessionFactory.getConfiguration();
//    TypeHandlerRegistry thr = c.getTypeHandlerRegistry();
//    Map<Type, Map<JdbcType, TypeHandler<?>>> mm = thr.getMp();
//    System.out.println("THR=="+c.hashCode()+":"+thr.hashCode()+":"+mm.hashCode());
//    Iterator<Type> iter1 = mm.keySet().iterator();
//    int j=0;
//    while(iter1.hasNext()) {
//        Type t = iter1.next();
//        System.out.println((j++)+":"+t+"=="+mm.get(t).hashCode());
//    }
//
//    Map<JdbcType, TypeHandler<?>> mt = (thr.getMp()).get(Object.class);
//    Iterator<JdbcType> iter = mt.keySet().iterator();
//    int i=0;
//    System.out.println("TYPE_HANDLER_MAP===begin:"+mt.hashCode());
//    while(iter.hasNext()){
//        JdbcType jt= iter.next();
//        TypeHandler<?> th = mt.get(jt);
//        System.out.println((i++)+":"+(jt==null?"null":jt)+"=="+th.getClass());
//    }
//    System.out.println("TYPE_HANDLER_MAP===  end");
    //test
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
    //真正处理
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
      
      //扩展内容从多个配置文件中读取信息，但注意setting;environment不进行加载，只从第一个配置文件中加载setting;environment等信息
      Configuration tc=null;
      for (int i=1; i<myConfigs.size(); i++) {
        tc=null;
        XMLConfigBuilder xmlConfigBuilder1 = null;
        xmlConfigBuilder1 = new XMLConfigBuilder(myConfigs.get(i).getInputStream(), null, this.configurationProperties);
        if (xmlConfigBuilder1!=null) {
          try {
            tc=xmlConfigBuilder1.parse();

            //1-propertiesElement(root.evalNode("properties"))
            Properties p = tc.getVariables();
            Properties p2 = configuration.getVariables();
            if (p!=null) p2.putAll(p);
            configuration.setVariables(p2);

            //2-typeAliasesElement(root.evalNode("typeAliases"));
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

            //3-pluginElement(root.evalNode("plugins"))
            List<Interceptor> l = tc.getInterceptors();
            if (l!=null) {
              for (Interceptor itc: l) configuration.addInterceptor(itc);
            }

            //4-objectFactoryElement(root.evalNode("objectFactory"))，注意，若之前有ObjectFactroy已经配置，则后续的配置都不起作用
            if (tc.getObjectFactory()!=null) configuration.setObjectFactory(tc.getObjectFactory());

            //5-objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"))，注意，若之前有ObjectWrapperFactory已经配置，则后续的配置都不起作用
            if (tc.getObjectWrapperFactory()!=null) configuration.setObjectWrapperFactory(tc.getObjectWrapperFactory());

            //6-setting NO，基本设置，只在第一个文件中处理
            //7-environments NO，环境设置(数据库等)只在第一个文件中处理

            //8-databaseIdProviderElement(root.evalNode("databaseIdProvider"))
            if (tc.getDatabaseId()!=null) configuration.setDatabaseId(tc.getDatabaseId());

            //9-typeHandlerElement(root.evalNode("typeHandlers"))
            TypeHandlerRegistry thr = tc.getTypeHandlerRegistry();

//            TypeHandlerRegistry thrc = configuration.getTypeHandlerRegistry();
//            List<Type> lm = new ArrayList<Type>();
//            Iterator<Type> iter1 = mm.keySet().iterator();
//          while(iter1.hasNext()) {
//              lm.add(iter1.next());
//          }
//          Type tttt=lm.get(0);
//          Collections.sort(lm, new Comparator<Type>(){
//              @Override
//              public int compare(Type o1, Type o2) {
//                  return (o1+"").compareTo(o2+"");
//              }
//          });
//          for (int a=0; a<lm.size(); a++) {
//              System.out.println("h<"+a+":==:"+lm.get(a)+":==:"+lm.get(a).getClass().getName());
//          }
//===========================
            if (thr!=null) {
              Collection<TypeHandler<?>> thc = thr.getTypeHandlers();
              for (TypeHandler<?> th: thc) {
                  if (th instanceof ArrayTypeHandler) continue;
                  if (th instanceof BlobByteObjectArrayTypeHandler) continue;
                  if (th instanceof BlobTypeHandler) continue;
                  if (th instanceof ByteArrayTypeHandler) continue;
                  if (th instanceof ByteObjectArrayTypeHandler) continue;
                  configuration.getTypeHandlerRegistry().register(th);
              }
            }

            //10-mapper这是重点
            XPathParser mapperParser = new XPathParser(myConfigs.get(i).getInputStream(), true, this.configurationProperties, new XMLMapperEntityResolver());
            XNode root = mapperParser.evalNode("/configuration");
            try {
              XNode parent = root.evalNode("mappers");
              if (parent != null) {
                for (XNode child : parent.getChildren()) {
                  if ("package".equals(child.getName())) {
                    String mapperPackage = child.getStringAttribute("name");
                    configuration.addMappers(mapperPackage);
                  } else {
                    String resource = child.getStringAttribute("resource");
                    String url = child.getStringAttribute("url");
                    String mapperClass = child.getStringAttribute("class");
                    if (resource != null && url == null && mapperClass == null) {
                      ErrorContext.instance().resource(resource);
                      InputStream inputStream = Resources.getResourceAsStream(resource);
                      XMLMapperBuilder mapperContentParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                      mapperContentParser.parse();
                    } else if (resource == null && url != null && mapperClass == null) {
                      ErrorContext.instance().resource(url);
                      InputStream inputStream = Resources.getUrlAsStream(url);
                      XMLMapperBuilder mapperContentParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
                      mapperContentParser.parse();
                    } else if (resource == null && url == null && mapperClass != null) {
                      Class<?> mapperInterface = Resources.classForName(mapperClass);
                      configuration.addMapper(mapperInterface);
                    } else {
                      throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
                    }
                  }
                }
              }
            } catch (Exception e) {
              throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
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
    if (StringUtils.isNullOrEmptyOrSpace(this.databaseType)) {
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
        if (mapperLocation == null) continue;
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
    if (this.sqlSessionFactory == null) afterPropertiesSet();
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
      this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
    }
  }
}