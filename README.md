# jwp-basic
자바 웹 프로그래밍 기본 실습

### Servlet Container의 초기화 과정 

### ContextLoaderListener(ServletContextListener)
서버를 시작하면 다음과 같은 로그가 뜬다. 
> org.springframework.jdbc.datasource.init.ResourceDatabasePopulator executeSqlScript    
정보: Executing SQL script from class path resource [jwp.sql]    
org.springframework.jdbc.datasource.init.ResourceDatabasePopulator executeSqlScript    
정보: Done executing SQL script from class path resource [jwp.sql] in 16 ms.

> [INFO ] [localhost-startStop-1] [n.s.context.ContextLoaderListener] - Completed Load ServletContext!

데이터베이스와 소통하는 작업을 하기 위해서는 `Connection객체`를 얻어와야 하는데, 이는 시간이 꽤 많이 걸리는 작업이다.
따라서 Connection을 서버가 시작될 때 미리 만들어서 저장해두고, 그때그때 커넥션이 필요해지면 저장해둔 커넥션을 가져다가 쓰도록 한다.  
이 과정이 `ContextLoaderListener` 안에서 진행된다. 

```java
@WebListener
public class ContextLoaderListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);
	
//	contextInitialized()
//	웹어플리케이션이 시작되었을 때 호출된다. 
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//populator는 외부에서 sql스크립트를 가지고 와서 실행하여 DB를 생성하는 역할을 한다.
		//addScript(Resource)메소드를 사용해서 populator에게 sql스크립트의 위치를 알려준다. 
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

//(cf)
//public static DataSource getDataSource() {
//	BasicDataSource ds = new BasicDataSource();
//	ds.setDriverClassName(DB_DRIVER);
//	ds.setUrl(DB_URL);
//	ds.setUsername(DB_USERNAME);
//	ds.setPassword(DB_PW);
//	return ds;
//}
```
**ServletContextListener는 어플리케이션이 시작되는(끝나는) 때가 언제인지 주시하고 있다가 때가 되면 실행된다**     
`contextInitialized()`의 목적은 어플리케이션의 초기화이기 때문에, 어떤 서블릿보다도 먼저 실행된다. 
즉, 얘를 불러서 실행시키는 다른 서블릿이 있는 게 아니라, 
말 그대로 ServletContext가 생기고 사라지는(웹어플리케이션이 시작되고 종료되는) `이벤트(ServletContextEvent sce)`가 발생하면 그때 실행된다. 

### DispatcherServlet

`"Completed Load ServletContext!"`가 뜨고 데이터베이스 초기화가 끝난 다음에는 이런 로그가 뜬다.    
>[INFO ] [localhost-startStop-1] [core.mvc.DispatcherServlet] - Initialized Request Mapping!    

이 로그는 URL마다 그 주소로 들어왔을 때 해야하는 일을 지정해주는 일을 다 하고 나서 찍히게 된다.    
먼저 `DispatcherServlet`이 다음과 같이 초기화된다. 
```java
	@Override
	public void init() throws ServletException {
		rm = new RequestMapping();
		rm.initMapping();
	}
```

여기서 호출되는 `RequestMapping`에서 URL-Controller맵핑을 실제로 수행한다. 
```java
public class RequestMapping {
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
	private Map<String, Controller> mappings = new HashMap<>();
	
	void initMapping() {
		mappings.put("/", new HomeController());
    //필요한 url-controller 쌍을 계속 추가 

		logger.info("Initialized Request Mapping!"); //로그 창에서 보았던 그것! 
	}
```
`mappings` 해쉬맵에 URL과 Controller를 쌍으로 맺어서 저장하고 있다.     
즉, `/`로 접속하면 `HomeController`가 알아서 처리하도록 부탁하는 것이다. 

### localhost:8080/으로 접속했을 때 서버에서 일어나는 일 
`localhost:8080/`으로 접속하면, 일단 `DispatcherServlet`의 `service()`로 들어온다. 

```java
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI(); 
		logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

		Controller controller = rm.findController(req.getRequestURI()); 
		ModelAndView mav;
		try {
			mav = controller.execute(req, resp);
			View view = mav.getView();
			view.render(mav.getModel(), req, resp);
		} catch (Throwable e) {
			logger.error("Exception : {}", e);
			throw new ServletException(e.getMessage());
		}
	}
```

rm(request mapping)인스턴스의 `findController`를 통해 지금 요청이 들어온 URI는 어떤 컨트롤러에서 처리하기로 했었는지 찾는다. 그리고 해당하는 컨트롤러의 `excute`를 실행한다.     
    
`/`에 맵핑되어있는 `HomeController`의 경우 다음과 같다. 
```java
public class HomeController extends AbstractController {
    private QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("index.jsp").addObject("questions", questionDao.findAll());
    }
}
```
질문과 관련한 데이터베이스 접근 작업을 중개해주는 QuestionDao의 findAll()을 사용하여 전체 질문 리스트를 가지고 와서, index.jsp에서 `questions`이라는 이름으로 꺼내서 쓸 수 있도록 넣어준다. 필요한 데이터를 갖춘 jspView는 DispatcherServlet의 service메소드로 리턴되어 render되고, 사용자에게 보여진다. 
