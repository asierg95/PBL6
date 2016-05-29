package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PsPortTest.class, PublisherTest.class, SuscriberTest.class, DataReaderTest.class })

public class AllTest {
	

}
