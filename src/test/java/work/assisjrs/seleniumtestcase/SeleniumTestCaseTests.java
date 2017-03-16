package work.assisjrs.seleniumtestcase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SeleniumTestCase(pageObject = SeleniumTestCaseTests.DummyPage.class)
public class SeleniumTestCaseTests {
	@Autowired
	public DummyPage page;

    @Autowired
    public WebDriver driver;

    @Test
    public void deveCarregarOWebDriverDoContexto() {
        Assert.assertNotNull(driver);
    }

	@Test
	public void deveCarregarOObjetoPageDoContexto() {
		Assert.assertNotNull(page);
	}

    @Test
    public void deveInjetarOWebDriverNoPageObject() {
        Assert.assertNotNull(page.driver);
    }

	public static class DummyPage {
		public WebDriver driver;

		public DummyPage(WebDriver driver){
			this.driver = driver;
		}
	}
}
