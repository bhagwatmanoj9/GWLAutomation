package baseClasses;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class GridHelper extends SeleniumUtils {

	public String getGridColumnText(String tableIdoRxPath,int row,int column) {
		
		WebElement element = null;
		String textValue = null;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/tbody" + "/tr[" + row + "]/td[" + column + "]"));
			
			if (element == null)
			{
				textValue = null;
			}else
			{
				textValue = element.getText().trim();
			}
			
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
		return textValue;
	}
	
	public String getGridAttributeValue(String tableIdoRxPath,int row,int column,String AttrName) {
		
		WebElement element = null;
		String attrValue = null;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/tbody" + "/tr[" + row + "]/td[" + column + "]"));
			if (element == null)
			{
				attrValue = null;
			}else
			{
				attrValue = element.getAttribute(AttrName).trim();
			}
						
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
		return attrValue;
	}

	public void clickInGrid(String tableIdoRxPath,int row,int column) {
		
		WebElement element = null;
		
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/tbody" + "/tr[" + row + "]/td[" + column + "]"));
			if (element == null)
			{
				
			}else
			{
				scrollIntoView(element);
				sleep(1000);
				element.click();
			}
			
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
	}	
	
	public void sendTextToGrid(String tableIdoRxPath,int row,int column, String inText) {
		
		WebElement element = null;
		
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/tbody" + "/tr[" + row + "]/td[" + column + "]//input"));
			if (element == null)
			{
				
			}else
			{
				element.clear();
				element.sendKeys(inText);
				element.sendKeys(Keys.TAB);
			}
			
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
	}	
	
	public void clickInGridAnchor(String tableIdoRxPath,int row,int column) {
		
		WebElement element = null;
		
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/tbody" + "/tr[" + row + "]/td[" + column + "]"));
			if (element == null)
			{
				
			}else
			{
				element.findElement(By.tagName("a")).click();
			}
			
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
	}	
	
	public void clickInGridInput(String tableIdoRxPath,int row,int column) {
		
		WebElement element = null;
		
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/tbody" + "/tr[" + row + "]/td[" + column + "]"));
			if (element == null)
			{
				
			}else
			{
				element.findElement(By.tagName("input")).click();
			}
			
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
	}
	
	public int getGridNumOfRows(String tableIdoRxPath) {
		
		WebElement element = null;
		int rowCount = 0;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath));
			List<WebElement>TotalRowsList = element.findElements(By.xpath("//div[@role='rowgroup'][2]//div[@role='row']"));
			rowCount = TotalRowsList.size();
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
		return rowCount;
	}
	
	public int getGridNumOfColumns(String tableIdoRxPath,int row) {
		
		WebElement element = null;
		int colCount = 0;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath));
			List<WebElement> TotalColsList = element.findElements(By.xpath("//div[contains(@class,'cpq-table-column-header-text')]"));
			colCount = TotalColsList.size();
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
		return colCount;
	}
	
	public int getLICount(String tableIdoRxPath) {
		
		WebElement element = null;
		int rowCount = 0;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath));
			List<WebElement>TotalRowsList = element.findElements(By.tagName("li"));
			rowCount = TotalRowsList.size();
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
		return rowCount;
	}
	
	public String getTextFromLI(String tableIdoRxPath, int lino) {
		
		WebElement element = null;
		String textValue = null;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath));
			List<WebElement>TotalRowsList = element.findElements(By.tagName("li"));
			textValue = TotalRowsList.get(lino).getText();
			
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
		return textValue;
	}
	
		public void clickLIElement(String tableIdoRxPath, String liText) {
		
		WebElement element = null;
		try {
			element = webDriver.findElement(By.xpath(tableIdoRxPath + "/descendant::div[contains(text(),'"+liText+"')]"));
			element.click();
		} catch (NoSuchElementException e) {
			// Ignore
		}
		
	}
	
		public void deselectColumns(String tableIdoRxPath) throws InterruptedException {
			
			try {
				List<WebElement>TotalRowsList = webDriver.findElements(By.xpath(tableIdoRxPath));
				Thread.sleep(5000);
				for (WebElement liElement : TotalRowsList) {
					liElement.click();
	            }
				
			} catch (NoSuchElementException e) {
				// Ignore
			}
			
		}
	
		public String getTextFromElementList(String tableIdoRxPath, int eleNo) {
			
			String textValue = null;
			try {
				List<WebElement>TotalRowsList = webDriver.findElements(By.xpath(tableIdoRxPath));
				textValue = TotalRowsList.get(eleNo).getText();
				
			} catch (NoSuchElementException e) {
				// Ignore
			}
			
			return textValue;
		}
		
		
}
