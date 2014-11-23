package quote;

import catalog.Assembly;
import catalog.Product;
import catalog.dao.CatalogPart;
import mgr.CatalogMgr;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import part.Part;
import util.AppRuntimeException;
import util.CmdRuntimeException;
import util.FileUtil;

import java.net.URL;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(JukitoRunner.class)
public class QuoteTestJukito {

//    FileUtil util = new FileUtil();

//    public static class GuiceTestModule extends JukitoModule {
//        protected void configureTest() {
//            bind(CatalogMgr.class);
//            bind(QuotePartResolver.class);
//        }
//    };

//    @Inject
//    QuoteEngine engine;

    //    @Before
//    public void setupMocks (CatalogMgr mgr) {
//        when(mgr.getPart(anyString())).thenReturn(new Part());
//    }
//    @Before
//    public void setupMocks(PartSQL partSQL) {
//        when(partSQL.getCurrentPartDoc(anyString())).thenReturn(new PartDoc());
//    }

    @Test
    public void dummy () {

    }
}
