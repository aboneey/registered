/**
 * 
 */
package com.usta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.usta.model.Tournament;
import com.usta.service.RegPlayerService;
import com.usta.service.TournamentService;

/**
 * @author anil.bonigala
 *
 */
public class USTARegPlayerFinder {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "E:\\chromed\\chromedriver.exe");

        TournamentService trns = new TournamentService();

        List<Tournament> tournaments = trns.getTournaments();
        for (Iterator<Tournament> iterator = tournaments.iterator(); iterator.hasNext();) {
            Tournament trn = (Tournament) iterator.next();
            System.out.println("Tournament name "+trn.getName());
            List<String> players = new ArrayList<String>();

            WebDriver driver = new ChromeDriver();

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
                regPlayerService.saveRegisteredPlayers(trn.getName(), players);

            }
        }
    }

}
