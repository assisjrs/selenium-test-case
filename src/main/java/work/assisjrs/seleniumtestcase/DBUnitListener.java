package work.assisjrs.seleniumtestcase;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.operation.DefaultDatabaseOperationLookup;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.io.File;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * Created by assis on 15/03/17.
 */
public class DBUnitListener extends AbstractTestExecutionListener {
    private DataSource dbUnitDatabaseConnection;

    @Override
    public void beforeTestClass(final TestContext testContext) throws Exception {
        final ApplicationContext context = testContext.getApplicationContext();

        if (context instanceof ConfigurableApplicationContext) {
            final DatabaseSetup annotation = findAnnotation(testContext.getTestClass(), DatabaseSetup.class);

            if (annotation == null) return;

            final String connection = annotation.connection().isEmpty()? "dbUnitDatabaseConnection" : annotation.connection();

            dbUnitDatabaseConnection = context.getBean(connection, DataSource.class);

            final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);

            final IDataSet dataSet = builder.build(new File("src/test/resources/" + annotation.value()[0]));
            final DatabaseConnection databaseConnection = new DatabaseConnection(dbUnitDatabaseConnection.getConnection());

            new DefaultDatabaseOperationLookup().get(annotation.type()).execute(databaseConnection, dataSet);
        }
    }
}
