/**
 * 
 */
package com.usta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.usta.model.Tournament;
import com.usta.service.MailService;
import com.usta.service.RegPlayerService;
import com.usta.service.TournamentService;

/**
 * @author anil.bonigala
 *
 */
@ComponentScan(basePackages = "com.usta")
public class USTARegPlayerFinder {


	
	/**
     * @param args
     */
    public static void main(String[] args) {
    	
    	//windows options
    	//System.setProperty("webdriver.chrome.driver", "E:\\chromed\\chromedriver.exe");
    	//System.setProperty("webdriver.chrome.driver","‎⁨/usr/local/bin/chromedriver⁩");
          
    	//Linux options
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        
        TournamentService trns = new TournamentService();

        List<Tournament> tournaments = trns.getTournaments();
        for (Iterator<Tournament> iterator = tournaments.iterator(); iterator.hasNext();) {
            Tournament trn = (Tournament) iterator.next();
            System.out.println("Tournament name "+trn.getName());
            List<String> players = new ArrayList<String>();

            // Windows option
            //WebDriver driver = new ChromeDriver();
            // Linux option
            WebDriver driver = new ChromeDriver(chromeOptions);
            
            // Navigate to URL
            driver.get(trn.getUrl());

            int age = trn.getAge();

            String ageChek = "B14s";
            if (age == 16)
                ageChek = "B16s";

            WebElement events = null;

            try {

                events = driver.findElement(By.name("ctl00$mainContent$ControlTabs7$cboEvents"));
            } catch (NoSuchElementException e) {
                // TODO: handle exception
                events = driver.findElement(By.name("ctl00$mainContent$ControlTabs5$cboEvents"));
            }
            if (events != null) {

                Select eventsDropdown = new Select(events);

                List<WebElement> eventOptions = eventsDropdown.getOptions();

                String drop_down_values = "";
                for (int i = 0; i < eventOptions.size(); i++) {

                    drop_down_values = eventOptions.get(i).getText();
                    if (drop_down_values.contains(ageChek)) {
                        System.out.println(ageChek + " values  " + eventOptions.get(i).getText());
                        break;
                    }
                    // System.out.println("dropdown values are " +
                    // drop_down_values);
                }
                eventsDropdown.selectByVisibleText(drop_down_values);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {

                    WebElement htmltable = driver.findElement(By.xpath("//*[@id='applicants']/tbody")); //
                    
                    List<WebElement> rows = htmltable.findElements(By.tagName("tr"));

                    for (int rnum = 0; rnum < rows.size(); rnum++) {
                        List<WebElement> columns = rows.get(rnum).findElements(By.tagName("td"));
                        WebElement webElement = columns.get(0);
                        if (!webElement.getText().equalsIgnoreCase("name") && webElement.getAttribute("colspan") == null) {
                            String name = webElement.getText();
                            // System.out.println(name);
                            players.add(name);
                        }
                    }

				} catch (NoSuchElementException e) {
					// TODO: handle exception
				}
             
                driver.quit();

                RegPlayerService regPlayerService = new RegPlayerService();
               
                
                boolean newPlayerRegistered = regPlayerService.saveRegisteredPlayers(trn.getName(), players,trn.getId());
                System.out.println("is a new Player registered in Tourn"+ trn.getName()+" = "+newPlayerRegistered);
                if (newPlayerRegistered) {
	                Map<Integer, String> playerRanks =  regPlayerService.getTournamentRanks(players, age);
	                String message = getPlayerListHtml (playerRanks) ;//playerRankService.getTournamentRanks(trn.getName(), trn.getAge());
	                System.out.println(" Sending email for "+trn.getName());
	                
	                MailService mailService  = new MailService();
	                mailService.sendMail(trn.getName(), message);
                }
                
            }
        }
    }

    private static String getPlayerListHtml(Map<Integer, String> playerRanks) {
		
    	
    	Integer aRank = new Integer(0);
        String keyPlayer="Bonigala, Aryan";
        for(Map.Entry<Integer, String> entry: playerRanks.entrySet()){
            if(keyPlayer.equalsIgnoreCase(entry.getValue())){
                aRank = entry.getKey();
                break; 
            }
        }
    	
        Map<Integer, String> playerRanksAbove = new TreeMap<Integer, String>();
        Map<Integer, String> playerRanksBelow = new TreeMap<Integer, String>();
        
        for(Map.Entry<Integer, String> entry: playerRanks.entrySet()){
        	if (entry.getKey() > aRank) {
        		playerRanksBelow.put(entry.getKey(), entry.getValue());
        	} else {
        		playerRanksAbove.put(entry.getKey(), entry.getValue());
        	}
        }
        
        int totRanksBelow = playerRanksBelow.size();
        int totRanksAbove = playerRanksAbove.size();
        
        
    	StringBuilder sb = new StringBuilder();
    	sb.append("<html>");
    	sb.append("<head>");
    	sb.append("</head>");
    	sb.append("<table>");
    	sb.append("<th colspan=2> Total players above "+ totRanksAbove + "</th>");
    	sb.append("<tr>");
    	sb.append("<th> Rank </th>");
    	sb.append("<th> Player </th>");
    	sb.append("</tr>");
    	for (Map.Entry<Integer, String> entry : playerRanksAbove.entrySet()) {
    	    sb.append("<tr>");
    	    sb.append("<td> " + entry.getKey() + " </td>");
    	    sb.append("<td> " + entry.getValue() + " </td>");
    	    sb.append("</tr>");
    	}
    	sb.append("</table>");
    	sb.append("</Br>");
    	sb.append("</Br>");
    	sb.append("<table>");
    	sb.append("<th colspan=2> Total players below "+ totRanksBelow + "</th>");
    	sb.append("<tr>");
    	sb.append("<th> Rank </th>");
    	sb.append("<th> Player </th>");
    	sb.append("</tr>");
    	for (Map.Entry<Integer, String> entry : playerRanksBelow.entrySet()) {
    	    sb.append("<tr>");
    	    sb.append("<td> " + entry.getKey() + " </td>");
    	    sb.append("<td> " + entry.getValue() + " </td>");
    	    sb.append("</tr>");
    	}
    	sb.append("</table>");
    	
    	sb.append("</body>");
    	sb.append("</html>");
    	return sb.toString();
    }

}
