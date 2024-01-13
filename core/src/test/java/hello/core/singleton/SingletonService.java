package hello.core.singleton;

public class SingletonService {

	private SingletonService() {
		
	}

	private static final SingletonService instance= new SingletonService();

	public static SingletonService getInstance() {
		return instance;
	}
	
	
}
