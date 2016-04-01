package next.support.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import core.jdbc.ConnectionManager;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);
	
//	contextInitialized()
//	웹어플리케이션이 시작되었을 때 호출된다. 
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//populator는 외부에서 sql스크립트를 가지고 와서 실행하여 DB를 생성하는 역할을 한다.
		//addScript(Resource)메소를 사용해서 populator에게 sql스크립트의 위치를 알려준다. 
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(); 
		populator.addScript(new ClassPathResource("jwp.sql"));
		
		//populator가 세팅된 데이터베이스에 sql문을 실행한 결과를 심는다. 
		DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
		
		logger.info("Completed Load ServletContext!");
	}

//	contextDestroyed()
//	웹어플리케이션이 종료되었을 때 호출된다. 
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}

//자바 프로그램에서 데이터베이스에 연결을 시도하는 것(Connection 객체를 얻는것)은 시간이 많이 걸리는 작업이다.
//만약, 일정량의 Connection을 미리 생성시켜 저장소에 저장했다가 프로그램에서 요청이 있으면 저장소에서 Connection 꺼내 제공한다면 시간을 절약할 수 있을 것이다.
//이러한 프로그래밍 기법을 Connection Pooling이라 한다.
//
//import core.jdbc.ConnectionManager;
//ConnectionManager.java
//DBConnectionPoolManager 클래스에게 커넥션을 요청하는 추상클래스
//추상 클래스로 만든 이유는 여러 데이터베이스를 고려했기 때문이다.
//사용하는 데이터베이스에 따라 이 클래스를 상속하는 클래스를 만들어 사용한다.
//public static DataSource getDataSource() {
//	BasicDataSource ds = new BasicDataSource();
//	ds.setDriverClassName(DB_DRIVER);
//	ds.setUrl(DB_URL);
//	ds.setUsername(DB_USERNAME);
//	ds.setPassword(DB_PW);
//	return ds;
//}