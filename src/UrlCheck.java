
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlCheck {
		// url set regex
		final static String regex = "(https?):\\/\\/[-a-zA-Z0-9+&@#%?=~_|!:,.;]*[-a-zA-Z0-9+&@#%=~_|\\/]*";

		// delimiter to get url from input file 
		final static String delimiter = "[\\[\\]\"<>'\n\b\r]";


		// request url and check the response
		public static void availableURL(String host)
		{

				try {
						// request url
						URL url = new URL(host);
						URLConnection con;
						con = url.openConnection();
						
						// response
						HttpURLConnection exitCode = (HttpURLConnection) con;
						exitCode.setInstanceFollowRedirects(true);
						HttpURLConnection.setFollowRedirects(true);
						exitCode.setConnectTimeout(1000);
						
						System.out.print("["+exitCode.getResponseCode()+"] "+ host +" - ");
						
						// response code result
						if(exitCode.getResponseCode() == 200)
						{ 
							System.out.println("Good");
						}
						else if(exitCode.getResponseCode() == 400 || exitCode.getResponseCode() == 404)
						{
							System.out.println("Bad");
						}
						else if(exitCode.getResponseCode() == 301 || exitCode.getResponseCode() == 307 || exitCode.getResponseCode() == 308 )
						{
							System.out.println("Redirect");
							
							// redirect to new location by Recursion itself when it is 301,307,308
							String newUrl = exitCode.getHeaderField("Location");
							availableURL(newUrl);	
							
						}
						else
						{
							System.out.println("Unknown");				
						}

				}catch (Exception e) {
					// response fail, server is not existed
					System.out.println("[599] "+ host +" - Fail" );

				}

		}
		
		public static void helpMessage()
		{
			System.out.println("========================================================");
			System.out.println("|               Help message                           |");
			System.out.println("========================================================");
			System.out.println("| Please Type proper argument                          |");
			System.out.println("|                                                      |");
			System.out.println("| 1) UrlCheck <fileName>                               |");
			System.out.println("| ex) UrlCheck index.html                              |");
			System.out.println("|     UrlCheck index.html index2.html                  |");
			System.out.println("|                                                      |");
			System.out.println("| 2) UrlCheck help                                     |");
			System.out.println("|                                                      |");
			System.out.println("| 3) UrlCheck -a <fileName>                            |");
			System.out.println("| : To check for archived versions of URLs             |");
			System.out.println("| ex) UrlCheck -a index2.html                          |");
			System.out.println("|                                                      |");
			System.out.println("| 4) UrlCheck -s <fileName>                            |");
			System.out.println("| : To request URLs with https                         |");
			System.out.println("| ex) UrlCheck -s index2.html                          |");
			System.out.println("|                                                      |");
			System.out.println("| Thanks!                                              |");
			
		}
		
		public static void startMessage()
		{
			System.out.println("========================================================");
			System.out.println("|             URL TEST RUNNING                         |");
			System.out.println("========================================================");

		}
		
		public static void endMessage()
		{
			
			System.out.println("========================================================");
		}
		

		// list up url in input file
		public static void fileUrlListUp(String fName, boolean archived, boolean secured)
		{
			String regSecure = "^(http)://";
			Pattern pat = Pattern.compile(regex, Pattern.MULTILINE);
			
			BufferedReader br;
			
			try {
				br = new BufferedReader(new FileReader(fName));
				String line = null;
				while((line = br.readLine()) != null)
				{
					String[] values = line.split(delimiter);
					for(String str : values)
					{
						Matcher matcher = pat.matcher(str);
						if(matcher.find())
						{
							// change http to https
							if(secured) str = str.replaceFirst(regSecure, "https://");	

							availableURL(str);
							
							// request archived
							if(archived) archiveUrl(str);
						}
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				
				System.out.println("File not Found");
			} catch (IOException e) {
				System.out.println("File not Found");
			}

		}
		
		// Archive API
		public static void archiveUrl(String host)
		{
		
			String apiUrl = "http://archive.org/wayback/available?url=" + host;
			StringBuilder sb = new StringBuilder();
			

			try {
				URL	url = new URL(apiUrl);
				URLConnection con;
				con = url.openConnection();
				
				
				BufferedReader brd = new BufferedReader(new InputStreamReader(con.getInputStream(),Charset.defaultCharset()));
				if(brd != null)
				{
					int cp;
					while((cp = brd.read())!= -1)
					{
						sb.append((char)cp);
					}
					brd.close();
				}
				System.out.println(sb);
				
			}catch(Exception e)
			{
				throw new RuntimeException("Exception URL : "+ host, e);
			}
		}
		
	public static void main(String[] args) {
		
		boolean archived = false;
		boolean secured = false;
		
		if(args.length == 0 || args[0].toLowerCase().equals("help"))
		{
			helpMessage();
		}
		else 
		{
			startMessage();
			
			// command line flag a, s
			if(args[0].startsWith("-"))
			{
				// archive flag handle
				if(args[0].contains("a")) archived = true;
		
				// secure request flag handle
				if(args[0].contains("s")) secured = true;
				
				for(int i = 1; i < args.length; i++)
				{
					System.out.println("File :  " + args[i]);
					fileUrlListUp(args[i],archived,secured);					
				}	
				
			}
			else {
				for(int i = 0; i < args.length; i++)
				{
					System.out.println("File :  " + args[i]);
					fileUrlListUp(args[i],archived,secured);					
				}
			}
		}
		
		endMessage();

	}

}