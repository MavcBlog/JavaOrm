package mavc.blog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataConnection {
	private String user;
	private String password;
	private String host = "127.0.0.1";
	private String port;
	private String dataBase;
	private String drive;		
	private String RDBMS;
}
