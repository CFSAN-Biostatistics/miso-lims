package uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.element.DataTable;

public class ListPage extends HeaderFooterPage implements AbstractListPage {

  @FindBy(id = "listingTable")
  private WebElement listingTable;
  @FindBy(className = "parsley-error")
  private WebElement errors;
  @FindBy(id = "listingTable_processing")
  private WebElement processing;

  private WebElement addButton = null;

  private final DataTable table;

  public ListPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
    waitWithTimeout().until(ExpectedConditions.visibilityOf(listingTable));
    table = new DataTable(listingTable);
    if (!driver.findElements(By.linkText("Add")).isEmpty()) {
      addButton = driver.findElement(By.linkText("Add"));
    }
  }

  public static ListPage getListPage(WebDriver driver, String baseUrl, String listTarget) {
    String url = String.format("%smiso/%s", baseUrl, listTarget);
    driver.get(url);
    return new ListPage(driver);
  }

  @Override
  public DataTable getTable() {
    return table;
  }

  @Override
  public WebElement getErrors() {
    return errors;
  }

  @Override
  public void sortByColumn(String columnHeading) {
    table.clickToSort(columnHeading);
    waitWithTimeout().until(ExpectedConditions.invisibilityOf(processing));
  }

  public void clickAddButton(boolean expectAddDialog) {
    if (addButton == null) throw new IllegalArgumentException("Add button is not present on page");
    addButton.click();
    if (expectAddDialog) {
      // add the dialog
    } else {
      WebElement html = getHtmlElement();
      waitForPageRefresh(html);
    }
  }
}
