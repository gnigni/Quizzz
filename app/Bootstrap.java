import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job{

	
	public void setup(){
	if(User.count() == 0){
		Fixtures.loadModels("initial-data.yml");
	}
	}
}
